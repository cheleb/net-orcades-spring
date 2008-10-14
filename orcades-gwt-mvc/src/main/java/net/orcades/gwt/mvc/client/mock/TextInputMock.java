package net.orcades.gwt.mvc.client.mock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.orcades.gwt.mvc.client.ui.TextInput;

import com.google.gwt.user.client.ui.FocusListener;

public class TextInputMock implements TextInput {

    private List<FocusListener> focusListeners = new ArrayList<FocusListener>();

    private String text;

    public void fireLostFocus() {
        for (Iterator<FocusListener> iFocusListeners = focusListeners.iterator();
        iFocusListeners.hasNext();) {
            ((FocusListener) iFocusListeners.next()).onLostFocus(null);
        }
    }

    public void addFocusListener(FocusListener listener) {
        focusListeners.add(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        focusListeners.remove(listener);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
