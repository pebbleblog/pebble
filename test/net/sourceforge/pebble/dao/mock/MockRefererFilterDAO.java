package net.sourceforge.pebble.dao.mock;

import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.RefererFilterDAO;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.RefererFilter;

import java.util.Collection;
import java.util.HashSet;

/**
 * A mock implementation of the CategoryDAO interface that does nothing. This
 * is used when performing unit tests.
 *
 * @author    Simon Brown
 */
public class MockRefererFilterDAO implements RefererFilterDAO {

  /**
   * Loads the referer filters.
   *
   * @param rootBlog    the owning Blog instance
   * @return  a Collection of RefererFilter instances
   * @throws  PersistenceException    if filters cannot be loaded
   */
  public Collection getRefererFilters(Blog rootBlog) throws PersistenceException {
    return new HashSet();
  }

  /**
   * Adds the specified referer filter.
   *
   * @param filter    the RefererFilter instance to be added
   * @param rootBlog    the owning Blog instance
   * @throws PersistenceException   if something goes wrong storing the filters
   */
  public void addRefererFilter(RefererFilter filter, Blog rootBlog) throws PersistenceException {
  }

  /**
   * Removes the specified referer filter.
   *
   * @param filter   the RefererFilter instance to be removed
   * @param rootBlog the owning Blog instance
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong removing the filter
   */
  public void deleteRefererFilter(RefererFilter filter, Blog rootBlog) throws PersistenceException {
  }

}
