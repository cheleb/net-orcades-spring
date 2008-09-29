package springsample.client;

import net.orcades.spring.gwt.security.client.GWTAuthService;
import net.orcades.spring.gwt.security.client.GWTAuthServiceAsync;
import net.orcades.spring.gwt.security.client.GWTLogoutService;
import net.orcades.spring.gwt.security.client.GWTLogoutServiceAsync;
import net.orcades.spring.gwt.security.client.rpc.SecuredAsyncCallback;
import springsample.client.admin.IAdminInfoService;
import springsample.client.user.IUserInfoService;
import springsample.client.user.UserInfoDTO;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class SampleModule implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get("unsecure").add(
				new Button("unsecured", new ClickListener() {

					public void onClick(Widget widget) {
						ISampleService.Util.getInstance().sayHelo("olivier",
								new AsyncCallback<String>() {

									public void onSuccess(String response) {
										RootPanel.get("log").add(
												new Label(response));

									}

									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub

									}

								});

					}

				}));

		RootPanel.get("secure").add(
				new Button("User secured", new ClickListener() {

					public void onClick(Widget widget) {
						IUserInfoService.Util.getInstance().showUserInfo(
								new SecuredAsyncCallback<UserInfoDTO>(this) {

									public void onSuccess(
											UserInfoDTO userInfoDTO) {
										RootPanel.get("log").add(
												new Label("User logued: "
														+ userInfoDTO
																.toString()));

									}

								});

					}

				}));

		RootPanel.get("secure").add(
				new Button("Admin secured", new ClickListener() {

					public void onClick(Widget widget) {
						IAdminInfoService.Util.getInstance().showUserInfo(
								"guest",
								new SecuredAsyncCallback<UserInfoDTO>(this) {

									public void onSuccess(
											UserInfoDTO userInfoDTO) {
										RootPanel.get("log").add(
												new Label("User info: "
														+ userInfoDTO
																.toString()));

									}

								});

					}

				}));

		RootPanel.get().add(new Button("logout", new ClickListener() {

			public void onClick(Widget arg0) {
				GWTLogoutServiceAsync logoutService = GWT
						.create(GWTLogoutService.class);
				ServiceDefTarget serviceDefTarget = (ServiceDefTarget) logoutService;
				serviceDefTarget.setServiceEntryPoint(GWT.getModuleBaseURL()
						+ "logout.gwt");
				
				logoutService.logout(new AsyncCallback<Boolean>() {

					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(Boolean arg0) {
						RootPanel.get().add(new Label("Logout"));
						
					}
					
				});

			}

		}));

	}

}
