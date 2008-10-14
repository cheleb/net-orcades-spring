package springsample.client.converter;

import net.orcades.gwt.mvc.client.model.BaseModel;

public class ConverterModel extends BaseModel {
	private final static float EUR_HFL = 2.20371F;

    private float amountHfl = 0;
    private float amountEur = 0;

    public float getAmountHfl() {
        return amountHfl;
    }

    public float getAmountEur() {
        return amountEur;
    }

    public void updateAmountHfl(float amountHfl) {
        this.amountHfl = amountHfl;
        this.amountEur = amountHfl/EUR_HFL;
        fireModelChangedEvent();
    }

    public void updateAmountEur(float amountEur) {
        this.amountEur = amountEur;
        this.amountHfl = amountEur * EUR_HFL;
        fireModelChangedEvent();
    }

}
