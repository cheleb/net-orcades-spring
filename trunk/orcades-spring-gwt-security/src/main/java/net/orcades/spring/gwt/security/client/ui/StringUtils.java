package net.orcades.spring.gwt.security.client.ui;

public class StringUtils {

	public static boolean isEmptyOrBlank(String text) {
		if(text==null) {
			return true;
		}
		return "".equals(text.trim());
	}
	
}
