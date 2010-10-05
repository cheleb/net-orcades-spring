package springsample.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Generic callback to handle security exception.
 * 
 * @author Olivier NOUGUIER
 * 
 * @param <T>
 */
public class ErrorAwareAsyncCallback<T> implements AsyncCallback<T> {

	public void onFailure(Throwable throwable) {

		Log.error(throwable.getMessage(), throwable);

	}

	public void onSuccess(T object) {

	}

}
