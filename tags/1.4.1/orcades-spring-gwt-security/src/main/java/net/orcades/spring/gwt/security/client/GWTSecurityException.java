package net.orcades.spring.gwt.security.client;

import java.io.Serializable;

public abstract class GWTSecurityException extends Exception implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String authServiceEndPoint;

	@Deprecated
	public GWTSecurityException() {
	}
	
	
	public GWTSecurityException(String authPoint, String message) {
		super(message);
		this.authServiceEndPoint = authPoint;
	}


	public String getAuthServiceEndPoint() {
		return authServiceEndPoint;
	}


	public void setAuthServiceEndPoint(String authServiceEndPoint) {
		this.authServiceEndPoint = authServiceEndPoint;
	}
	
	public abstract void accept(IGWTSecurityExceptionVisitor visitor);	
}
