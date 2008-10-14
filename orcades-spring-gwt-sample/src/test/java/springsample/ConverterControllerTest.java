package springsample;

import net.orcades.gwt.mvc.client.mock.TextInputMock;
import springsample.client.converter.ConverterController;
import springsample.client.converter.ConverterModel;
import junit.framework.TestCase;

public class ConverterControllerTest extends TestCase {
	 private ConverterController instance;

	    private ConverterModel model;
	    private TextInputMock hflTextInput = new TextInputMock();
	    private TextInputMock eurTextInput = new TextInputMock();

	    protected void setUp() throws Exception {
	        model  = new ConverterModel();
	        instance = new ConverterController(model);
	        instance.registerHflInput(hflTextInput);
	        instance.registerEurInput(eurTextInput);
	        model.fireModelChangedEvent();
	    }

	    /**
	     * Pass condition: changing HFL amount in input results in updated model and view.
	     */
	    public void testChangeAmountHfl() {
	        hflTextInput.setText("2.20371");
	        hflTextInput.fireLostFocus();

	        assertEquals(1.0f, model.getAmountEur());
	        assertEquals("1.0", eurTextInput.getText());
	    }

	    /**
	     * Pass condition: changing EUR amount in input results in updated model and view.
	     */
	    public void testChangeAmountEur() {
	        eurTextInput.setText("1.0");
	        eurTextInput.fireLostFocus();

	        assertEquals(2.20371f, model.getAmountHfl());
	        assertEquals("2.20371", hflTextInput.getText());
	    }
}
