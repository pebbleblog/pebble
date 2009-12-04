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
  public static final String OTHER = "Other";

  static final String MSIE_50 = "MSIE 5.0";
  static final String MSIE_60 = "MSIE 6.0";
  static final String MSIE_70 = "MSIE 7.0";
  static final String MSIE_80 = "MSIE 8.0";
  static final String MSIE_90 = "MSIE 9.0";
  static final String FIREFOX_1X = "Firefox/1.";
  static final String FIREFOX_2X = "Firefox/2.";
  static final String FIREFOX_30 = "Firefox/3.0";
  static final String FIREFOX_35 = "Firefox/3.5";
  static final String FIREFOX_36 = "Firefox/3.6";
  static final String SAFARI = "Safari";
  static final String BLOGLINES = "Bloglines";
  static final String GOOGLEBOT = "Googlebot";
  static final String GOOGLE_FEEDFETCHER = "Feedfetcher-Google";
  static final String YAHOOBOT = "Yahoo! Slurp";

  /**
   * Consolidate given user agent to a short name if the agent is recognized,
   * UserAgentConsolidator.OTHER otherwise.
   * 
   * @param userAgent
   *          Name that the useragent identifies as
   * @return short name or UserAgentConsolidator.OTHER
   */
  public static String consolidate(String userAgent) {
    // extracted implementation. Simple, but works.
    String consolidatedUserAgent = OTHER;
    if (userAgent.contains(MSIE_50)) {
      consolidatedUserAgent = MSIE_50;
    } else if (userAgent.contains(MSIE_60)) {
      consolidatedUserAgent = MSIE_60;
    } else if (userAgent.contains(MSIE_70)) {
      consolidatedUserAgent = MSIE_70;
    } else if (userAgent.contains(MSIE_80)) {
      consolidatedUserAgent = MSIE_80;
    } else if (userAgent.contains(MSIE_90)) {
      consolidatedUserAgent = MSIE_90;
    } else if (userAgent.contains(FIREFOX_1X)) {
      consolidatedUserAgent = FIREFOX_1X;
    } else if (userAgent.contains(FIREFOX_2X)) {
      consolidatedUserAgent = FIREFOX_2X;
    } else if (userAgent.contains(FIREFOX_30)) {
      consolidatedUserAgent = FIREFOX_30;
    } else if (userAgent.contains(FIREFOX_35)) {
      consolidatedUserAgent = FIREFOX_35;
    } else if (userAgent.contains(FIREFOX_36)) {
      consolidatedUserAgent = FIREFOX_36;
    } else if (userAgent.contains(SAFARI)) {
      consolidatedUserAgent = SAFARI;
    } else if (userAgent.contains(BLOGLINES)) {
      consolidatedUserAgent = BLOGLINES;
    } else if (userAgent.contains(GOOGLEBOT)) {
      consolidatedUserAgent = GOOGLEBOT;
    } else if (userAgent.contains(GOOGLE_FEEDFETCHER)) {
      consolidatedUserAgent = GOOGLE_FEEDFETCHER;
    } else if (userAgent.contains(YAHOOBOT)) {
      consolidatedUserAgent = YAHOOBOT;
    }
    return consolidatedUserAgent;
  }

}
