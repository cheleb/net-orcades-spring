package net.orcades.spring.gwt.security.server;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.orcades.spring.gwt.component.GWTPayloadHelper;
import net.orcades.spring.gwt.security.client.GWTAuthentication;
import net.orcades.spring.gwt.security.client.GWTAuthenticationFailedException;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationProcessingFilter;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;

public class GWTAuthenticationProcessingFilter extends GenericFilterBean
	implements InitializingBean, ApplicationEventPublisherAware,
	MessageSourceAware {

    private ThreadLocal<RPCRequest> perThreadRPCRequest = new ThreadLocal<RPCRequest>();

    @Autowired
    private GWTLogoutFilter logoutFilter;

    protected ApplicationEventPublisher eventPublisher;

    private SessionRegistry sessionRegistry;

    private RememberMeServices rememberMeServices = new NullRememberMeServices();

    private boolean allowSessionCreation = true;
    /**
     * Tells if we on successful authentication should invalidate the current
     * session. This is a common guard against session fixation attacks.
     * Defaults to <code>false</code>.
     */
    private boolean invalidateSessionOnSuccessfulAuthentication = false;
    /**
     * If {@link #invalidateSessionOnSuccessfulAuthentication} is true, this
     * flag indicates that the session attributes of the session to be
     * invalidated are to be migrated to the new session. Defaults to
     * <code>true</code> since nothing will happpen unless
     * {@link #invalidateSessionOnSuccessfulAuthentication} is true.
     */
    private boolean migrateInvalidatedSessionAttributes = true;

    protected AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();

    @Autowired
    private GWTPayloadHelper payloadHelper;

    private MessageSourceAccessor messages;

    private AuthenticationManager authenticationManager;

    private String filterProcessesUrl;

    /**
     * Attempt authentication.<br />
     * If login && password are null, return the authenticated (login /
     * grantedAuthorities) if applicable (user already logued in). FIXME Not
     * sure that it's a good idea to use the authentication channel to send
     * granted authorities on reload ;)
     */
    public void doFilter(ServletRequest req, ServletResponse res,
	    FilterChain chain) throws IOException, ServletException {

	// public void doFilterHttp(HttpServletRequest request,
	// HttpServletResponse response, FilterChain chain)
	// throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) req;
	HttpServletResponse response = (HttpServletResponse) res;

	if (requiresAuthentication(request, response)) {
	    //
	    // Store the request & response objects in thread-local storage.
	    //
	    payloadHelper.begin(request, response);
	    try {

		RPCRequest rpcRequest = payloadHelper.decodeRPCRequest();

		String username = obtainUsername(rpcRequest);
		String password = obtainPassword(rpcRequest);
		perThreadRPCRequest.set(rpcRequest);

		Authentication authResult;

		try {
		    if (username == null && password == null) {
			// The page as been relead as Login panel never send
			// blank or null password.
			Principal principal = request.getUserPrincipal();
			if (principal instanceof UsernamePasswordAuthenticationToken) {
			    // Already logued sending user name and granted
			    // authorities.
			    UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) principal;
			    sendGWTAuthentication(request, response,
				    authenticationToken.getAuthorities(),
				    authenticationToken.getName());
			    return;
			}
			// Not logued send a blank authentication result
			sendGWTAuthentication(request, response,
				new ArrayList<GrantedAuthority>(), "");
			return;
		    }

		    onPreAuthentication(request, response);
		    authResult = attemptAuthentication(username, password,
			    request);
		} catch (AuthenticationException failed) {
		    // Authentication failed
		    unsuccessfulAuthentication(request, response, failed);

		    return;
		}

		// Authentication success

		successfulAuthentication(request, response, authResult);

	    } catch (Throwable e) {
		// Give a subclass a chance to either handle the exception or
		// rethrow it
		//
		payloadHelper.doUnexpectedFailure(e);
	    } finally {
		// null the thread-locals to avoid holding request/response
		//
		payloadHelper.end();
	    }
	} else {
	    chain.doFilter(request, response);
	}
    }

    public Authentication attemptAuthentication(String username,
	    String password, HttpServletRequest request)
	    throws AuthenticationException {

	if (username == null) {
	    username = "";
	}

	if (password == null) {
	    password = "";
	}

	username = username.trim();

	UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
		username, password);

	// Place the last username attempted into HttpSession for views
	HttpSession session = request.getSession(false);

	if (session != null || getAllowSessionCreation()) {
	    request
		    .getSession()
		    .setAttribute(
			    AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY,
			    StringEscapeUtils.escapeHtml(username));
	}

	// Allow subclasses to set the "details" property
	setDetails(request, authRequest);

	return getAuthenticationManager().authenticate(authRequest);
    }

    private AuthenticationManager getAuthenticationManager() {

	return authenticationManager;
    }

    public void setAuthenticationManager(
	    AuthenticationManager authenticationManager) {
	this.authenticationManager = authenticationManager;
    }

    private boolean getAllowSessionCreation() {
	return allowSessionCreation;
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
	    HttpServletResponse response) {
	String uri = request.getRequestURI();
	int pathParamIndex = uri.indexOf(';');

	if (pathParamIndex > 0) {
	    // strip everything after the first semi-colon
	    uri = uri.substring(0, pathParamIndex);
	}

	if ("".equals(request.getContextPath())) {
	    return uri.endsWith(getFilterProcessesUrl());
	}

	return uri.endsWith(getFilterProcessesUrl());
    }

    protected String obtainUsername(RPCRequest rpcRequest) {
	Object[] parameter = rpcRequest.getParameters();

	return (String) parameter[0];
    }

    protected String obtainPassword(RPCRequest rpcRequest) {

	Object[] parameter = rpcRequest.getParameters();

	return (String) parameter[1];
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
	this.filterProcessesUrl = filterProcessesUrl;
    }

    public String getFilterProcessesUrl() {
	return this.filterProcessesUrl;
    }

    protected void sendRedirect(HttpServletRequest request,
	    HttpServletResponse response, GWTAuthentication gwtAuth)
	    throws IOException {
	RPCRequest rpcRequest = perThreadRPCRequest.get();
	try {
	    RPCServletUtils.writeResponse(request.getSession()
		    .getServletContext(), (HttpServletResponse) response, RPC
		    .encodeResponseForSuccess(rpcRequest.getMethod(), gwtAuth,
			    rpcRequest.getSerializationPolicy()), false);
	} catch (SerializationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    protected void successfulAuthentication(HttpServletRequest request,
	    HttpServletResponse response, Authentication authResult)
	    throws IOException, ServletException {
	if (logger.isDebugEnabled()) {
	    logger.debug("Authentication success: " + authResult.toString());
	}

	SecurityContextHolder.getContext().setAuthentication(authResult);

	if (logger.isDebugEnabled()) {
	    logger
		    .debug("Updated SecurityContextHolder to contain the following Authentication: '"
			    + authResult + "'");
	}

	if (invalidateSessionOnSuccessfulAuthentication) {
	    startNewSessionIfRequired(request,
		    migrateInvalidatedSessionAttributes, sessionRegistry);
	}

	if (logger.isDebugEnabled()) {
	    logger.debug("Sending successfull RPC response: ");
	}

	onSuccessfulAuthentication(request, response, authResult);

	rememberMeServices.loginSuccess(request, response, authResult);

	// Fire event
	if (this.eventPublisher != null) {
	    eventPublisher
		    .publishEvent(new InteractiveAuthenticationSuccessEvent(
			    authResult, this.getClass()));
	}

        Collection<GrantedAuthority> grantedAuthorities = authResult.getAuthorities();

	String login = authResult.getName();

	sendGWTAuthentication(request, response, grantedAuthorities, login);
    }

    private void sendGWTAuthentication(HttpServletRequest request,
	    HttpServletResponse response,
	    Collection<GrantedAuthority> grantedAuthorities, String login)
	    throws IOException {
	List<String> gwtGrantedAuthorityList = new ArrayList<String>();
	for (GrantedAuthority grantedAuthority : grantedAuthorities) {
	    gwtGrantedAuthorityList.add(grantedAuthority.getAuthority());
	}

	GWTAuthentication gwtAuth = new GWTAuthentication(login,
		gwtGrantedAuthorityList, logoutFilter.getFilterProcessesUrl());

	sendRedirect(request, response, gwtAuth);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
	    HttpServletResponse response, AuthenticationException failed)
	    throws IOException, ServletException {
	RPCRequest rpcRequest = perThreadRPCRequest.get();
	try {
	    RPCServletUtils.writeResponse(request.getSession()
		    .getServletContext(), (HttpServletResponse) response, RPC
		    .encodeResponseForFailure(rpcRequest.getMethod(),
			    new GWTAuthenticationFailedException(failed
				    .getMessage()), rpcRequest
				    .getSerializationPolicy()), Boolean.FALSE);
	} catch (SerializationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request,
	    HttpServletResponse response, Authentication authResult)
	    throws IOException {

    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request,
	    HttpServletResponse response, AuthenticationException failed)
	    throws IOException {
    }

    public void setApplicationEventPublisher(
	    ApplicationEventPublisher applicationEventPublisher) {
	this.eventPublisher = applicationEventPublisher;

    }

    public void setMessageSource(MessageSource messageSource) {
	this.messages = new MessageSourceAccessor(messageSource);
    }


    public void afterPropertiesSet() {
	Assert.hasLength(getFilterProcessesUrl(),
		"filterProcessesUrl must be specified");
	Assert.isTrue(UrlUtils.isValidRedirectUrl(getFilterProcessesUrl()),
		getFilterProcessesUrl() + " isn't a valid redirect URL");
	// GWT Assert.hasLength(defaultTargetUrl,
	// "defaultTargetUrl must be specified");
	// GWT Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultTargetUrl),
	// defaultTargetUrl + " isn't a valid redirect URL");
	// Assert.hasLength(authenticationFailureUrl,
	// "authenticationFailureUrl must be specified");
	// GWT
	// Assert.isTrue(UrlUtils.isValidRedirectUrl(authenticationFailureUrl),
	// authenticationFailureUrl + " isn't a valid redirect URL");
	Assert.notNull(getAuthenticationManager(),
		"authenticationManager must be specified");
	Assert.notNull(getRememberMeServices(),
		"rememberMeServices cannot be null");
    }

    private RememberMeServices getRememberMeServices() {
	// TODO Auto-generated method stub
	return rememberMeServices;
    }

    protected void onPreAuthentication(HttpServletRequest request,
	    HttpServletResponse response) throws AuthenticationException,
	    IOException {
    }

    /**
     * Provided so that subclasses may configure what is put into the
     * authentication request's details property.
     * 
     * @param request
     *            that an authentication request is being created for
     * @param authRequest
     *            the authentication request object that should have its details
     *            set
     */
    protected void setDetails(HttpServletRequest request,
	    UsernamePasswordAuthenticationToken authRequest) {
	authRequest.setDetails(authenticationDetailsSource
		.buildDetails(request));
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
	this.sessionRegistry = sessionRegistry;
    }

    /**
     * This method was removed from spring between 2.x and 3.x, copied from
     * spring 2.x to here for quick solution.
     * 
     * @param request
     * @param migrateAttributes
     * @param sessionRegistry
     */
    private void startNewSessionIfRequired(HttpServletRequest request,
	    boolean migrateAttributes, SessionRegistry sessionRegistry) {

	HttpSession session = request.getSession(false);

	if (session == null) {
	    return;
	}

	String originalSessionId = session.getId();

	if (Log.isDebugEnabled()) {
	    Log.debug("Invalidating session with Id '" + originalSessionId
		    + "' " + (migrateAttributes ? "and" : "without")
		    + " migrating attributes.");
	}

	HashMap attributesToMigrate = null;

	if (migrateAttributes) {
	    attributesToMigrate = new HashMap();

	    Enumeration enumer = session.getAttributeNames();

	    while (enumer.hasMoreElements()) {
		String key = (String) enumer.nextElement();
		attributesToMigrate.put(key, session.getAttribute(key));
	    }
	}

	session.invalidate();
	session = request.getSession(true); // we now have a new session

	if (Log.isDebugEnabled()) {
	    Log.debug("Started new session: " + session.getId());
	}

	if (attributesToMigrate != null) {
	    Iterator iter = attributesToMigrate.entrySet().iterator();

	    while (iter.hasNext()) {
		Map.Entry entry = (Map.Entry) iter.next();
		session.setAttribute((String) entry.getKey(), entry.getValue());
	    }
	}

	if (sessionRegistry != null) {
	    sessionRegistry.removeSessionInformation(originalSessionId);
	    Object principal = obtainPrincipalFromAuthentication(SecurityContextHolder
		    .getContext().getAuthentication());

	    sessionRegistry.registerNewSession(session.getId(), principal);
	}
    }

    /**
     * This method was removed from spring between 2.x and 3.x, copied from
     * spring 2.x to here for quick solution.
     * 
     * @param auth
     * @return
     */
    private Object obtainPrincipalFromAuthentication(Authentication auth) {
	Assert.notNull(auth, "Authentication required");
	Assert.notNull(auth.getPrincipal(),
		"Authentication.getPrincipal() required");

	if (auth.getPrincipal() instanceof UserDetails) {
	    return ((UserDetails) auth.getPrincipal()).getUsername();
	} else {
	    return auth.getPrincipal();
	}
    }
}
