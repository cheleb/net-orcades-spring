package net.orcades.spring.gwt.security.client;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Module that handle secured component to enable/disable them on authentication
 * event.
 * 
 * @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 * 
 */
public class GWTSecurityModule implements EntryPoint, GWTAuthenticationListener {

	static GWTLogoutServiceAsync logoutServiceAsync = GWT
			.create(GWTLogoutService.class);

	static final List<GWTAuthenticationListener> list = new ArrayList<GWTAuthenticationListener>();

	private static GWTAuthentication authentication;

	
	public void onModuleLoad() {
		addAuthenticationListener(this);
	
	}

	/**
	 * Ask for Granted entities and fire {@link GWTSecurityModule#fireAuthenticationPerformed(GWTAuthentication)}
	 * @param loginURL
	 */
	public static void initOnReload(String loginURL) {
		final GWTAuthServiceAsync authServiceAsync = GWT
				.create(GWTAuthService.class);
		ServiceDefTarget serviceDefTarget = (ServiceDefTarget) authServiceAsync;
		
		 
		
		serviceDefTarget.setServiceEntryPoint(GWT.getModuleBaseURL()
				+ loginURL);
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
}
