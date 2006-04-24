package net.sourceforge.pebble.dao.mock;

import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.DailyBlog;
import net.sourceforge.pebble.domain.Blog;

import java.util.*;

/**
 * This is a mock implementation of BlogEntryDAO that is used when performing
 * unit tests.
 *
 * @author    Simon brown
 */
public class MockBlogEntryDAO implements BlogEntryDAO {

  private Map draftBlogEntries = new HashMap();
  private Map blogEntryTemplates = new HashMap();
  private Map staticPages = new HashMap();

  /**
   * Stores the specified blog entry.
   *
   * @param blogEntry   the blog entry to store
   * @throws PersistenceException   if something goes wrong storing the entry
   */
  public void store(BlogEntry blogEntry) throws PersistenceException {
    switch (blogEntry.getType()) {
      case BlogEntry.DRAFT :
        draftBlogEntries.put(blogEntry.getId(), blogEntry);
        break;
      case BlogEntry.TEMPLATE :
        blogEntryTemplates.put(blogEntry.getId(), blogEntry);
        break;
      case BlogEntry.STATIC_PAGE :
        staticPages.put(blogEntry.getId(), blogEntry);
        break;
    }
  }

  /**
   * Removes the specified blog entry.
   *
   * @param blogEntry   the blog entry to remove
   * @throws PersistenceException   if something goes wrong removing the entry
   */
  public void remove(BlogEntry blogEntry) throws PersistenceException {
    switch (blogEntry.getType()) {
      case BlogEntry.DRAFT :
        draftBlogEntries.remove(blogEntry.getId());
        break;
      case BlogEntry.TEMPLATE :
        blogEntryTemplates.remove(blogEntry.getId());
        break;
      case BlogEntry.STATIC_PAGE :
        staticPages.remove(blogEntry.getId());
        break;
    }
  }

  /**
   * Gets the YearlyBlogs that the specified root blog is managing.
   *
   * @param rootBlog    the owning Blog instance
   * @throws  PersistenceException    if the yearly blogs cannot be loaded
   */
  public List getYearlyBlogs(Blog rootBlog) throws PersistenceException {
    return new ArrayList();
  }

  /**
   * Loads the blog entries for a given daily blog.
   *
   * @param dailyBlog   the DailyBlog instance
   * @return  a List of BlogEntry instances
   * @throws  PersistenceException    if blog entries cannot be loaded
   */
  public List getBlogEntries(DailyBlog dailyBlog) throws PersistenceException {
    return new ArrayList();
  }

  /**
   * Loads the draft blog entries for a given blog.
   *
   * @param blog    the owning Blog instance
   * @return  a List of BlogEntry instances
   * @throws  net.sourceforge.pebble.dao.PersistenceException    if blog entries cannot be loaded
   */
  public Collection getDraftBlogEntries(Blog blog) throws PersistenceException {
    return draftBlogEntries.values();
  }

  /**
   * Loads the blog entry templates for a given blog.
   *
   * @param blog    the owning Blog instance
   * @return  a List of BlogEntry instances
   * @throws  net.sourceforge.pebble.dao.PersistenceException    if blog entries cannot be loaded
   */
  public Collection getBlogEntryTemplates(Blog blog) throws PersistenceException {
    return blogEntryTemplates.values();
  }

  /**
   * Loads the static pages for a given blog.
   *
   * @param blog    the owning Blog instance
   * @return  a List of BlogEntry instances
   * @throws  net.sourceforge.pebble.dao.PersistenceException    if blog entries cannot be loaded
   */
  public Collection getStaticPages(Blog blog) throws PersistenceException {
    return staticPages.values();
  }

}
