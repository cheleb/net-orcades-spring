package net.orcades.spring.gwt.security.client;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Logout service.
 * @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 *
 */
public interface GWTLogoutService extends RemoteService {

	
	
	
	Boolean logout();
	
}
