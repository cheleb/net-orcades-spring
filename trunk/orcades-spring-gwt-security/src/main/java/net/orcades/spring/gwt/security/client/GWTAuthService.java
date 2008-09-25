package net.orcades.spring.gwt.security.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTAuthService extends RemoteService {
	
	Boolean autenticate(String login, String password);

}
