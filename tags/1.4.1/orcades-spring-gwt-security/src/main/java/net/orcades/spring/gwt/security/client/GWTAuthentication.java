package net.orcades.spring.gwt.security.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 *         Authentication information.
 * 
 */
public class GWTAuthentication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Granted authorities.
	 */
	private List<String> grantedAuthorities = new ArrayList<String>();

	/**
	 * The url to call the logout (injected from spring security xml file).
	 */
	private String logoutServiceEndPoint;

	/**
	 * The login used.
	 */
	private String login;

	/**
	 * Serialization constructor.
	 */
	public GWTAuthentication() {

	}

	/**
	 * Constructor.
	 * 
	 * @param login
	 * @param grantedAuthorities
	 * @param logoutServiceEndPoint
	 */
	public GWTAuthentication(String login, List<String> grantedAuthorities,
			String logoutServiceEndPoint) {
		this.login = login;
		this.grantedAuthorities = grantedAuthorities;
		this.logoutServiceEndPoint = logoutServiceEndPoint;
	}

	/**
	 * Getter for login property.
	 * 
	 * @return login.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Setter from granted authorities.
	 * 
	 * @param gwtGrantedAuthorityList
	 */
	public void setGrantedAuthorities(List<String> gwtGrantedAuthorityList) {

		this.grantedAuthorities = gwtGrantedAuthorityList;

	}

	public List<String> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	public String getLogoutServiceEndPoint() {
		return logoutServiceEndPoint;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("GWTAuthentication[");
		buffer.append(grantedAuthorities);
		buffer.append(']');
		buffer.append("\nLogout end point: ");
		buffer.append(getLogoutServiceEndPoint());
		return buffer.toString();
	}

	/**
	 * Determine if user is in role.
	 * 
	 * @param role
	 * @return true if user is in given role.
	 */
	public boolean userInRole(String role) {

		return grantedAuthorities.contains(role);
	}

}
