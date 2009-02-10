package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.util.UrlRewriter;
/**
 * Used from jsp taglib url.tld 
 * @author Olaf Kock
 * @see UrlRewriter
 */
public class UrlFunctions {
	public static String rewrite(String url) {
		// if you'd like to see the decorated urls highlighted, 
		// a html injection is commented. This wouldn't work 
		// for all cases, but gives an idea where rewriting an
		// url is done (and has succeeded)
		return UrlRewriter.doRewrite(url); // + "\" style=\"background-color:blue";
	};

	public static String escape(String url) {
		return StringUtils.transformHTML(url); 
	};
}
