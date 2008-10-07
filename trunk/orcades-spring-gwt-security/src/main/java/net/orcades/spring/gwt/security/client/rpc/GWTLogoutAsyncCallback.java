package net.orcades.spring.gwt.security.client.rpc;

import net.orcades.spring.gwt.security.client.GWTSecurityModule;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GWTLogoutAsyncCallback implements AsyncCallback<Boolean>{

	public void onFailure(Throwable throwable) {
		Log.error(throwable.getMessage(), throwable);

	}

	public void onSuccess(Boolean arg0) {
		Log.debug("Logout");
		GWTSecurityModule.fireLogout();
	}


}