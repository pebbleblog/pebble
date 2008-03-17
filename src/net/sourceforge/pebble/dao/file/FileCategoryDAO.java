package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.dao.CategoryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.CategoryBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * DAO responsible for managing the storage of category definitions.
 *
 * @author    Simon Brown
 */
public class FileCategoryDAO implements CategoryDAO {

  /** the name of the file containing the category information */
  private static final String CATEGORIES_FILE_NAME = "categories.xml";

  /** the log used by this class */
  private static Log log = LogFactory.getLog(FileCategoryDAO.class);

  private JAXBContext jaxbContext;

  /**
   * Default, no args constructor.
   */
  public FileCategoryDAO() {
    try {
      jaxbContext = JAXBContext.newInstance(getClass().getPackage().getName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the categories for a particular blog.
   *
   * @param blog    the owning Blog instance
   * @return  a Collection of Category instances
   * @throws  PersistenceException    if categories cannot be loaded
   */
  public Category getCategories(Blog blog) throws PersistenceException {
    CategoryBuilder categoryBuilder = new CategoryBuilder(blog);
    File source = new File(blog.getRoot(), CATEGORIES_FILE_NAME);
    if (source.exists()) {
      try {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<CategoriesType> controller = (JAXBElement)unmarshaller.unmarshal(source);
        CategoriesType categoriesType = controller.getValue();

        for (CategoryType categoryType : categoriesType.getCategory()) {
          Category category = new Category();
          category.setId(categoryType.getId());
          category.setName(categoryType.getName());
          category.setTags(categoryType.getTags());

          categoryBuilder.addCategory(category);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
        throw new PersistenceException(e.getMessage());
      }
    }                                                           

    return categoryBuilder.getRootCategory();
  }

  /**
   * Adds the specified category.
   *
   * @param category    the Category instance to be added
   * @param blog    the owning blog
   * @throws PersistenceException   if something goes wrong storing the category
   */
  public void addCategory(Category category, Blog blog) throws PersistenceException {
    store(blog);
  }

  /**
   * Updates the specified category.
   *
   * @param updatedCategory   the Category instance to be updated
   * @param blog    the owning blog
   * @throws PersistenceException   if something goes wrong storing the category
   */
  public void updateCategory(Category updatedCategory, Blog blog) throws PersistenceException {
    store(blog);
  }

  /**
   * Removes the specified category.
   *
   * @param category    the Category instance to be removed
   * @param blog    the owning blog
   * @throws PersistenceException   if something goes wrong removing the category
   */
  public void deleteCategory(Category category, Blog blog) throws PersistenceException {
    store(blog);
  }

  /**
   * Helper method to store all categories for a given blog.
   *
   * @param blog      the blog to which the categories belong
   * @throws  PersistenceException    if the categories cannnot be stored
   */
  private void store(Blog blog) throws PersistenceException {
    List<Category> categories = blog.getCategories();
    File destination = new File(blog.getRoot(), CATEGORIES_FILE_NAME);
    try {
      Marshaller marshaller = jaxbContext.createMarshaller();
      CategoriesType categoriesType = new CategoriesType();

      for (Category category : categories) {
        CategoryType categoryType = new CategoryType();
        categoryType.setId(category.getId());
        categoryType.setName(category.getName());
        categoryType.setTags(category.getTags());
        categoriesType.getCategory().add(categoryType);
      }

      log.debug("Saving to " + destination.getAbsolutePath());
      ObjectFactory objectFactory = new ObjectFactory();
      JAXBElement jaxbElement = objectFactory.createCategories(categoriesType);

      marshaller.setProperty("jaxb.formatted.output", true);
      marshaller.setProperty("jaxb.encoding", blog.getCharacterEncoding());
      marshaller.marshal(jaxbElement, destination);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
      throw new PersistenceException(e.getMessage());
    }
  }

}