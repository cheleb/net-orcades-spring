package net.orcades.spring.gwt.security.client.ui;

/**
 *  Famous StringUtil.
 *  @author NOUGUIER Olivier olivier@orcades.net, olivier.nouguier@gmail.com
 * 
 *
 */
public class StringUtils {

	/**
	 * Determines if a string is empty (or null).
	 * @param text
	 * @return true is null or blank string.
	 */
	public static boolean isEmptyOrBlank(String text) {
		if(text==null) {
			return true;
		}
		return "".equals(text.trim());
	}
	
}
