package net.orcades.spring.gwt.security.client.ui;

import net.orcades.spring.gwt.security.client.GWTAuthentication;
import net.orcades.spring.gwt.security.client.GWTAuthenticationListener;
import net.orcades.spring.gwt.security.client.GWTSecurityModule;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

/**
 *  
 *  @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 *
 */
public class SecuredPushButton extends PushButton implements
		GWTAuthenticationListener {

	private String role;

	private boolean inverse = true;

	public SecuredPushButton(Image off, String title,  ClickHandler clickHandler,
			String role) {
		super(off, clickHandler);
		off.setTitle(title);
		init(role);
	}
	public SecuredPushButton(String string, ClickHandler clickHandler,
			String role) {

		super(string, clickHandler);
		init(role);
	}
	private void init(String role) {
		if (role.startsWith("!")) {
			inverse=true;
			this.role = role.substring(1);
		} else {
			this.inverse = false;
			this.role = role;
		}
		setEnabled(inverse);
		GWTSecurityModule.addAuthenticationListener(this);
	}

	public void authenticated(GWTAuthentication authentication) {
		if (authentication == null) {
			setEnabled(inverse);
		} else {
			
			setEnabled(authentication.userInRole(role)^inverse);
		}

	}

}
