package net.sourceforge.pebble.util;

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

  public static void upgradeBlog(Blog blog, String fromVersion, String toVersion) throws Exception {
    log.info("Upgrading blog from " + fromVersion + " to " + toVersion);
    if (fromVersion == null) {
      Utilities.restructureBlogToGMT(blog);
      Utilities.resetTheme(blog);
      Utilities.resetPlugins(blog);
      Utilities.buildIndexes(blog);
    } else if (toVersion.equals("2.1")) {
      Utilities.resetTheme(blog);
    }
  }

}
