package net.orcades.spring.gwt.security.client;

/**
 * 
 * @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 * 
 * Interface to listen authentication event (login/logout)
 *
 */
public interface GWTAuthenticationListener {

	public void authenticated(GWTAuthentication authentication);

}
