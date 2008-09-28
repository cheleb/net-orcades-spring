package net.orcades.spring.gwt.security.client.rpc;

import net.orcades.spring.gwt.security.client.GWTAuthorizationRequiredException;
import net.orcades.spring.gwt.security.client.ui.LoginPanel;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;

public class SecuredAsyncCallback<T> implements AsyncCallback<T> {

	private ClickListener clickListener;

	public SecuredAsyncCallback(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public void onFailure(Throwable arg0) {
		if (arg0 instanceof GWTAuthorizationRequiredException) {
			GWTAuthorizationRequiredException authException = (GWTAuthorizationRequiredException) arg0;
			
			LoginPanel loginPanel = new LoginPanel(authException.getAuthServiceEndPoint(), authException.getMessage(), clickListener);
			loginPanel.center();
			loginPanel.show();
		}
		
	}

	public void onSuccess(T object) {
		// TODO Auto-generated method stub
		
	}

}
