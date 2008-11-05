package net.orcades.spring.gwt.security.client;

public class GWTAuthenticationFailedException extends Exception {

	/**
	 * Serialization UID.
	 */
	private static final long serialVersionUID = 1L;

	@Deprecated
	public GWTAuthenticationFailedException() {

	}

	public GWTAuthenticationFailedException(String message) {
		super(message);
	}

}
