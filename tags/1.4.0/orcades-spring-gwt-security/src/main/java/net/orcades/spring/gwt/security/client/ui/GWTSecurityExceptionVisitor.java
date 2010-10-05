package net.orcades.spring.gwt.security.client.ui;
/**
 *  @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 */
import net.orcades.spring.gwt.security.client.GWTAccessDeniedException;
import net.orcades.spring.gwt.security.client.GWTSecurityException;
import net.orcades.spring.gwt.security.client.IGWTSecurityExceptionVisitor;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickHandler;

public class GWTSecurityExceptionVisitor implements
		IGWTSecurityExceptionVisitor {

	private ClickHandler clickHandler;

	public GWTSecurityExceptionVisitor(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	public void visit(
			GWTSecurityException authorizationRequiredException) {
		Log.info("Auth required");
		LoginPanel loginPanel = new LoginPanel(authorizationRequiredException
				.getAuthServiceEndPoint(), authorizationRequiredException
				.getMessage(), clickHandler);
		loginPanel.center();
		loginPanel.show();

	}

	public void visit(GWTAccessDeniedException accessDeniedException) {
		Log.info("Access denied");
		LoginPanel accessDeniedPanel = new LoginPanel(accessDeniedException
				.getAuthServiceEndPoint(), accessDeniedException
				.getMessage(), clickHandler);
		accessDeniedPanel.center();
		accessDeniedPanel.show();

	}

}
