package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.RefererFilterDAO;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.RefererFilter;
import net.sourceforge.pebble.domain.RefererFilterManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A mock implementation of the CategoryDAO interface that does nothing. This
 * is used when performing unit tests.
 *
 * @author    Simon Brown
 */
public class FileRefererFilterDAO implements RefererFilterDAO {

  /** the name of the file containing the filter */
  private static final String FILTERS_FILE = "refererFilters.txt";

  /** the log used by this class */
  private static Log log = LogFactory.getLog(RefererFilterManager.class);

  /**
   * Loads the referer filters.
   *
   * @param rootBlog    the owning Blog instance
   * @return  a Collection of RefererFilter instances
   * @throws  PersistenceException    if filters cannot be loaded
   */
  public Collection getRefererFilters(Blog rootBlog) throws PersistenceException {
    ArrayList filters = new ArrayList();
    String root = rootBlog.getRoot();
    try {

      File filtersFile = new File(root, FILTERS_FILE);
      if (!filtersFile.exists()) {
        return filters;
      }

      BufferedReader reader = new BufferedReader(new FileReader(filtersFile));
      String expression = reader.readLine();
      while (expression != null) {
        filters.add(new RefererFilter(expression));
        expression = reader.readLine();
      }

      reader.close();
    } catch (IOException ioe) {
      log.error("A " + FILTERS_FILE + " file at " + root + " cannot be loaded", ioe);
    }

    return filters;
  }

  /**
   * Adds the specified referer filter.
   *
   * @param filter    the RefererFilter instance to be added
   * @param rootBlog    the owning Blog instance
   * @throws PersistenceException   if something goes wrong storing the filters
   */
  public void addRefererFilter(RefererFilter filter, Blog rootBlog) throws PersistenceException {
    Collection filters = getRefererFilters(rootBlog);
    filters.add(filter);
    store(filters, rootBlog);
  }

  /**
   * Removes the specified referer filter.
   *
   * @param filter    the RefererFilter instance to be removed
   * @param rootBlog    the owning Blog instance
   * @throws PersistenceException   if something goes wrong removing the filter
   */
  public void deleteRefererFilter(RefererFilter filter, Blog rootBlog) throws PersistenceException {
    Collection filters = getRefererFilters(rootBlog);
    filters.remove(filter);
    store(filters, rootBlog);
  }

  /**
   * Helper method to store all filters for a given blog.
   *
   * @param filters   the Collection of RefererFilter instances to store
   * @param rootBlog      the blog to which the filters belong
   */
  private void store(Collection filters, Blog rootBlog) throws PersistenceException {
    try {
      String root = rootBlog.getRoot();
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File(root, FILTERS_FILE)));

      Iterator it = filters.iterator();
      RefererFilter filter;
      while (it.hasNext()) {
        filter = (RefererFilter)it.next();
        writer.write(filter.getExpression());
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (IOException ioe) {
      log.error(ioe);
      throw new PersistenceException("Filters could not be saved : " + ioe.getMessage());
    }
  }

}
