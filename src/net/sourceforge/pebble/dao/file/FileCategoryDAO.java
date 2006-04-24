package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.dao.CategoryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class FileCategoryDAO implements CategoryDAO {

  /** the name of the file containing the category information */
  private static final String CATEGORIES_FILE = "categories.xml";

  /** the log used by this class */
  private static Log log = LogFactory.getLog(FileCategoryDAO.class);

  private DocumentBuilder builder;

  public FileCategoryDAO() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      factory.setNamespaceAware(true);
      factory.setIgnoringElementContentWhitespace(true);
      factory.setIgnoringComments(true);
      builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new ErrorHandler() {
        public void warning(SAXParseException e) throws SAXException {
          log.warn(e);
          throw e;
        }

        public void error(SAXParseException e) throws SAXException {
          log.error(e);
          throw e;
        }

        public void fatalError(SAXParseException e) throws SAXException {
          log.fatal(e);
          throw e;
        }
      });

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
    CategoryHandler handler = new CategoryHandler(blog);
    try {
      SAXParserFactory saxFactory = SAXParserFactory.newInstance();
      saxFactory.setValidating(false);
      saxFactory.setNamespaceAware(true);
      SAXParser parser = saxFactory.newSAXParser();
      File file = new File(blog.getRoot(), CATEGORIES_FILE);
      if (file.exists()) {
        parser.parse(file, handler);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
      throw new PersistenceException(e.getMessage());
    }

    return handler.getRootCategory();
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
   */
  private void store(Blog blog) throws PersistenceException {
    List categories = blog.getCategories();

    File outputFile = new File(blog.getRoot(), CATEGORIES_FILE);
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      factory.setNamespaceAware(true);
      factory.setIgnoringElementContentWhitespace(true);
      factory.setIgnoringComments(true);

      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.newDocument();

      Element root = doc.createElement("categories");
      doc.appendChild(root);

      Iterator it = categories.iterator();
      while (it.hasNext()) {
        Category category = (Category)it.next();
        storeCategory(category, doc, root);
      }

      // write the XMl to a String, and then write this string to a file
      // (if the XML format fails, we don't corrupt the file)
      StringWriter sw = new StringWriter();
      Source source = new DOMSource(doc);
      Result result = new StreamResult(sw);
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.setOutputProperty(OutputKeys.METHOD, "xml");
      xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      xformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
      xformer.setOutputProperty(OutputKeys.INDENT, "yes");
      xformer.transform(source, result);

      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
      bw.write(sw.getBuffer().toString());
      bw.flush();
      bw.close();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
      throw new PersistenceException(e.getMessage());
    }
  }

  /**
   * Helper method to store an individual category.
   *
   * @param category  the Category being stored
   * @param doc       the Document into which the category is to be inserted
   * @param root      the root Node for the category
   * @throws java.lang.Exception if something goes wrong
   */
  private void storeCategory(Category category, Document doc, Node root) throws Exception {
    Element categoryNode = doc.createElement("category");
    root.appendChild(categoryNode);

    Element idNode = doc.createElement("id");
    Element nameNode = doc.createElement("name");
    Element tagsNode = doc.createElement("tags");

    categoryNode.appendChild(idNode);
    categoryNode.appendChild(nameNode);
    categoryNode.appendChild(tagsNode);

    idNode.appendChild(createTextNode(doc, category.getId()));
    nameNode.appendChild(createTextNode(doc, category.getName()));
    tagsNode.appendChild(createTextNode(doc, category.getTags()));
  }

  private Node createTextNode(Document doc, String text) {
    if (text != null) {
      return doc.createTextNode(text);
    } else {
      return doc.createTextNode("");
    }
  }

}
