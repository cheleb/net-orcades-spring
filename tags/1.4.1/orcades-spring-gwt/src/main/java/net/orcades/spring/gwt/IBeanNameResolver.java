package net.orcades.spring.gwt;

/**
 * Interface to define the (Spring) BeanName resolution from an incoming URI.
 * @author olivier.nouguier@gmail.com
 *
 */
public interface IBeanNameResolver {
	/**
	 * Resolve a bean name from an URI.
	 * @param relativeURI received
	 * @return the spring bean name (aka id) that will handle this request.
	 * @throws BeanNameResolutionException
	 */
	String resolve(String relativeURI) throws BeanNameResolutionException;
}
