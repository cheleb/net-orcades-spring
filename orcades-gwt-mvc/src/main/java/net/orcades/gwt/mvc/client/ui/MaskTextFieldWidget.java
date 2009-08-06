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

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

public class MaskTextFieldWidget extends TextFieldWidget {
	private String mask;

	public MaskTextFieldWidget(String labelText, String textMask) {
		super(labelText);
		mask = textMask;
        setMaxLength(mask.length());
        
		textBox.addKeyPressHandler(new KeyPressHandler() {
			
			public void onKeyPress(KeyPressEvent keyPressEvent) {
				Object sender = keyPressEvent.getSource();
				char keyCode = keyPressEvent.getCharCode();
                String text = ((TextBox) sender).getText();
                int pos=((TextBox) sender).getCursorPos();
                int selLength=((TextBox) sender).getSelectionLength();
//                String selText=((TextBox) sender).getSelectedText();
                try {
				
                    if ((keyCode == (char) KeyCodes.KEY_TAB || keyCode == (char)  KeyCodes.KEY_ENTER) || (keyCode == (char)  KeyCodes.KEY_BACKSPACE))
                        return;
                    if(text.length()==mask.length() && selLength==0)    {//reached the end of mask
                        return;
                    }
                    int nextIndex = mask.indexOf('#', pos);
                    if(pos==nextIndex)
                        nextIndex++;
                    String currentMask = ""; 
                    if (nextIndex >= 0)
                        currentMask = mask.substring(pos, nextIndex);
                    else
                        currentMask = mask.substring(pos);
                    
//                    System.out.println("Pos="+pos+" nIdx="+nextIndex+" Maskchar="+currentMask+" char="+keyCode+" text="+text+" mask="+mask+" selLen="+selLength);
                    if(currentMask.equals("#"))    {
                        if(!Character.isDigit(keyCode))
                            ((TextBox) sender).cancelKey();
                    }else  { //if((currentMaskChar!='#')
                        
                        if(currentMask.charAt(0)!=keyCode) {//allow keyCode
                           
                            if(pos==text.length())  { //we are at the end so append
                                ((TextBox) sender).setText(text+currentMask);
                            }else   {
                                String newText="";
                                if(selLength>0) {
                                    newText=text.substring(0, pos)+currentMask+text.substring(pos+selLength);
                                }else   {
                                    newText=text.substring(0, pos)+currentMask+text.substring(pos+1);
                                }
                                
                                ((TextBox) sender).setText(newText);
                                ((TextBox) sender).setCursorPos(pos+currentMask.length());
                            }
                            if(!Character.isDigit(keyCode))
                                ((TextBox) sender).cancelKey();
                                
                        }
                    }
                }catch(Exception e) {
                    System.out.println("Exception for <"+text+"> key="+keyCode+" cursor at "+pos+" mask=<"+mask+">");
                    e.printStackTrace();
                }
                
                
                
                
			/*	int length = text.length();
				if ((keyCode == (char) KEY_BACKSPACE) && length > 0) {
					((TextBox) sender).setText(text.substring(0, length - 1));
				}
				if ((keyCode == (char) KEY_TAB || keyCode == (char) KEY_ENTER))
					return;
				if (length >= mask.length()) {
					((TextBox) sender).cancelKey();
					return;
				}
				int nextIndex = mask.indexOf('#', length);

				String currentMask = "";
				if (nextIndex >= 0)
					currentMask = mask.substring(length, nextIndex);
				else
					currentMask = mask.substring(length);
				String newText;
				if (Character.isDigit(keyCode)) {
					if (!currentMask.equals("#"))
						newText = text + currentMask + String.valueOf(keyCode);
					else
						newText = text + keyCode;
					((TextBox) sender).setText(newText);
					((TextBox) sender).setCursorPos(newText.length());
				}
				((TextBox) sender).cancelKey();*/
			}

            });
	}

	public MaskTextFieldWidget(String labelText, String width, String textMask) {
		this(labelText, textMask);
		textBox.setWidth(width);
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
    public String getMask() {
        return mask;
        
    }

	public String getText() {
		String origtext = super.getText();
		String strip = "";
		try {
			for (int i = 0; i < origtext.length(); i++) {
				if (Character.isDigit(origtext.charAt(i)))
					strip = strip.concat(String.valueOf(origtext.charAt(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strip;
	}
    public String getFormattedText()    {
        return super.getText();
    }

	public void setText(String text) {
		String fmt = "";
		int textcnt = 0;
		if (text == null || text.length() == 0) {
			super.setText(text);
			return;
		}

		try {
			for (int i = 0; i < mask.length(); i++) {
				if (mask.charAt(i) == '#') {
					fmt = fmt.concat(text.substring(textcnt, textcnt + 1));
					textcnt++;
				}else if(mask.charAt(i)==text.charAt(i))    {
				    fmt=fmt+text.charAt(i);
                    textcnt++;
                }
                else
					fmt = fmt.concat(mask.substring(i, i + 1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.setText(fmt);
	}

	public boolean validate() {
		if (super.validate())
			if (super.getText().length() == mask.length()
					|| super.getText().length() == 0) {
				showError(false);
				return true;
			} else {
                showError("boum3",true);
//				showError("'" + getLabel() + "' is not complete", true);
				return false;
			}
		else
			return false;
	}
}
