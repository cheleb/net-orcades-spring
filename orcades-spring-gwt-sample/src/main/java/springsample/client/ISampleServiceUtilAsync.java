package springsample.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
/**
 * Sample service.
 * @author Nouguier Olivier olivier@orcades.net olivier.nouguier@gmail.com
 *
 */
public interface ISampleServiceUtilAsync {

	public void sayHelo(String who, AsyncCallback<String> callback);
	
	public void getMessages(AsyncCallback<List<Message>> callback) ;
	
	public void buggy(AsyncCallback<?> callback);
}
