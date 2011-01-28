/*
 * Copyright (c) 2003-2011, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

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
import java.io.FileWriter;
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
          Category category = new Category(categoryType.getId(), categoryType.getName());
          category.setBlog(blog);
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
      FileWriter writer = new FileWriter(destination);
      marshaller.marshal(jaxbElement, writer);
      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
      throw new PersistenceException(e.getMessage());
    }
  }

}