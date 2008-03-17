package net.sourceforge.pebble.dao.mock;

import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;

import java.util.*;

/**
 * This is a mock implementation of BlogEntryDAO that is used when performing
 * unit tests.
 *
 * @author    Simon brown
 */
public class MockBlogEntryDAO implements BlogEntryDAO {

  private Map blogEntries = new HashMap();

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
  public Collection<BlogEntry> loadBlogEntries(Blog blog) throws PersistenceException {
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
    blogEntries.put(blogEntry.getId(), blogEntry);
  }

  /**
   * Removes the specified blog entry.
   *
   * @param blogEntry   the blog entry to remove
   * @throws PersistenceException   if something goes wrong removing the entry
   */
  public void removeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    blogEntries.remove(blogEntry.getId());
  }

}
