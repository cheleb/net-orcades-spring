package springsample.client.converter;

import net.orcades.gwt.mvc.client.ui.TextBoxWidget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConverterView extends Composite {

	private TextBoxWidget input1 = new TextBoxWidget();
    private TextBoxWidget input2 = new TextBoxWidget();

    public ConverterView() {
        // Layout
        VerticalPanel panel = new VerticalPanel();

        HorizontalPanel row1 = new HorizontalPanel();
        Label label1 = new Label("HFL");
        row1.add(input1);
        row1.add(label1);
        panel.add(row1);

        HorizontalPanel row2 = new HorizontalPanel();
        Label label2 = new Label("EUR");
        row2.add(input2);
        row2.add(label2);
        panel.add(row2);

        initWidget(panel);

        // Model
        ConverterModel model = new ConverterModel();
     //   model.addModelChangeListener(this);

        // Controller
        ConverterController controller = new ConverterController(model);
        controller.registerHflInput(input1);
        controller.registerEurInput(input2);

        // Initialize view to initial state of model
        model.fireModelChangedEvent();
    }

   

}
