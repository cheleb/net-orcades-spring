package springsample.client;

import net.orcades.spring.gwt.security.client.rpc.SecuredAsyncCallback;
import springsample.client.user.IUserInfoService;
import springsample.client.user.UserInfoDTO;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
public class SampleModule implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get("unsecure").add(new Button("unsecured", new ClickListener() {

			public void onClick(Widget widget) {
				ISampleService.Util.getInstance().sayHelo("olivier",
						new AsyncCallback<String>() {

							public void onSuccess(String response) {
								RootPanel.get("log").add(new Label(response));

							}

							public void onFailure(Throwable arg0) {
								// TODO Auto-generated method stub
								
							}

						});

			}

		}));

		RootPanel.get("secure").add(new Button("secured", new ClickListener() {

			public void onClick(Widget widget) {
				IUserInfoService.Util.getInstance().showUserInfo(new SecuredAsyncCallback<UserInfoDTO>(this) {

							public void onSuccess(UserInfoDTO userInfoDTO) {
								RootPanel.get("log").add(new Label("User logued: " + userInfoDTO.toString()));

							}

						});

			}

		}));

		
	}

}
