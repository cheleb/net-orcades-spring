package springsample.client.converter;

import net.orcades.gwt.mvc.client.model.BaseModel;
import net.orcades.gwt.mvc.client.model.ModelChangeListener;
import net.orcades.gwt.mvc.client.ui.TextInput;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;

public class ConverterController {
	private ConverterModel converterModel;

    public ConverterController(ConverterModel converterModel) {
        this.converterModel = converterModel;
    }

    public void registerHflInput(final TextInput hflInput) {
        hflInput.addBlurHandler(new BlurHandler() {
			
			public void onBlur(BlurEvent event) {
				
			
                try {
                    float amountHfl = Float.parseFloat(hflInput.getText());
                    converterModel.updateAmountHfl(amountHfl);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        });
        converterModel.addModelChangeListener(new ModelChangeListener() {
            public void onModelChanged(BaseModel model) {
                hflInput.setText(Float.toString(converterModel.getAmountHfl()));
            }
        });
    }

    public void registerEurInput(final TextInput eurInput) {
        eurInput.addBlurHandler(new BlurHandler() {
			
			public void onBlur(BlurEvent event) {
                try {
                    float amountEur = Float.parseFloat(eurInput.getText());
                    converterModel.updateAmountEur(amountEur);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        });
        converterModel.addModelChangeListener(new ModelChangeListener() {
            public void onModelChanged(BaseModel model) {
                eurInput.setText(Float.toString(converterModel.getAmountEur()));
            }
        });
    }

}
