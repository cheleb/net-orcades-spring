/*
 * Copyright 2007 Aditya Kapur <addy AT gwtiger.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.orcades.gwt.mvc.client.ui;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

public class TextFieldWidget extends BaseFieldLabelWidget {
    protected TextBox textBox = new TextBox();

    private boolean convertToUpper = true;

    public TextFieldWidget(String label) {
	super(label);
	setTextBoxBase(textBox);
	this.addField(textBox);
    }

    protected void setTextBoxBase(TextBoxBase textBoxBase) {
	textBoxBase = textBox;
    }

    public TextFieldWidget(String labelText, String width) {
	this(labelText);
	textBox.setWidth(width);
    }

    public void setMaxLength(int length) {
	textBox.setMaxLength(length);
    }

    public String getText() {
	String str = textBox.getText();
	if ((str != null) && (convertToUpper))
	    str = str.toUpperCase();
	return str;
    }

    public void clear() {
	setText("");
	showError(false);
    }

    public void setText(String text) {
	if (text != null)
	    textBox.setText(text);
    }

    public void setWidth(String width) {
	textBox.setWidth(width);
    }

    public String getNullText() {
	if (textBox.getText().length() == 0)
	    return null;
	else
	    return getText();
    }

    public void setConvertToUpper(boolean flag) {
	convertToUpper = flag;
    }

    public boolean validate() {
	if (this.isRequired() && textBox.getText().length() == 0) {
	    showError("boum2", true);
	    return false;
	} else {
	    showError(false);
	    return true;
	}

    }

    public void addKeyboardListener(KeyboardListener listener) {
	textBox.addKeyboardListener(listener);
    }

    public void addClickListener(ClickListener listener) {
	textBox.addClickListener(listener);
    }

    public void addFocusListener(FocusListener listener) {
	textBox.addFocusListener(listener);
    }

    public void setEnabled(boolean enabled) {
	textBox.setEnabled(enabled);
    }
    
    public void addChangeListener(ChangeListener listener)  {
        textBox.addChangeListener(listener);
    }
    
}