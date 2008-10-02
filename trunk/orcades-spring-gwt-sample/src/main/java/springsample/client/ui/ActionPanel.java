package springsample.client.ui;

import net.orcades.spring.gwt.security.client.GWTAuthentication;
import net.orcades.spring.gwt.security.client.GWTAuthenticationListener;
import net.orcades.spring.gwt.security.client.rpc.SecuredAsyncCallback;
import springsample.client.Message;
import springsample.client.user.IUserInfoService;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class ActionPanel extends HorizontalPanel implements
		GWTAuthenticationListener {

	private PushButton delete;
	private PushButton moderate;
	private Message message;

	public ActionPanel(final Message message, final FlexTable flexTable,
			final Node child) {

		this.message = message;

		add(delete = new PushButton(new Image(GWT.getModuleBaseURL()
				+ "img/delete.gif"), new ClickListener() {

			public void onClick(Widget sender) {
				IUserInfoService.Util.getInstance().deleteMessage(message,
						new SecuredAsyncCallback<Boolean>(this) {
							@Override
							public void onSuccess(Boolean result) {
								Log.debug("Removal: " + result);
								int row = DOM.getChildIndex(flexTable
										.getElement(), (Element) child);
								flexTable.removeRow(row);
							}
						});

			}

		}));

		add(moderate = new PushButton(new Image(GWT.getModuleBaseURL()
				+ "img/waiting.gif")));
		delete.setEnabled(false);
		moderate.setEnabled(false);
	}

	public void authenticated(GWTAuthentication authentication) {
		boolean m = false, d = false;
		if (authentication != null) {
			if (authentication.userInRole("USER")) {
				m = false;
				d = message.getOwner().getLogin().equals(
						authentication.getLogin());
			}
			if (authentication.userInRole("ADMIN")) {
				m = true;
				d = true;
			}
		}
		moderate.setEnabled(m);
		delete.setEnabled(d);
	}

}
