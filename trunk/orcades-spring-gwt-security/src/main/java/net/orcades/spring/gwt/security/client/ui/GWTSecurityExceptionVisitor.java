package net.orcades.spring.gwt.security.client.ui;
/**
 *  @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 */
import net.orcades.spring.gwt.security.client.GWTAccessDeniedException;
import net.orcades.spring.gwt.security.client.GWTSecurityException;
import net.orcades.spring.gwt.security.client.IGWTSecurityExceptionVisitor;

import com.google.gwt.user.client.ui.ClickListener;

public class GWTSecurityExceptionVisitor implements
		IGWTSecurityExceptionVisitor {

	private ClickListener clickListener;

	public GWTSecurityExceptionVisitor(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public void visit(
			GWTSecurityException authorizationRequiredException) {

		LoginPanel loginPanel = new LoginPanel(authorizationRequiredException
				.getAuthServiceEndPoint(), authorizationRequiredException
				.getMessage(), clickListener);
		loginPanel.center();
		loginPanel.show();

	}

	public void visit(GWTAccessDeniedException accessDeniedException) {
		LoginPanel accessDeniedPanel = new LoginPanel(accessDeniedException
				.getAuthServiceEndPoint(), accessDeniedException
				.getMessage(), clickListener);
		accessDeniedPanel.center();
		accessDeniedPanel.show();

	}

}
