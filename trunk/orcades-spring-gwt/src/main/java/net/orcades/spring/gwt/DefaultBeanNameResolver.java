package net.orcades.spring.gwt;

/**
 * Default resolution for bean.
 * <code>
 * /myContext/myModule/myBeanName-gwtEndPoint.gwt ====> myBeanName.
 * </code>
 * @author olivier.nouguier@gmail.com
 *
 */
public class DefaultBeanNameResolver implements IBeanNameResolver {

	/**
	 * Resolve a bean name from an URI.
	 * @param relativeURI received
	 * @return the spring bean name (aka id) that will handle this request.
	 * @throws BeanNameResolutionException
	 */
	public String resolve(String relativeURI) throws BeanNameResolutionException {
		if(relativeURI.indexOf('-') == -1) {
			throw new BeanNameResolutionException("Unable to resolve bean name (beanName-service.gwt) from URI: " + relativeURI + " you shoul provide anothere implemenation of IBeanNameResolver");
		}
		return relativeURI.substring(0, relativeURI.indexOf('-'));
	}

}
