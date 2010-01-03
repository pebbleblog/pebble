package net.sourceforge.pebble.util;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.pebble.domain.Blog;

public class I18n {
	public static String getMessage(Locale locale, String key) {
		ResourceBundle bundle = getBundle(locale);
	    return bundle.getString(key);
	}
	
	public static String getMessage(Blog blog, String key) {
	    return getMessage(blog.getLocale(), key);
	}

	public static ResourceBundle getBundle(Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle("resources", locale);
		return bundle;
	}
}
