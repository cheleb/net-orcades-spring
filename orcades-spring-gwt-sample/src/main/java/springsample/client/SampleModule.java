package springsample.client;

import net.orcades.spring.gwt.security.client.GWTAuthentication;
import net.orcades.spring.gwt.security.client.GWTAuthenticationListener;
import net.orcades.spring.gwt.security.client.GWTSecurityModule;
import net.orcades.spring.gwt.security.client.rpc.GWTLogoutAsyncCallback;
import net.orcades.spring.gwt.security.client.rpc.SecuredAsyncCallback;
import net.orcades.spring.gwt.security.client.ui.SecuredPushButton;
import springsample.client.admin.IAdminInfoService;
import springsample.client.ui.ActionPanel;
import springsample.client.ui.BoardPanel;
import springsample.client.user.IUserInfoService;
import springsample.client.user.UserInfoDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SampleModule implements EntryPoint, GWTAuthenticationListener {

	

	private BoardPanel boardPanel;



	public void onModuleLoad() {

		Log.setUncaughtExceptionHandler();

		final RootPanel messagePanel = RootPanel.get("board");

		boardPanel = new BoardPanel();
		messagePanel.add(boardPanel);
		

		GWTSecurityModule.addAuthenticationListener(this);

		
		HorizontalPanel loginPanel = new HorizontalPanel();
		RootPanel.get("login-bar").add(loginPanel);
		
		loginPanel.add(
				new Button("User login", new ClickListener() {

					public void onClick(Widget widget) {
						IUserInfoService.Util.getInstance().showUserInfo(
								new SecuredAsyncCallback<UserInfoDTO>(this) {

									public void onSuccess(
											UserInfoDTO userInfoDTO) {
										Log.info("User logued: "
												+ userInfoDTO.toString());

									}

								});

					}

				}));

		loginPanel.add(
				new Button("Admin Login", new ClickListener() {

					public void onClick(Widget widget) {
						IAdminInfoService.Util.getInstance().showUserInfo(
								"guest",
								new SecuredAsyncCallback<UserInfoDTO>(this) {

									public void onSuccess(
											UserInfoDTO userInfoDTO) {
										Log.info("User info: "
												+ userInfoDTO.toString());

									}

								});

					}

				}));

		
		loginPanel.add(new SecuredPushButton("logout", new ClickListener() {

			public void onClick(Widget widget) {

				GWTSecurityModule.logout(new GWTLogoutAsyncCallback());

			}

		}, "USER"));

	}

	

	public void authenticated(GWTAuthentication authentication) {
		RootPanel panel = RootPanel.get("entry-box");
		HorizontalPanel entryBoxPanel = new HorizontalPanel();
		
		if (authentication.userInRole("USER")) {
			
			if (panel.getWidgetCount() == 0) {
				panel.add(entryBoxPanel);
				entryBoxPanel.add(new Label("Type some text and press ENTER: "));
				final TextBox textBox;
				entryBoxPanel.add(textBox = new TextBox());
				textBox.addKeyboardListener(new KeyboardListener() {

					public void onKeyDown(Widget sender, char keyCode,
							int modifiers) {
						if (keyCode == KEY_ENTER) {
							IUserInfoService.Util.getInstance().newMessage(
									textBox.getText(),
									new ErrorAwareAsyncCallback<Message>() {
										@Override
										public void onSuccess(Message message) {
											ActionPanel actionPanel = boardPanel.addMessage(message);
											GWTSecurityModule.applyAuthentication(actionPanel);
										}
									});
						}

					}

					public void onKeyPress(Widget sender, char keyCode,
							int modifiers) {
						// TODO Auto-generated method stub

					}

					public void onKeyUp(Widget sender, char keyCode,
							int modifiers) {
						// TODO Auto-generated method stub

					}

				});
			}
		} else {
			panel.remove(0);
		}

	}

	

}
