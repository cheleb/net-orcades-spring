package net.orcades.spring.gwt.security.client;

public class GWTAccessDeniedException extends GWTSecurityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Deprecated
	public GWTAccessDeniedException() {
	}
	
	public GWTAccessDeniedException(String authURL, String message) {
		super(authURL, message);
	}



	public void accept(IGWTSecurityExceptionVisitor visitor) {
		visitor.visit(this);
		
	}

}
