package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.domain.BlogEntry;

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
    getModel().put("trackBackLinkExpires", "true");
    getModel().put("trackBackLink", blogEntry.getTrackBackLink());
  }

}
