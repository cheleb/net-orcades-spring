package net.orcades.spring.gwt.security.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GWTAuthentication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> grantedAuthorities = new ArrayList<String>();

	private String logoutServiceEndPoint;

	private String login;
	
	public GWTAuthentication() {

	}

	public GWTAuthentication(String login, List<String> grantedAuthorities,
			String logoutServiceEndPoint) {
		this.login = login;
		this.grantedAuthorities = grantedAuthorities;
		this.logoutServiceEndPoint = logoutServiceEndPoint;
	}

	
	
	public String getLogin() {
		return login;
	}

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

	public boolean userInRole(String role) {

		return grantedAuthorities.contains(role);
	}

	

}
