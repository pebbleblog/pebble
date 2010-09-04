package net.sourceforge.pebble.util;

/**
 * Don't rewrite URLs
 * @author Olaf Kock
 */
public class NullUrlRewriter extends UrlRewriter {
	public static final NullUrlRewriter instance = new NullUrlRewriter();
	
	public String rewrite(String url) {
		return url;
	}
}
