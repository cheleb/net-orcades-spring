package springsample.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

@RemoteServiceRelativePath("sample.gwt")
public interface ISampleService extends RemoteService {

	public static class Util {

		public static ISampleServiceAsync getInstance() {

			return GWT.create(ISampleService.class);
		}
	}

	
	public String sayHelo(String who);
}
