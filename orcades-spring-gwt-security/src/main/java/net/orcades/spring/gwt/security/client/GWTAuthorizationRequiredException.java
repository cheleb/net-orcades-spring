package net.orcades.spring.gwt.security.client;

public class GWTAuthorizationRequiredException extends Exception {

	private String authServiceEndPoint;

	@Deprecated
	public GWTAuthorizationRequiredException() {
	}
	
	public GWTAuthorizationRequiredException(String authURL, String message) {
		super(message);
		this.authServiceEndPoint = authURL;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getAuthServiceEndPoint() {
		return authServiceEndPoint;
	}

	public void setAuthServiceEndPoint(String authServiceEndPoint) {
		this.authServiceEndPoint = authServiceEndPoint;
	}

}
