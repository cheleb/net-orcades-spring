package net.orcades.spring.gwt.security.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTAuthServiceAsync  {
	
	void autenticate(String login, String password, AsyncCallback<Boolean> result);

}
