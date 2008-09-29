package springsample.client;

import net.orcades.spring.gwt.security.client.GWTSecurityException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("unsecure.gwt")
public interface ISampleService extends RemoteService {

	public static class Util {

		public static ISampleServiceAsync getInstance() {

			return GWT.create(ISampleService.class);
		}
	}

	
	public String sayHelo(String who) throws GWTSecurityException;
}
