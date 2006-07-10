package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.TrackBackTokenManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Superclass for TrackBack link generation actions.
 *
 * @author    Simon Brown
 */
public abstract class AbstractTrackBackAction extends Action {

  public static final String BLOG_ENTRY_ID = "trackBack.blogEntryId";

  /** the log used by this class */
  private static Log log = LogFactory.getLog(AbstractTrackBackAction.class);

  protected void generateTrackBackLink(BlogEntry blogEntry) {
    String token = TrackBackTokenManager.getInstance().generateToken(blogEntry);

    StringBuffer link = new StringBuffer();
    link.append(blogEntry.getBlog().getUrl());
    link.append("addTrackBack.action?entry=");
    link.append(blogEntry.getId());
    link.append("&token=");
    link.append(token);

    getModel().put("trackBackLink", link);
  }

}
