package springsample.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISampleServiceAsync {

	public void sayHelo(String who, AsyncCallback<String> callback);
}
