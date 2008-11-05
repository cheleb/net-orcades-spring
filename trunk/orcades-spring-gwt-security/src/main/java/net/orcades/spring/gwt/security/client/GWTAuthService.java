package net.orcades.spring.gwt.security.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Authentication service.
 * @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 * 
 */
public interface GWTAuthService extends RemoteService {

	GWTAuthentication autenticate(String login, String password) throws GWTAuthenticationFailedException;

}
