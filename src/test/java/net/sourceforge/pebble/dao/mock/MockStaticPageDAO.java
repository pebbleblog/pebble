package net.sourceforge.pebble.dao.mock;

import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.StaticPage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a mock implementation of StaticPageDAO that is used when performing
 * unit tests.
 *
 * @author    Simon Brown
 */
public class MockStaticPageDAO implements StaticPageDAO {

  private Map<String,StaticPage> pages = new HashMap<String,StaticPage>();

  /**
   * Loads the static pages for a given blog.
   *
   * @param blog    the owning Blog instance
   * @return  a Collection of StaticPage instances
   * @throws  PersistenceException    if static pages cannot be loaded
   */
  public Collection<StaticPage> loadStaticPages(Blog blog) throws PersistenceException {
    return pages.values();
  }

  /**
   * Loads a specific static page.
   *
   * @param blog    the owning Blog
   * @param pageId   the page ID
   * @return a StaticPage instance
   * @throws PersistenceException   if the static page cannot be loaded
   */
  public StaticPage loadStaticPage(Blog blog, String pageId) throws PersistenceException {
    return pages.get(pageId);
  }

  /**
   * Stores the specified static page.
   *
   * @param staticPage the static page to store
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong storing the static page
   */
  public void storeStaticPage(StaticPage staticPage) throws PersistenceException {
    pages.put(staticPage.getId(), staticPage);
  }

  /**
   * Removes the specified static page.
   *
   * @param staticPage the static page to remove
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong removing the page
   */
  public void removeStaticPage(StaticPage staticPage) throws PersistenceException {
    pages.remove(staticPage.getId());
  }

  /**
   * Locks the specified static page.
   *
   * @param staticPage the static page to lock
   * @return true if the page could be locked, false otherwise
   */
  public boolean lock(StaticPage staticPage) {
    return true;
  }

  /**
   * Unlocks the specified static page.
   *
   * @param staticPage the static page to unlock
   * @return true if the page could be unlocked, false otherwise
   */
  public boolean unlock(StaticPage staticPage) {
    return true;
  }
}
