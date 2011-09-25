package net.sourceforge.pebble.dao.orient.model;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.State;
import net.sourceforge.pebble.domain.TrackBack;

/**
 * OrientDB trackback
 */
public class OrientTrackBack extends OrientResponse {
  private String excerpt;
  private String blogName;
  private String url;
  private String state;

  private OrientTrackBack() {
  }

  public OrientTrackBack(TrackBack trackBack) {
    super(trackBack);
    excerpt = trackBack.getExcerpt();
    blogName = trackBack.getBlogName();
    url = trackBack.getUrl();
    state = trackBack.getState().getName();
  }

  public TrackBack toTrackBack(BlogEntry blogEntry) {
    return blogEntry.createTrackBack(title, excerpt, url, blogName, ipAddress, date, State.getState(state));
  }
}
