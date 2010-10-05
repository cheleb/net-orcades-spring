package net.orcades.gwt.mvc.client.ui;

import com.google.gwt.event.dom.client.KeyPressHandler;

public interface Validate {
	public boolean validate();

	public void clear();

	public void addKeyboardListener(KeyPressHandler listener);
}
