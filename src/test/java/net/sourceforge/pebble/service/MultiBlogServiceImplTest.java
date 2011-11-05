package net.sourceforge.pebble.service;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogServiceException;
import net.sourceforge.pebble.domain.MultiBlogTestCase;

public class MultiBlogServiceImplTest extends MultiBlogTestCase {

  public void testRecentBlogEntries() throws BlogServiceException {
    // should be no days to start with
    assertTrue(multiBlogService.getRecentPublishedBlogEntries(0).isEmpty());

    // now add an entry
    BlogEntry blogEntry = new BlogEntry(blog1);
    blogEntry.setPublished(true);
    blogService.putBlogEntry(blogEntry);

    // ask for zero entries
    assertTrue(multiBlogService.getRecentPublishedBlogEntries(0).isEmpty());

    // ask for 1 entry
    assertTrue(multiBlogService.getRecentPublishedBlogEntries(1).contains(blogEntry));
  }
}
