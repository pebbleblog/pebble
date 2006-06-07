package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.domain.StaticPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class StaticPageHandler extends DefaultHandler {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(StaticPageHandler.class);

  private static final int NOT_DEFINED = -1;
  private static final int TITLE = 0;
  private static final int BODY = 2;
  private static final int DATE = 3;
  private static final int AUTHOR = 4;
  private static final int ORIGINAL_PERMALINK = 5;
  private static final int STATIC_NAME = 6;
  private static final int STATE = 18;
  private static final int SUBTITLE = 20;

  private StaticPage staticPage;
  private int elementStatus = NOT_DEFINED;
  private SimpleDateFormat dateTimeFormats[];

  private StringBuffer elementContent;

  public StaticPageHandler(StaticPage staticPage) {
    this.staticPage = staticPage;

    // create all date/time formats, for backwards compatibility
    SimpleDateFormat format;
    dateTimeFormats = new SimpleDateFormat[6];

    format = new SimpleDateFormat(FileBlogEntryDAO.NEW_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
    format.setTimeZone(staticPage.getBlog().getTimeZone());
    dateTimeFormats[0] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.NEW_PERSISTENT_DATETIME_FORMAT, staticPage.getBlog().getLocale());
    format.setTimeZone(staticPage.getBlog().getTimeZone());
    dateTimeFormats[1] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.NEW_PERSISTENT_DATETIME_FORMAT);
    format.setTimeZone(staticPage.getBlog().getTimeZone());
    dateTimeFormats[2] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.OLD_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
    format.setTimeZone(staticPage.getBlog().getTimeZone());
    dateTimeFormats[3] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.OLD_PERSISTENT_DATETIME_FORMAT, staticPage.getBlog().getLocale());
    format.setTimeZone(staticPage.getBlog().getTimeZone());
    dateTimeFormats[4] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.OLD_PERSISTENT_DATETIME_FORMAT);
    format.setTimeZone(staticPage.getBlog().getTimeZone());
    dateTimeFormats[5] = format;
  }

  public void startElement(String uri, String name, String qName, Attributes attributes) throws SAXException {
    //log.info("startElement : " + name);
    elementContent = new StringBuffer();
    if (name.equals("title")) {
      elementStatus = TITLE;
    } else if (name.equals("subtitle")) {
      elementStatus = SUBTITLE;
    } else if (name.equals("body")) {
      elementStatus = BODY;
    } else if (name.equals("date")) {
      elementStatus = DATE;
    } else if (name.equals("author")) {
      elementStatus = AUTHOR;
    } else if (name.equals("originalPermalink")) {
      elementStatus = ORIGINAL_PERMALINK;
    } else if (name.equals("staticName")) {
      elementStatus = STATIC_NAME;
    } else if (name.equals("state")) {
      elementStatus = STATE;
    } else if (name.equals("size")) {
    } else {
      elementStatus = NOT_DEFINED;
    }
  }

  public void endElement(String uri, String name, String qName) throws SAXException {
    //log.info("endElement : " + name);
    switch (elementStatus) {
      case TITLE :
        staticPage.setTitle(elementContent.toString());
        break;
      case SUBTITLE :
        staticPage.setSubtitle(elementContent.toString());
        break;
      case BODY :
        staticPage.setBody(elementContent.toString());
        break;
      case DATE :
        staticPage.setDate(getDate(elementContent.toString()));
        break;
//      case STATE :
//        if (elementContent.toString().equals(State.UNPUBLISHED.getName())) {
//          staticPage.setPublished(false);
//        } else {
//          staticPage.setPublished(true);
//        }
//        break;
      case AUTHOR :
        staticPage.setAuthor(elementContent.toString());
        break;
      case ORIGINAL_PERMALINK :
        staticPage.setOriginalPermalink(elementContent.toString());
        break;
      case STATIC_NAME :
        staticPage.setName(elementContent.toString());
        break;
    }
    elementStatus = NOT_DEFINED;
  }

  public void characters(char ch[], int start, int length) throws SAXException {
    elementContent.append(new String(ch, start, length));
    //log.info("characters : " + s);
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

  private Date getDate(String s) {
    for (DateFormat dateTimeFormat : dateTimeFormats) {
      try {
        return dateTimeFormat.parse(s);
      } catch (ParseException pe) {
        // do nothing, just try the next one
      }
    }

    log.error("Could not parse date of " + s);
    return null;
  }

}