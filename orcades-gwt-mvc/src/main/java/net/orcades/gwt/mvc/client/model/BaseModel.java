package net.orcades.gwt.mvc.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class BaseModel {
	 private List<ModelChangeListener> listeners = new ArrayList<ModelChangeListener>();

	    public void addModelChangeListener(ModelChangeListener listener) {
	        listeners.add(listener);
	    }

	    public void removeModelChangeListener(ModelChangeListener listener) {
	        listeners.remove(listener);
	    }

	    public void fireModelChangedEvent() {
	        for (Iterator<ModelChangeListener> iListeners = listeners.iterator(); iListeners.hasNext();) {
	            ((ModelChangeListener) iListeners.next()).onModelChanged(this);
	        }
	    }
}
