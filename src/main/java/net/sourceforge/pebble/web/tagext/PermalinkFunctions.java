package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.api.permalink.PermalinkProvider;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;

public class PermalinkFunctions {
  public static String monthPermalink(PermalinkProvider permalinkProvider, Month month) {
    return permalinkProvider.getPermalink(month);
  }
  public static String dayPermalink(PermalinkProvider permalinkProvider, Day day) {
    return permalinkProvider.getPermalink(day);
  }
  public static String blogEntryPermalink(PermalinkProvider permalinkProvider, BlogEntry blogEntry) {
    return permalinkProvider.getPermalink(blogEntry);
  }
}
