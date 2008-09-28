package net.orcades.spring.gwt.security.server;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.concurrent.SessionRegistry;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.event.authentication.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.ui.AuthenticationDetailsSource;
import org.springframework.security.ui.FilterChainOrder;
import org.springframework.security.ui.SpringSecurityFilter;
import org.springframework.security.ui.WebAuthenticationDetailsSource;
import org.springframework.security.ui.rememberme.NullRememberMeServices;
import org.springframework.security.ui.rememberme.RememberMeServices;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.security.util.SessionUtils;
import org.springframework.security.util.TextUtils;
import org.springframework.security.util.UrlUtils;
import org.springframework.util.Assert;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;

public class GWTAuthenticationProcessingFilter extends SpringSecurityFilter
		implements InitializingBean, ApplicationEventPublisherAware,
		MessageSourceAware {

	private ThreadLocal<RPCRequest> perThreadRPCRequest = new ThreadLocal<RPCRequest>();

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

	public void doFilterHttp(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (requiresAuthentication(request, response)) {

			payloadHelper.begin(request, response);

			try {
				// Store the request & response objects in thread-local storage.
				//

				// Read the request fully.
				//
				String requestPayload = RPCServletUtils
						.readContentAsUtf8(request);

				// Let subclasses see the serialized request.
				//
				payloadHelper.onBeforeRequestDeserialized(requestPayload);

				RPCRequest rpcRequest = RPC.decodeRequest(requestPayload, null,
						payloadHelper);

				perThreadRPCRequest.set(rpcRequest);

				Authentication authResult;

				try {
					onPreAuthentication(request, response);
					authResult = attemptAuthentication(request);
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

	public Authentication attemptAuthentication(HttpServletRequest request)
			throws AuthenticationException {
		String username = obtainUsername(request);
		String password = obtainPassword(request);

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
							TextUtils.escapeEntities(username));
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

	protected String obtainUsername(HttpServletRequest request) {

		RPCRequest rpcRequest = perThreadRPCRequest.get();
		Object[] parameter = rpcRequest.getParameters();

		return (String) parameter[0];
	}

	protected String obtainPassword(HttpServletRequest request) {
		RPCRequest rpcRequest = perThreadRPCRequest.get();
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
			HttpServletResponse response, Boolean success) throws IOException {
		RPCRequest rpcRequest = perThreadRPCRequest.get();
		try {
			RPCServletUtils.writeResponse(request.getSession()
					.getServletContext(), (HttpServletResponse) response, RPC
					.encodeResponseForSuccess(rpcRequest.getMethod(), success),
					false);
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
			SessionUtils.startNewSessionIfRequired(request,
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

		sendRedirect(request, response, Boolean.TRUE);
	}

	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		RPCRequest rpcRequest = perThreadRPCRequest.get();
		try {
			RPCServletUtils.writeResponse(request.getSession()
					.getServletContext(), (HttpServletResponse) response, RPC
					.encodeResponseForSuccess(rpcRequest.getMethod(),
							Boolean.FALSE), false);
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

	public int getOrder() {
		return FilterChainOrder.AUTHENTICATION_PROCESSING_FILTER;
	}

	public void afterPropertiesSet() throws Exception {
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
}
