package springsample.client.ui;

import java.util.List;

import net.orcades.spring.gwt.security.client.GWTSecurityModule;
import springsample.client.ErrorAwareAsyncCallback;
import springsample.client.ISampleService;
import springsample.client.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BoardPanel extends VerticalPanel {

	FlexTable flexTable;

	public BoardPanel() {
		setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		HorizontalPanel panel = new HorizontalPanel();
		
		panel.add(new PushButton(
				new Image(GWT.getModuleBaseURL() + "img/refresh.gif"),
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						initMessageTable(true);
					}
				}));

		
		add(panel);
		initMessageTable(false);
	}

	private FlexTable initMessageTable(final boolean applyAuthentication) {

		ISampleService.Util.getInstance().getMessages(
				new ErrorAwareAsyncCallback<List<Message>>() {
					@Override
					public void onSuccess(List<Message> messageList) {
						if (flexTable != null) {
							remove(flexTable);
						}
						flexTable = new FlexTable();
						flexTable.setStyleName("board");
						add(flexTable);

						flexTable.setWidget(0, 0, new Label("Owner"));
						flexTable.setWidget(0, 1, new Label("Message"));
						flexTable.setWidget(0, 2, new Label("Status"));
						flexTable.setWidget(0, 3, new Label("Actions"));
						for (Message message : messageList) {
							ActionPanel actionPanel = addMessage(message);
							if (applyAuthentication) {
								GWTSecurityModule
										.applyAuthentication(actionPanel);
							}
						}

					}
				});

		return flexTable;
	}

	public ActionPanel addMessage(Message message) {
		int row = flexTable.getRowCount();
		flexTable.setWidget(row, 0, new Label(message.getOwner().getLogin()));
		flexTable.setWidget(row, 1, new Label(message.getText()));
		flexTable.setWidget(row, 2, new Label(message.getStatus()));
		ActionPanel actionPanel = new ActionPanel(message, flexTable, flexTable
				.getElement().getLastChild());

		flexTable.setWidget(row, 3, actionPanel);
		GWTSecurityModule.addAuthenticationListener(actionPanel);

		return actionPanel;
	}
}
