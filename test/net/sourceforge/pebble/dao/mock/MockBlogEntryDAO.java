package net.sourceforge.pebble.dao.mock;

import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Blog;

import java.util.*;

/**
 * This is a mock implementation of BlogEntryDAO that is used when performing
 * unit tests.
 *
 * @author    Simon brown
 */
public class MockBlogEntryDAO implements BlogEntryDAO {

  private Map blogEntries = new HashMap();
  private Map draftBlogEntries = new HashMap();
  private Map blogEntryTemplates = new HashMap();
  private Map staticPages = new HashMap();

  /**
   * Loads a specific blog entry.
   *
   * @param blogEntryId   the blog entry ID
   * @return a BlogEntry instance
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the specified blog entry cannot be loaded
   */
  public BlogEntry loadBlogEntry(Blog blog, String blogEntryId) throws PersistenceException {
    return (BlogEntry)blogEntries.get(blogEntryId);
  }

  /**
   * Loads all blog entries.
   *
   * @param blog the Blog to load all entries for
   * @return a List of BlogEntry objects
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the blog entries cannot be loaded
   */
  public List<BlogEntry> loadBlogEntries(Blog blog) throws PersistenceException {
    List<BlogEntry> list = new ArrayList<BlogEntry>();
    for (Object o : blogEntries.values()) {
      list.add((BlogEntry)o);
    }
    return list;
  }

  /**
   * Stores the specified blog entry.
   *
   * @param blogEntry   the blog entry to store
   * @throws PersistenceException   if something goes wrong storing the entry
   */
  public void storeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
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
      default :
        blogEntries.put(blogEntry.getId(), blogEntry);
    }
  }

  /**
   * Removes the specified blog entry.
   *
   * @param blogEntry   the blog entry to remove
   * @throws PersistenceException   if something goes wrong removing the entry
   */
  public void removeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
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
      default :
        blogEntries.remove(blogEntry.getId());
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
  public List<BlogEntry> loadStaticPages(Blog blog) throws PersistenceException {
    return new ArrayList<BlogEntry>(staticPages.values());
  }

  /**
   * Loads a specific static page.
   *
   * @param blog    the owning Blog
   * @param pageId   the page ID
   * @return a BlogEntry instance
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the specified blog entry cannot be loaded
   */
  public BlogEntry loadStaticPage(Blog blog, String pageId) throws PersistenceException {
    return (BlogEntry)staticPages.get(pageId);
  }

}
