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
    if (fromVersion == null 
        || fromVersion.startsWith("2.3") 
        || fromVersion.startsWith("2.2") 
        || fromVersion.startsWith("2.1") 
        || fromVersion.startsWith("2.0")) {
      log.info("restructuring static pages");
      Utilities.restructureStaticPages(blog);
      log.info("building indizes");
      Utilities.buildIndexes(blog);
      log.info("upgrade done");
    } else {
      log.info("No upgrade required");
    }
  }

}
