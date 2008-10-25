package springsample.client;

import java.util.List;

import net.orcades.spring.gwt.security.client.GWTSecurityException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
/**
 * Sample service.
 * @author Nouguier Olivier olivier@orcades.net olivier.nouguier@gmail.com
 *
 */
@RemoteServiceRelativePath("unsecure.gwt")
public interface ISampleService extends RemoteService {

	public static class Util {

		public static ISampleServiceAsync getInstance() {

			return GWT.create(ISampleService.class);
		}
	}

	
	public String sayHelo(String who) throws GWTSecurityException;
	
	public List<Message> getMessages();
}
