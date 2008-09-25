package net.orcades.spring.gwt.security.client.ui;

import net.orcades.spring.gwt.security.client.GWTAuthService;
import net.orcades.spring.gwt.security.client.GWTAuthServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginPanel extends PopupPanel {

	private FlexTable table = new FlexTable();
	private TextBox loginTextBox;
	private PasswordTextBox passwordTextBox;
	
	public LoginPanel(String message, final ClickListener clickListener) {
		setWidget(table);
		table.setWidget(0, 0, new Label(message));
		table.getFlexCellFormatter().setColSpan(0, 0, 2);
		table.setWidget(1, 0, new Label("Login"));
		table.setWidget(1, 1, loginTextBox=new TextBox());
		table.setWidget(2, 0, new Label("Password"));
		table.setWidget(2, 1, passwordTextBox=new PasswordTextBox());
		table.setWidget(3, 0, new PushButton("Cancel", new ClickListener() {

			public void onClick(Widget widget) {
				LoginPanel.this.hide();
				
			}
			
		}));
		table.setWidget(3, 1, new PushButton("submit", new ClickListener() {

			public void onClick(Widget sender) {
				GWTAuthServiceAsync authService = GWT.create(GWTAuthService.class);
				ServiceDefTarget serviceDefTarget = (ServiceDefTarget) authService;
				serviceDefTarget.setServiceEntryPoint(GWT.getModuleBaseURL()+"security-auth.gwt");
				authService.autenticate(loginTextBox.getText(), passwordTextBox.getText(), new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(Boolean result) {
						//RootPanel.get().add(new Label("Auth " + (result.booleanValue()?"successful":"failed")));
						LoginPanel.this.hide();
						clickListener.onClick(null);
					}
					
				});
			}
			
		}));
		
	}
	
}
