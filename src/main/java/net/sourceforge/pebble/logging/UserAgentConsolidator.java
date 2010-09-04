package net.sourceforge.pebble.logging;


/**
 * Consolidate UserAgent String to a short name. This explicitly checks for a
 * few known snippets from the UserAgent that a browser reports. There's a
 * number of them missing - these are some of the most common as well as some
 * ancient ones.
 * 
 * Extracted from {@link net.sourceforge.pebble.web.action.ViewUserAgentsAction}
 */
public class UserAgentConsolidator {

  /**
   * UserAgent identifications that are known. Be careful when you add more: they are 
   * supposed to be checked for matches in the order they appear here. So place more 
   * specific names (e.g. containing a version number) at the beginning, more generic 
   * ones at the end. 
   */
  private static final String[] KNOWN_AGENTS = new String[] { "MSIE 5.0",
      "MSIE 6.0", "MSIE 7.0", "MSIE 8.0", "MSIE 9.0", "Firefox/1.",
      "Firefox/2.", "Firefox/3.0", "Firefox/3.5", "Firefox/3.6", "Safari",
      "Opera", "Chrome", "Bloglines", "Googlebot", "Feedfetcher-Google",
      "Yahoo! Slurp", "Bing"};  // Are all of these real? Should we remove old ones?

  /**
   * Consolidate given user agent to a short name if the agent is recognized,
   * UserAgentConsolidator.OTHER otherwise.
   * 
   * @param userAgent
   *          Name that the useragent identifies as
   * @return short name or UserAgentConsolidator.OTHER
   */
  public static String consolidate(String userAgent) {
    String consolidatedUserAgent = "Other"; // not localized...
    for(String knownAgent : KNOWN_AGENTS) {
      if (userAgent.contains(knownAgent)) {
        consolidatedUserAgent = knownAgent;
        break;
      } 
    }
    return consolidatedUserAgent;
  }

}
