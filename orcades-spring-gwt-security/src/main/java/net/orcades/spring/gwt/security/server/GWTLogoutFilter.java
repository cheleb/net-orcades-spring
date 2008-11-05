package net.orcades.spring.gwt.security.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.ui.logout.LogoutHandler;
import org.springframework.util.Assert;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RPCServletUtils;

public class GWTLogoutFilter implements Filter {

	// ~ Static fields/initializers
	// ==============================================
	// =======================================

	private static final Log logger = LogFactory.getLog(GWTLogoutFilter.class);

	// ~ Instance fields
	// ========================================================
	// ========================================

	private String filterProcessesUrl = "logout.gwt";
	private String logoutSuccessUrl;
	private LogoutHandler[] handlers = new LogoutHandler[0];

	@Autowired
	private GWTPayloadHelper payloadHelper;

	// ~ Constructors
	// ============================================================
	// =======================================

	public GWTLogoutFilter(String logoutSuccessUrl, LogoutHandler[] handlers) {
		Assert.hasText(logoutSuccessUrl, "LogoutSuccessUrl required");
		Assert.notEmpty(handlers, "LogoutHandlers are required");
		this.logoutSuccessUrl = logoutSuccessUrl;
		this.handlers = handlers;
	}

	// ~ Methods
	// ================================================================
	// ========================================

	/**
	 * Not used. Use IoC container lifecycle methods instead.
	 */
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest)) {
			throw new ServletException("Can only process HttpServletRequest");
		}

		if (!(response instanceof HttpServletResponse)) {
			throw new ServletException("Can only process HttpServletResponse");
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (requiresLogout(httpRequest, httpResponse)) {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();

			if (logger.isDebugEnabled()) {
				logger.debug("Logging out user '" + auth
						+ "' and redirecting to logout page");
			}

			for (int i = 0; i < handlers.length; i++) {
				handlers[i].logout(httpRequest, httpResponse, auth);
			}

			sendRedirect(httpRequest, httpResponse, logoutSuccessUrl);

			return;
		}

		chain.doFilter(request, response);
	}

	/**
	 * Not used. Use IoC container lifecycle methods instead.
	 * 
	 * @param arg0
	 *            ignored
	 * 
	 * @throws ServletException
	 *             ignored
	 */
	public void init(FilterConfig arg0) throws ServletException {
	}

	/**
	 * Allow subclasses to modify when a logout should take place.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * 
	 * @return <code>true</code> if logout should occur, <code>false</code>
	 *         otherwise
	 */
	protected boolean requiresLogout(HttpServletRequest request,
			HttpServletResponse response) {
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');

		if (pathParamIndex > 0) {
			// strip everything from the first semi-colon
			uri = uri.substring(0, pathParamIndex);
		}

		int queryParamIndex = uri.indexOf('?');

		if (queryParamIndex > 0) {
			// strip everything from the first question mark
			uri = uri.substring(0, queryParamIndex);
		}
//FIXME be more restrictive (match context) with support for hosted mode. 
//		if ("".equals(request.getContextPath())) {
//			return uri.endsWith("/" + filterProcessesUrl);
//		}

		return uri.endsWith("/" + filterProcessesUrl);
	}

	/**
	 * Allow subclasses to modify the redirection message.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @param url
	 *            the URL to redirect to
	 * 
	 * @throws IOException
	 *             in the event of any failure
	 */
	protected void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String url) throws IOException {
		if (url != null) {
			// TODO send a redirect request to client ...
			if (!url.startsWith("http://") && !url.startsWith("https://")) {
				url = request.getContextPath() + url;
			}
		}
		try {
			payloadHelper.begin(request, response);
			RPCRequest rpcRequest = payloadHelper.decodeRPCRequest();
			RPCServletUtils.writeResponse(request.getSession()
					.getServletContext(), (HttpServletResponse) response, RPC
					.encodeResponseForSuccess(rpcRequest.getMethod(),
							Boolean.TRUE), false);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			payloadHelper.end();
		}
	}

	/**
	 * Setter for filterProcesssing logout.<br>
	 * Ensure that a leading "/" is <b>not</b> present.
	 * @param filterProcessesUrl
	 */
	public void setFilterProcessesUrl(String filterProcessesUrl) {
		Assert.hasText(filterProcessesUrl, "FilterProcessesUrl required");
		Assert.state(!filterProcessesUrl.startsWith("/"), "Not leading slash for FilterProcessesUrl");
		this.filterProcessesUrl = filterProcessesUrl;
		

	}

	protected String getFilterProcessesUrl() {
		return filterProcessesUrl;
	}
}
