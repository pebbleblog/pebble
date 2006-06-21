package net.sourceforge.pebble.event.blog;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sourceforge.pebble.api.event.blog.BlogEvent;
import net.sourceforge.pebble.api.event.blog.BlogListener;
import net.sourceforge.pebble.domain.BlogService;

import java.net.URL;

/**
 * Blog listener that starts up and shuts down the caches used by
 * the blog.
 *
 * @author Simon Brown
 */
public class CacheListener implements BlogListener {

  private CacheManager cacheManager;

  /**
   * Called when a blog has been started.
   *
   * @param event   a BlogEvent instance
   */
  public void blogStarted(BlogEvent event) {
    URL url = BlogService.class.getResource("/ehcache.xml");
    cacheManager = new CacheManager(url);
    Cache cache = cacheManager.getCache("blogEntriesCache");
    event.getBlog().setBlogEntryCache(cache);
  }

  /**
   * Called when a blog has been stopped.
   *
   * @param event   a BlogEvent instance
   */
  public void blogStopped(BlogEvent event) {
    cacheManager.shutdown();
  }

}
