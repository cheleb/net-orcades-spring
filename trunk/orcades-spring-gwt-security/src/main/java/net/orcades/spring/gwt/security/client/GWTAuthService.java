package net.orcades.spring.gwt.security.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTAuthService extends RemoteService {
	
	GWTAuthentication autenticate(String login, String password);

}
