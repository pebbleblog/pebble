package net.sourceforge.pebble.index.file;

import net.sourceforge.pebble.domain.Archive;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.index.BlogEntryIndex;

public class FileBlogEntryIndexTest extends SingleBlogTestCase {

  private BlogEntryIndex blogEntryIndex;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    blogEntryIndex = daoFactory.getBlogEntryIndex();
  }

  public void testGetPreviousBlogEntry() throws Exception {
    Archive archive =  blogEntryIndex.getArchive(blog);
    Day today = archive.getToday();
    Day oneDayAgo = archive.getPreviousDay(today);
    Day twoDaysAgo = archive.getPreviousDay(oneDayAgo);

    BlogEntry b1 = new BlogEntry(blog);
    b1.setDate(twoDaysAgo.getDate(blog.getCalendar()));
    b1.setPublished(true);

    BlogEntry b2 = new BlogEntry(blog);
    b2.setDate(oneDayAgo.getDate(blog.getCalendar()));
    b2.setPublished(true);

    BlogEntry b3 = new BlogEntry(blog);
    b3.setDate(today.getDate(blog.getCalendar()));
    b3.setPublished(true);

    blogService.putBlogEntry(b1);
    blogService.putBlogEntry(b2);
    blogService.putBlogEntry(b3);

    assertNull(blogEntryIndex.getPreviousBlogEntry(blog, b1.getId()));
    assertEquals(b1.getId(), blogEntryIndex.getPreviousBlogEntry(blog, b2.getId()));
    assertEquals(b2.getId(), blogEntryIndex.getPreviousBlogEntry(blog, b3.getId()));
  }


  public void testGetNextBlogEntry() throws Exception {
    Archive archive = blogEntryIndex.getArchive(blog);
    Day today = archive.getToday();
    Day oneDayAgo = archive.getPreviousDay(today);
    Day twoDaysAgo = archive.getPreviousDay(oneDayAgo);

    BlogEntry b1 = new BlogEntry(blog);
    b1.setDate(twoDaysAgo.getDate(blog.getCalendar()));
    b1.setPublished(true);

    BlogEntry b2 = new BlogEntry(blog);
    b2.setDate(oneDayAgo.getDate(blog.getCalendar()));
    b2.setPublished(true);

    BlogEntry b3 = new BlogEntry(blog);
    b3.setDate(today.getDate(blog.getCalendar()));
    b3.setPublished(true);

    blogService.putBlogEntry(b1);
    blogService.putBlogEntry(b2);
    blogService.putBlogEntry(b3);

    assertNull(blogEntryIndex.getNextBlogEntry(blog, b3.getId()));
    assertEquals(b3.getId(), blogEntryIndex.getNextBlogEntry(blog, b2.getId()));
    assertEquals(b2.getId(), blogEntryIndex.getNextBlogEntry(blog, b1.getId()));
  }

}
