package net.sourceforge.pebble.util;

import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utilities for upgrading blogs.
 *
 * @author    Simon Brown
 */
public class UpgradeUtilities {

  /** the logger used by this action */
  private static final Log log = LogFactory.getLog(UpgradeUtilities.class);

  public static void upgradeBlog(Blog blog, String version) throws Exception {
    log.info("Upgrading blog from " + version + " to " + PebbleContext.getInstance().getBuildVersion());
    if (version == null) {
      Utilities.restructureBlogToGMT(blog);
      Utilities.buildIndexes(blog);
    }
  }

}
