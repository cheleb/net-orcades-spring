package net.orcades.gwt.mvc.client.ui;

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




import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

abstract public class BaseFieldLabelWidget extends Composite implements
	Validate {
    private HorizontalPanel panel = new HorizontalPanel();

//    private HorizontalPanel tbPanel = new HorizontalPanel();
    private FlexTable tbPanel = new FlexTable();
    private HorizontalPanel alertPanel = new HorizontalPanel();

    protected Widget baseField;

    private Label label = new Label();

    private Label lblReq = new Label("*");

    private static final String STYLE_ERROR = "gwtiger-form-error";
    public static final String STYLE_INPUT="gwtiger-form-input";
    public static final String STYLE_LABEL="gwtiger-form-label";
    
    private SimplePanel errorIconPanel=new SimplePanel();

    private Label errorMsg = new Label("boum");

    private boolean isRequired = false;

    public BaseFieldLabelWidget(String labelText) {
        lblReq.setVisible(false);
        errorMsg.setVisible(false);
        errorMsg.setStyleName(STYLE_ERROR);
	
        label.setText(labelText);
	
        label.setStyleName(STYLE_LABEL);
//	tbPanel.add(label);
    
        tbPanel.setWidget(0, 0, label);
        
    	alertPanel.add(tbPanel);// Label and the field
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    	panel.add(alertPanel);
    	
    	panel.add(lblReq); //required field marker
//        errorIconPanel.add(errorIcon.createImage());
        errorIconPanel.setVisible(false);
        panel.add(errorIconPanel);
    	panel.add(errorMsg);// Error message
    
    	// All composites must call initWidget() in their constructors.
    	initWidget(panel);
    }

   
    
    abstract public boolean validate();

    abstract public void clear();// remove all values

       
    protected void addField(Widget field) {
	baseField = field;
	field.setStyleName(STYLE_INPUT);
    tbPanel.setWidget(0, 1, field);
//	tbPanel.add(field);
    }

    public String getLabel() {
	return label.getText();
    }

    public void setLabel(String labelText) {
	label.setText(labelText);

    }

    public void showError(boolean visible) {
    	errorMsg.setVisible(visible);
        errorIconPanel.setVisible(visible);
        alertPanel.setBorderWidth(visible?2:0);
    }
    public void showError(String errorMessage, boolean visible) {
	errorMsg.setText(errorMessage);
	showError(visible);
    }

    public void showError(String errorMessage) {
	showError(errorMessage, true);
    }

    public void setRequired(boolean required) {
	this.isRequired = required;
	lblReq.setVisible(required);
    }
    
    public void setLabelStyleName(String style) {
        label.setStyleName(style);
    }
    public String getLabelStyleName()   {
        return label.getStyleName();
    }
    
    /**
     * This method returns the panel that encloses the Label and the Widget
     * @return Panel
     */
  /*  public HorizontalPanel getPanel()   {
        return tbPanel;
    }*/
    
    public void setWidget(int row,int column, Widget widget)   {
        tbPanel.setWidget(row, column, widget);
    }
    
    public FlexTable getPanel() {
        return tbPanel;
    }
    
    public void setErrorStyleName(String style) {
        errorMsg.setStyleName(style);
    }
    public String getErrorStyleName()   {
        return errorMsg.getStyleName();
    }
    
    public void setStyleName(String style) {
        baseField.setStyleName(style);
    }
    public String getStyleName()   {
        return baseField.getStyleName();
    }

    public boolean isRequired() {
	return isRequired;
    }

}
