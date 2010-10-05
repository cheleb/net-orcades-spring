package net.orcades.spring.gwt;

/**
 * Exception thrown when not able to parse the URI to find the sprinf bean name.
 * {@link IBeanNameResolver}
 * @author olivier.nouguier@gmail.com
 *
 */
public class BeanNameResolutionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BeanNameResolutionException(String message) {
		super(message);
	}
}
