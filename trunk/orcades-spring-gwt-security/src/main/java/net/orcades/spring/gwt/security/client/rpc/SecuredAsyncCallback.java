package net.orcades.spring.gwt.security.client.rpc;

import net.orcades.spring.gwt.security.client.GWTSecurityException;
import net.orcades.spring.gwt.security.client.IGWTSecurityExceptionVisitor;
import net.orcades.spring.gwt.security.client.ui.GWTSecurityExceptionVisitor;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
/**
 * Generic callback to handle security exception.
 * @author Olivier NOUGUIER
 *
 * @param <T>
 */
public class SecuredAsyncCallback<T> implements AsyncCallback<T> {


	/**
	 * Exception Visitor. 
	 */
	private IGWTSecurityExceptionVisitor securityExceptionVisitor;
	
	/**
	 * Constructor.
	 * @param clickListener to call on successful authentication, may be null. 
	 */
	public SecuredAsyncCallback(ClickListener clickListener) {
		securityExceptionVisitor = new GWTSecurityExceptionVisitor(clickListener);
	}

	public void onFailure(Throwable throwable) {
		
		if (throwable instanceof GWTSecurityException) {
			GWTSecurityException securityException = (GWTSecurityException)throwable ;
			securityException.accept(securityExceptionVisitor);
		}else {
			Log.error(throwable.getMessage(), throwable);
		}
		
	}

	public void onSuccess(T object) {
		
	}

}
