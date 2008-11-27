package net.orcades.gwt.mvc.client.ui;

import com.google.gwt.user.client.ui.KeyboardListener;

public interface Validate {
	public boolean validate();

	public void clear();

	public void addKeyboardListener(KeyboardListener listener);
}
