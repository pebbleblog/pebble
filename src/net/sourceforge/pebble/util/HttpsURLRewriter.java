package net.sourceforge.pebble.util;

/**
 * Rewrite Urls to use the given scheme, typically https.
 * @author Olaf Kock
 */

public class HttpsURLRewriter extends UrlRewriter {

	private final String currentScheme;

	public HttpsURLRewriter(String currentScheme) {
		this.currentScheme = currentScheme;
	}
	
	public String rewrite(String url) {
		return Utilities.calcBaseUrl(currentScheme, url);
	}

}
