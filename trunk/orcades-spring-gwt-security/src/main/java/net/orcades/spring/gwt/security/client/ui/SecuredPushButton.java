package net.orcades.spring.gwt.security.client.ui;

import net.orcades.spring.gwt.security.client.GWTAuthentication;
import net.orcades.spring.gwt.security.client.GWTAuthenticationListener;
import net.orcades.spring.gwt.security.client.GWTSecurityModule;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PushButton;

public class SecuredPushButton extends PushButton implements
		GWTAuthenticationListener {

	private String role;

	public SecuredPushButton(String string, ClickListener clickListener,
			String role) {
		super(string, clickListener);
		this.role = role;
		setEnabled(false);
		GWTSecurityModule.addAuthenticationListener(this);
	}

	public void authenticated(GWTAuthentication authentication) {
		if (authentication == null) {
			setEnabled(false);
		} else {
			setEnabled(authentication.userInRole(role));
		}

	}

}
