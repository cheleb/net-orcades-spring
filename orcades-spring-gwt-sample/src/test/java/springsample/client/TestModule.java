package springsample.client;

import org.junit.Ignore;

import springsample.client.converter.ConverterView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

@Ignore
public class TestModule implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get().add(new ConverterView());

	}

}
