package net.sourceforge.pebble.dao.mock;

import net.sourceforge.pebble.dao.CategoryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.CategoryBuilder;
import net.sourceforge.pebble.domain.Blog;

/**
 * A mock implementation of the CategoryDAO interface that does nothing. This
 * is used when performing unit tests.
 *
 * @author    Simon Brown
 */
public class MockCategoryDAO implements CategoryDAO {

  /**
   * Gets the categories for a particular blog.
   *
   * @param blog    the owning blog
   * @return  a Collection of Category instances
   * @throws  PersistenceException    if categories cannot be loaded
   */
  public Category getCategories(Blog blog) throws PersistenceException {
    CategoryBuilder builder = new CategoryBuilder(blog);
    return builder.getRootCategory();
  }

  /**
   * Adds the specified category.
   *
   * @param category the Category instance to be added
   * @param blog     the owning blog
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong storing the category
   */
  public void addCategory(Category category, Blog blog) throws PersistenceException {
  }

  /**
   * Updates the specified category.
   *
   * @param category the Category instance to be updated
   * @param blog     the owning blog
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong storing the category
   */
  public void updateCategory(Category category, Blog blog) throws PersistenceException {
  }

  /**
   * Removes the specified category.
   *
   * @param category the Category instance to be removed
   * @param blog     the owning blog
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong removing the category
   */
  public void deleteCategory(Category category, Blog blog) throws PersistenceException {
  }

}
