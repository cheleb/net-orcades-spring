package springsample.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISampleServiceAsync {

	public void sayHelo(String who, AsyncCallback<String> callback);
	
	public void getMessages(AsyncCallback<List<Message>> callback);
}
