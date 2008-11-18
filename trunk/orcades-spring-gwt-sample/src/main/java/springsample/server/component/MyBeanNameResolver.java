package springsample.server.component;

import org.springframework.stereotype.Component;

import net.orcades.spring.gwt.BeanNameResolutionException;
import net.orcades.spring.gwt.IBeanNameResolver;


public class MyBeanNameResolver implements IBeanNameResolver {

	public String resolve(String relativeURI)
			throws BeanNameResolutionException {
		
		return "neverfind";
	}

}
