package net.orcades.spring.gwt.security.client.ui;

import net.orcades.spring.gwt.security.client.GWTAuthService;
import net.orcades.spring.gwt.security.client.GWTAuthServiceAsync;
import net.orcades.spring.gwt.security.client.GWTAuthentication;
import net.orcades.spring.gwt.security.client.GWTAuthenticationFailedException;
import net.orcades.spring.gwt.security.client.GWTSecurityModule;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author olivier nouguier olivier@orcades.net Simple login panel. On
 *         successful login, the panel will (re)invoke the click that opened the
 *         login box (same behavior of webapp login form).
 * 
 */
public class LoginPanel extends PopupPanel {

	/**
	 * Top container.
	 */
	private FlexTable table = new FlexTable();
	/**
	 * Login entry box.
	 */
	private TextBox loginTextBox;
	/**
	 * Password entry box.
	 */
	private PasswordTextBox passwordTextBox;
	private Label logMessage;

	/**
	 * Constructor.
	 * 
	 * @param authServiceEndPoint
	 *            authentication end point.
	 * @param message
	 *            Message to show in header.
	 * @param clickListener
	 *            to call on successful authentication, may be null.
	 */
	public LoginPanel(final String authServiceEndPoint, String message,
			final ClickListener clickListener) {
		setWidget(table);
		table.setWidget(0, 0, new Label(message));
		table.getFlexCellFormatter().setColSpan(0, 0, 2);
		table.setWidget(1, 0, new Label("Login"));
		table.setWidget(1, 1, loginTextBox = new TextBox());

		table.setWidget(2, 0, new Label("Password"));
		table.setWidget(2, 1, passwordTextBox = new PasswordTextBox());

		table.setWidget(3, 0, new PushButton("Cancel", new ClickListener() {

			public void onClick(Widget widget) {
				LoginPanel.this.hide();

			}

		}));
		table.setWidget(3, 1, new PushButton("submit", new ClickListener() {

			public void onClick(Widget sender) {
				if (StringUtils.isEmptyOrBlank(loginTextBox.getText())
						|| StringUtils
								.isEmptyOrBlank(passwordTextBox.getText())) {
					logMessage.setText("Login AND password must be provided");
					return;
				}
				GWTAuthServiceAsync authService = GWT
						.create(GWTAuthService.class);
				ServiceDefTarget serviceDefTarget = (ServiceDefTarget) authService;
				serviceDefTarget.setServiceEntryPoint(GWT.getModuleBaseURL()
						+ authServiceEndPoint);
				authService.autenticate(loginTextBox.getText(), passwordTextBox
						.getText(), new AsyncCallback<GWTAuthentication>() {

					public void onFailure(Throwable caught) {
						if (caught instanceof GWTAuthenticationFailedException) {
							logMessage.setText(caught.getMessage());
							Log.info(caught.getMessage());
							
						}else {
						    logMessage.setText("Error on login");
						    Log.error("Error on login", caught);
						}
						
					}

					public void onSuccess(GWTAuthentication authentication) {
						Log.debug(authentication.toString());
						GWTSecurityModule
								.fireAuthenticationPerformed(authentication);
						logMessage.setText("success");
						LoginPanel.this.hide();
						if (clickListener != null) {
							clickListener.onClick(null);
						}

					}

				});
			}

		}));
		table.setWidget(4, 0, logMessage = new Label());
		table.getFlexCellFormatter().setColSpan(4, 0, 2);

		// loginTextBox.setText("adm");
		// passwordTextBox.setText("adm");
	}

}
