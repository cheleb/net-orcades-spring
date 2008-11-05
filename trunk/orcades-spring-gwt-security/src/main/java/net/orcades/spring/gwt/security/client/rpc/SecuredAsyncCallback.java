package net.orcades.spring.gwt.security.client.rpc;

import net.orcades.spring.gwt.security.client.GWTSecurityException;
import net.orcades.spring.gwt.security.client.IGWTSecurityExceptionVisitor;
import net.orcades.spring.gwt.security.client.ui.GWTSecurityExceptionVisitor;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;

/**
 * Adapter Generic callback to handle security exception.
 * 
 * @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 * 
 * @param <T>
 */
public class SecuredAsyncCallback<T> implements AsyncCallback<T> {

	/**
	 * Exception Visitor.
	 */
	private IGWTSecurityExceptionVisitor securityExceptionVisitor;

	/**
	 * Constructor. When using this constructor, the request will be lost if the
	 * user is not yet authenticated.
	 * 
	 */
	public SecuredAsyncCallback() {
		this(null);
	}

	/**
	 * Constructor. When using this constructor and if parameter is not null,
	 * the clickListener will be invoked on a successful authentication.
	 * 
	 * @param clickListener
	 *            to call on successful authentication, may be null.
	 */
	public SecuredAsyncCallback(ClickListener clickListener) {
		securityExceptionVisitor = new GWTSecurityExceptionVisitor(
				clickListener);
	}

	/**
	 * Handle the security exception, overload
	 * {@link SecuredAsyncCallback#doOnFailure(Throwable)} to do something on
	 * failure.
	 */
	public final void onFailure(Throwable throwable) {

		if (throwable instanceof GWTSecurityException) {
			GWTSecurityException securityException = (GWTSecurityException) throwable;
			securityException.accept(securityExceptionVisitor);
		} else {
			Log.error(throwable.getMessage(), throwable);
			doOnFailure(throwable);
		}
		

	}

	/**
	 * Called when a failure occurs, after the {@link SecuredAsyncCallback} handle.
	 * @param throwable
	 */
	protected void doOnFailure(Throwable throwable) {

	}

	public void onSuccess(T object) {

	}

}
