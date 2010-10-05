package net.orcades.spring.gwt.security.client;

/**
 * Exception thrown on Access denied.
 * 
 * @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 *
 */
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


	/**
	 * Visitor accept method.
	 */
	public void accept(IGWTSecurityExceptionVisitor visitor) {
		visitor.visit(this);
		
	}

}
