package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Escapes &lt; and &gt; tags in the excerpt/body of blog entries and
 * the body of static pages.
 * 
 * @author Simon Brown
 */
public class EscapeMarkupDecorator extends ContentDecoratorSupport {

  private static final String ESCAPE_START_TAG = "<escape>";
  private static final String ESCAPE_END_TAG = "</escape>";

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    String escapedBody = escape(blogEntry.getBody());
    blogEntry.setBody(escapedBody);

    String escapedExcerpt = escape(blogEntry.getExcerpt());
    blogEntry.setExcerpt(escapedExcerpt);
  }

  /**
   * Decorates the specified static page.
   *
   * @param context    the context in which the decoration is running
   * @param staticPage the static page to be decorated
   */
  public void decorate(ContentDecoratorContext context, StaticPage staticPage) {
    String escapedBody = escape(staticPage.getBody());
    staticPage.setBody(escapedBody);
  }

  private String escape(String content) {
    // is there work to do?
    if (content == null || content.length() == 0) {
      return "";
    }

    // this pattern says "take the shortest match you can find where there are
    // one or more characters between escape tags"
    //  - the match is case insensitive and DOTALL means that newlines are
    //  - considered as a character match
    Pattern p = Pattern.compile(ESCAPE_START_TAG + ".+?" + ESCAPE_END_TAG,
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher m = p.matcher(content);

    // while there are blocks to be escaped
    while (m.find()) {
      int start = m.start();
      int end = m.end();

      // grab the text, strip off the escape tags and transform it
      String textToEscape = content.substring(start, end);
      textToEscape = textToEscape.substring(ESCAPE_START_TAG.length(), textToEscape.length() - ESCAPE_END_TAG.length());
      textToEscape = StringUtils.transformHTML(textToEscape);

      // now add it back into the original text
      content = content.substring(0, start) + textToEscape + content.substring(end, content.length());
      m = p.matcher(content);
    }

    return content;
  }

}
