package net.orcades.spring.gwt.security.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *  
 *  @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 *
 */
public interface GWTAuthServiceAsync  {
	
	void autenticate(String login, String password, AsyncCallback<GWTAuthentication> result);

}
