package net.orcades.spring.gwt.security.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTLogoutServiceAsync {
	void logout(AsyncCallback<Boolean> asyncCallback);
}
