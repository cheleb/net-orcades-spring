package net.orcades.spring.gwt.security.client.rpc;

import net.orcades.spring.gwt.security.client.GWTSecurityModule;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * GWT Spring Security enabled call back.
 * Fires event ( {@link GWTSecurityModule#fireLogout()} ) on successful logout.
 *  @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 *
 */
public class GWTLogoutAsyncCallback implements AsyncCallback<Boolean>{

	public void onFailure(Throwable throwable) {
		Log.error(throwable.getMessage(), throwable);

	}

	public void onSuccess(Boolean arg0) {
		Log.debug("Logout");
		GWTSecurityModule.fireLogout();
	}


}
