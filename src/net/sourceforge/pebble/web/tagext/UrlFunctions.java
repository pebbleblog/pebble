package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.util.StringUtils;
/**
 * Used from jsp taglib url.tld 
 * @author Olaf Kock
 * @see UrlRewriter
 */
public class UrlFunctions {
	/**
	 * Escape HTML special characters
	 * @param url
	 * @return escaped parameter
	 */
	public static String escape(String url) {
		return StringUtils.transformHTML(url); 
	};
}
