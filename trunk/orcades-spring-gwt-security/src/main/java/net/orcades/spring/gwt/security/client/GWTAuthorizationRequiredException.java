package net.orcades.spring.gwt.security.client;

public class GWTAuthorizationRequiredException extends GWTSecurityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GWTAuthentication authentication;
	
	@Deprecated
	public GWTAuthorizationRequiredException() {
	}
	
	public GWTAuthorizationRequiredException(String authURL, String message) {
		super(authURL, message);

	}

	

	public void accept(IGWTSecurityExceptionVisitor visitor) {
		visitor.visit(this);
		
	}

	public GWTAuthentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(GWTAuthentication authentication) {
		this.authentication = authentication;
	}

}
