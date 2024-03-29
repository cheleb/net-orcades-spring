package springsample.client;

import net.orcades.spring.gwt.security.client.GWTAuthentication;
import net.orcades.spring.gwt.security.client.GWTAuthenticationListener;
import net.orcades.spring.gwt.security.client.GWTSecurityModule;
import net.orcades.spring.gwt.security.client.rpc.SecuredAsyncCallback;
import net.orcades.spring.gwt.security.client.ui.SecuredPushButton;
import springsample.client.admin.IAdminInfoService;
import springsample.client.ui.ActionPanel;
import springsample.client.ui.BoardPanel;
import springsample.client.user.IUserInfoService;
import springsample.client.user.UserInfoDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class SampleModule implements EntryPoint, GWTAuthenticationListener {

	private BoardPanel boardPanel;

	public void onModuleLoad() {
		//
		// Try to load granted entity (if user is already logued in 
		//
		GWTSecurityModule.initOnReload("security-auth2.gwt");
		//
		// Put handler for uncaught error.
		//
		Log.setUncaughtExceptionHandler();

		final RootPanel messagePanel = RootPanel.get("board");
		// messagePanel.add(new Label("ooooooooo******ooooooooooo"));
		boardPanel = new BoardPanel();
		messagePanel.add(boardPanel);

		GWTSecurityModule.addAuthenticationListener(this);

		// RootPanel.get("converter").add(new ConverterView());

		HorizontalPanel loginPanel = new HorizontalPanel();
		loginPanel.setSpacing(10);
		RootPanel.get("login-bar").add(loginPanel);

		

		loginPanel
				.add(new HTML(
						"Click to login ==============><br /> (disabled is user in role \"USER\")"));
		loginPanel.add(new SecuredPushButton(new Image(GWT.getModuleBaseURL()
				+ "img/login-usr.png"), "User login", new ClickHandler() {

			public void onClick(ClickEvent clickEvent) {
				IUserInfoService.Util.getInstance().showUserInfo(
						new SecuredAsyncCallback<UserInfoDTO>() {

							public void onSuccess(UserInfoDTO userInfoDTO) {
								Log.info("User logued: "
										+ userInfoDTO.toString());
							}

							@Override
							protected void doOnFailure(Throwable throwable) {
								Log.warn("Error occurs", throwable);

							}

						});

			}

		}, "!USER"));

		loginPanel
				.add(new HTML(
						"Click to login ==========><br />(disabled if user in role \"ADMIN\")"));
		loginPanel.add(new SecuredPushButton(new Image(GWT.getModuleBaseURL()
				+ "img/login-adm.png"), "Admin login", new ClickHandler() {

			public void onClick(ClickEvent clickEvent) {
				IAdminInfoService.Util.getInstance().showUserInfo("guest",
						new SecuredAsyncCallback<UserInfoDTO>(this) {

							public void onSuccess(UserInfoDTO userInfoDTO) {
								Log
										.info("User info: "
												+ userInfoDTO.toString());

							}

						});

			}

		}, "!ADMIN"));

		loginPanel
				.add(new HTML(
						"Click to logout ==========><br />(disabled if user <b>not</b> in role \"USER\")"));
		loginPanel.add(new SecuredPushButton(new Image(GWT.getModuleBaseURL()
				+ "img/logout.png"), "Logout", new ClickHandler() {

			public void onClick(ClickEvent event) {

				GWTSecurityModule.logout();

			}

		}, "USER"));

		RootPanel buggyRootPanel = RootPanel.get("buggy");

		if (buggyRootPanel != null) {
			final ListBox listBox = new ListBox();
			buggyRootPanel.add(listBox);

			listBox.addItem("Buggy sampleService", "sampleService");
			listBox.addItem("Fixed sampleService", "sampleService2");

			buggyRootPanel.add(new Button("go", new ClickHandler() {
				public void onClick(ClickEvent event) {
					ISampleServiceUtil.getInstance(
							listBox.getValue(listBox.getSelectedIndex()))
							.buggy(new ErrorAwareAsyncCallback<Void>());
				}

			}));
		}

	}

	public void authenticated(GWTAuthentication authentication) {
		RootPanel panel = RootPanel.get("entry-box");
		HorizontalPanel entryBoxPanel = new HorizontalPanel();

		if (authentication.userInRole("USER")) {

			if (panel.getWidgetCount() == 0) {
				panel.add(entryBoxPanel);
				entryBoxPanel
						.add(new Label("Type some text and press ENTER: "));
				final TextBox textBox;
				entryBoxPanel.add(textBox = new TextBox());
				textBox.addKeyDownHandler(new KeyDownHandler() {
					
					public void onKeyDown(KeyDownEvent event) {
						char keyCode = (char) event.getNativeKeyCode();
						if (keyCode == KeyCodes.KEY_ENTER) {
							IUserInfoService.Util.getInstance().newMessage(
									textBox.getText(),
									new ErrorAwareAsyncCallback<Message>() {
										@Override
										public void onSuccess(Message message) {
											ActionPanel actionPanel = boardPanel
													.addMessage(message);
											GWTSecurityModule
													.applyAuthentication(actionPanel);
										}
									});
						}

					}


				});
			}
		} else {
			if (panel.getWidgetCount() > 0) {
				panel.remove(0);
			}
		}

	}

}
