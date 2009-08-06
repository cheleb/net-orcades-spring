package net.orcades.gwt.mvc.client.mock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.orcades.gwt.mvc.client.ui.TextInput;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class TextInputMock implements TextInput {

    private List<BlurHandler> focusListeners = new ArrayList<BlurHandler>();

    private String text;

    public void fireLostFocus() {
        for (Iterator<BlurHandler> iFocusListeners = focusListeners.iterator();
        iFocusListeners.hasNext();) {
            ((BlurHandler) iFocusListeners.next()).onBlur(null);
        }
    }

    public void addFocusListener(BlurHandler listener) {
        focusListeners.add(listener);
    }

    public void removeFocusListener(BlurHandler listener) {
        focusListeners.remove(listener);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

	public HandlerRegistration addFocusHandler(FocusHandler arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void fireEvent(GwtEvent<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addBlurHandler(BlurHandler arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
