package net.sourceforge.pebble.logging;

import junit.framework.TestCase;
import static net.sourceforge.pebble.logging.UserAgentConsolidator.consolidate;

public class UserAgentConsolidationTest extends TestCase {
  public void testUserAgentConsolidation() throws Exception {
    assertEquals("MSIE 8.0", consolidate("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; GTB6.3; SIMBAR={1C3922DD-71F2-472A-A5C6-940389EF01C9}; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; OfficeLiveConnector.1.3; OfficeLivePatch.0.0)"));
    assertEquals("Other", consolidate("Mozilla/5.0 (compatible; Ask Jeeves/Teoma; +http://about.ask.com/en/docs/about/webmasters.shtml)"));
    assertEquals("Googlebot", consolidate("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"));
    assertEquals("Other", consolidate("Mozilla/5.0 (compatible; iCcrawler - iCjobs Stellenangebote Jobs; http://www.icjobs.de)"));
    assertEquals("MSIE 7.0", consolidate("Mozilla/5.0 (compatible; MSIE 7.0b; Windows NT 6.0)"));
    assertEquals("Other", consolidate("Mozilla/5.0 (compatible; Tagoobot/3.0; +http://www.tagoo.ru)"));
    assertEquals("Yahoo! Slurp", consolidate("Mozilla/5.0 (compatible; Yahoo! Slurp/3.0; http://help.yahoo.com/help/us/ysearch/slurp)"));
    assertEquals("Firefox/3.0", consolidate("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; en-US; rv:1.9.0.5) Gecko/2008120121 Firefox/3.0.5"));
    assertEquals("Safari", consolidate("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_2; de-de) AppleWebKit/531.21.8 (KHTML, like Gecko) Version/4.0.4 Safari/531.21.10"));
    assertEquals("Firefox/3.0", consolidate("Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.0.15) Gecko/2009101601 Firefox/3.0.15 (.NET CLR 3.5.30729)"));
    assertEquals("Firefox/3.5", consolidate("Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5"));
  }
}
