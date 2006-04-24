package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.CategoryBuilder;
import net.sourceforge.pebble.domain.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class CategoryHandler extends DefaultHandler {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(CategoryHandler.class);

  private CategoryBuilder categoryBuilder;
  private Category category = new Category();
  private String tags = null;

  private StringBuffer elementContent;

  public CategoryHandler(Blog blog) {
    this.categoryBuilder = new CategoryBuilder(blog);
  }

  public void startElement(String uri, String name, String qName, Attributes attributes) throws SAXException {
    elementContent = new StringBuffer();
  }

  public void endElement(String uri, String name, String qName) throws SAXException {
    if (name.equals("id")) {
      category.setId(elementContent.toString());
    } else if (name.equals("name")) {
      category.setName(elementContent.toString());
    } else if (name.equals("tags")) {
      tags = elementContent.toString();
    } else if (name.equals("category")) {
      categoryBuilder.addCategory(category);
      category.setTags(tags);
      category = new Category();
      tags = null;
    }
  }

  public void characters(char ch[], int start, int length) throws SAXException {
    elementContent.append(new String(ch, start, length));
  }

  public void warning(SAXParseException e) throws SAXException {
    log.warn(e);
  }

  public void error(SAXParseException e) throws SAXException {
    log.error(e);
  }

  public void fatalError(SAXParseException e) throws SAXException {
    log.fatal(e);
  }

  public Category getRootCategory() {
      return this.categoryBuilder.getRootCategory();
  }

}