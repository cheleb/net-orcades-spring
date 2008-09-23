package springsample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SampleModule implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get().add(new Button("sayHelo", new ClickListener() {

			public void onClick(Widget widget) {
				ISampleService.Util.getInstance().sayHelo("olivier",
						new AsyncCallback<String>() {

							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub

							}

							public void onSuccess(String response) {
								RootPanel.get().add(new Label(response));

							}

						});

			}

		}));
	}

}
