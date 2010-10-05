package net.orcades.spring.gwt.security.client;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


/**
 * Module that handle secured component to enable/disable them on authentication
 * event.
 * 
 * @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 * 
 */
public class GWTSecurityModule implements EntryPoint, GWTAuthenticationListener {

	static final String GWT_SECURITY_REDIRECT_TAG = "gwt.security-redirect";

	static GWTLogoutServiceAsync logoutServiceAsync = GWT
			.create(GWTLogoutService.class);

	static final List<GWTAuthenticationListener> list = new ArrayList<GWTAuthenticationListener>();

	private static GWTAuthentication authentication;

	public void onModuleLoad() {
		addAuthenticationListener(this);

	}

	/**
	 * Ask for Granted entities and fire
	 * {@link GWTSecurityModule#fireAuthenticationPerformed(GWTAuthentication)}
	 * 
	 * @param loginURL
	 */
	public static void initOnReload(String loginURL) {
		final GWTAuthServiceAsync authServiceAsync = GWT
				.create(GWTAuthService.class);
		ServiceDefTarget serviceDefTarget = (ServiceDefTarget) authServiceAsync;

		serviceDefTarget
				.setServiceEntryPoint(GWT.getModuleBaseURL() + loginURL);
		DeferredCommand.addCommand(new Command() {

			public void execute() {
				authServiceAsync.autenticate(null, null,
						new AsyncCallback<GWTAuthentication>() {

							public void onFailure(Throwable caught) {
								if (caught instanceof GWTAuthenticationFailedException) {
									Log.info(caught.getMessage());
								} else {
									Log.error("Error on login", caught);
								}

							}

							public void onSuccess(
									GWTAuthentication authentication) {
								Log.debug(authentication.toString());
								GWTSecurityModule
										.fireAuthenticationPerformed(authentication);

							}

						});

			}

		});
	}

	static public void addAuthenticationListener(
			GWTAuthenticationListener authenticationListener) {
		list.add(authenticationListener);
	}

	static public void fireAuthenticationPerformed(
			GWTAuthentication authentication) {
		for (GWTAuthenticationListener authenticationListener : list) {
			authenticationListener.authenticated(authentication);
		}
	}

	static public void applyAuthentication(
			GWTAuthenticationListener authenticationListener) {
		if (authenticationListener == null) {
			Log.warn("No authentication!");
		} else {
			authenticationListener.authenticated(authentication);
		}

	}

	/**
	 * Fire logout event (empty {@link GWTAuthentication}).
	 */
	public static void fireLogout() {
		for (GWTAuthenticationListener authenticationListener : list) {
			authenticationListener.authenticated(new GWTAuthentication());
		}
	}

	/**
	 * Register the auth url.
	 */
	public void authenticated(GWTAuthentication authen) {
		authentication = authen;
		ServiceDefTarget serviceDefTarget = (ServiceDefTarget) logoutServiceAsync;
		serviceDefTarget.setServiceEntryPoint(GWT.getModuleBaseURL()
				+ authentication.getLogoutServiceEndPoint());

	}

	public static void logout() {
		if (authentication == null) {
			Log.warn("No user logued in!");
		} else {
			Log.info("User: " + authentication.getLogin() + " logs out!");
			logoutServiceAsync.logout(new AsyncCallback<Boolean>() {
				public void onFailure(Throwable throwable) {
					Log.error(throwable.getMessage(), throwable);
					if (isSecurityRedirect(throwable)) {
						boolean success = handleSecurityRedirect(throwable);
						if (!success) {
							Log.error(throwable.getMessage(), throwable);
						}
					} else {
						Log.error(throwable.getMessage(), throwable);
					}
				}

				/**
				 * Logout Client component
				 * {@link GWTSecurityModule#fireLogout()}
				 */
				public void onSuccess(Boolean arg0) {
					Log.debug("Logout");
					GWTSecurityModule.fireLogout();
				}
			});
			authentication = null;
		}
	}

	public static boolean isSecurityRedirect(Throwable throwable) {
		return throwable instanceof InvocationException
				&& throwable.toString().contains(
						"<meta name=\"" + GWT_SECURITY_REDIRECT_TAG + "\"");
	}

	/**
	 * 
	 * @param throwable
	 * @return true if handled ok, false on failure (
	 */
	public static boolean handleSecurityRedirect(Throwable throwable) {
		Log.debug("handleSecurityRedirect");
		Log.debug("detected meta tag:" + GWT_SECURITY_REDIRECT_TAG);
		// Some external security framework requests
		// redirection, but
		// redirection in async call needs to be handled
		// manually.
		// (for example redirection to login page)
		// Added this for supporting josso single sign-on
		String VALUE_TAG = "content=\"";
		String requestData = throwable.toString();

		int tagIndex = requestData.indexOf(GWT_SECURITY_REDIRECT_TAG);
		int valueIndex = requestData.indexOf(VALUE_TAG, tagIndex);
		int valueStartIndex = valueIndex + VALUE_TAG.length();
		int valueEndIndex = requestData.indexOf("\"", valueStartIndex + 1);

		String value = requestData.substring(valueStartIndex, valueEndIndex);
		if (value != null && value.toLowerCase().startsWith("http://")
				|| value.toLowerCase().startsWith("https://")) {
			String redirectTargetUrl = value + GWT.getModuleBaseURL();
			Log.info("Found redirect meta-tag ('" + GWT_SECURITY_REDIRECT_TAG
					+ "') in async response, redirecting to:"
					+ redirectTargetUrl);
			redirect(redirectTargetUrl);
			return true;
		} else {
			Log.error("Found redirect meta-tag ('"
					+ GWT_SECURITY_REDIRECT_TAG
					+ "') but no suitable redirectTargetUrl (should start with http:// or https://):"
					+ value);
			return false;
		}
	}

	native static void redirect(String url)
	/*-{
	        $wnd.location.replace(url);

	}-*/;
}
