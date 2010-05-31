package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adds a rel="nofollow" attribute into all links within comment
 * and TrackBack content.
 * 
 * @author Simon Brown
 */
public class NoFollowDecorator extends ContentDecoratorSupport {

  /** the regex used to find HTML links */
  private static Pattern HTML_LINK_PATTERN = Pattern.compile("<a.*?href=.*?>", Pattern.CASE_INSENSITIVE);

  /**
   * Decorates the specified comment.
   *
   * @param context the context in which the decoration is running
   * @param comment the comment to be decorated
   */
  public void decorate(ContentDecoratorContext context, Comment comment) {
    comment.setBody(addNoFollowLinks(comment.getBody()));
  }

  /**
   * Decorates the specified TrackBack.
   *
   * @param context   the context in which the decoration is running
   * @param trackBack the TrackBack to be decorated
   */
  public void decorate(ContentDecoratorContext context, TrackBack trackBack) {
    trackBack.setExcerpt(addNoFollowLinks(trackBack.getExcerpt()));
  }

  /**
   * Adds rel="nofollow" links too any links in the specified string.
   *
   * @param   html    a String containing HTML
   * @return  the same String with rel="nofollow" in anchor tags
   */
  private String addNoFollowLinks(String html) {
    if (html == null || html.length() == 0) {
      return html;
    }

    Matcher m = HTML_LINK_PATTERN.matcher(html);
    StringBuffer buf = new StringBuffer();

    while (m.find()) {
      int start = m.start();
      int end = m.end();

      String link = html.substring(start, end);
      buf.append(html.substring(0, start));

      // add the nofollow, if necessary
      int startOfRelIndex = link.indexOf("rel=\"");
      if (startOfRelIndex == -1) {
        // no rel link, add one
        buf.append(link.substring(0, link.length() - 1));
        buf.append(" rel=\"nofollow\">");
      } else {
        int endOfRelIndex = link.indexOf("\"", startOfRelIndex+5);
        String rel = link.substring(startOfRelIndex+5, endOfRelIndex);
        rel = rel.toLowerCase();
        if (rel.indexOf("nofollow") == -1) {
          // rel exists, but without nofollow
          buf.append(link.substring(0, endOfRelIndex));
          buf.append(" nofollow");
          buf.append(link.substring(endOfRelIndex));
        } else {
          // nofollow exists
          buf.append(link);
        }
      }

      html = html.substring(end, html.length());
      m = HTML_LINK_PATTERN.matcher(html);
    }

    buf.append(html);

    return buf.toString();
  }

}
