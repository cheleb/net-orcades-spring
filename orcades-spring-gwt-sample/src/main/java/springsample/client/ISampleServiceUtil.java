package springsample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Sample service.
 * 
 * @author Nouguier Olivier olivier@orcades.net olivier.nouguier@gmail.com
 * 
 */

public class ISampleServiceUtil {

	public static ISampleServiceAsync getInstance() {

		return GWT.create(ISampleService.class);

	}

	public static ISampleServiceAsync getInstance(String bean) {
		ISampleServiceAsync async = GWT.create(ISampleService.class);
		((ServiceDefTarget) async).setServiceEntryPoint(bean + "-"
				+ ISampleService.URI);
		return async;
	}
}
