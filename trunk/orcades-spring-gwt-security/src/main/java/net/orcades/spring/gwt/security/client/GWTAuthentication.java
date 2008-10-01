package net.orcades.spring.gwt.security.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GWTAuthentication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> grantedAuthorities;

	public void setGrantedAuthorities(
			List<String> gwtGrantedAuthorityList) {
		if (this.grantedAuthorities == null) {
			this.grantedAuthorities = gwtGrantedAuthorityList;
		}
	}

	public List<String> getGrantedAuthorities() {
		List<String> list = new ArrayList<String>();
		list.addAll(grantedAuthorities);
		return list;
	}

	
	
	
	@Override
	public String toString() {
	    StringBuffer buffer = new StringBuffer("GWTAuthentication[");
	    buffer.append(grantedAuthorities);
	    buffer.append(']');
		return buffer.toString();
	}

	
}
