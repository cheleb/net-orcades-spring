package net.orcades.spring.gwt.security.client;

import com.google.gwt.user.client.rpc.RemoteService;


public interface GWTLogoutService extends RemoteService {
	Boolean logout();
	
}
