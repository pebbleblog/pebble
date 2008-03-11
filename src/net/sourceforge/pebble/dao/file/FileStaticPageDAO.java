package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.util.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileStaticPageDAO implements StaticPageDAO {

  /**
   * the log used by this class
   */
  private static Log log = LogFactory.getLog(FileStaticPageDAO.class);

  private static final String STATIC_PAGES_DIRECTORY_NAME = "pages";
  private static final String STATIC_PAGE_FILE_EXTENSION = ".xml";
  private static final String STATIC_PAGE_LOCK_EXTENSION = ".lock";

  private DocumentBuilder builder;

  public FileStaticPageDAO() {
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

  /** the date/time format used when persisting dates */
  static final String OLD_PERSISTENT_DATETIME_FORMAT = "dd MMM yyyy HH:mm:ss z";
  static final String NEW_PERSISTENT_DATETIME_FORMAT = "dd MMM yyyy HH:mm:ss:S Z";
  static final String REGEX_FOR_YEAR = "\\d\\d\\d\\d";

  /**
   * Loads the static pages for a given blog.
   *
   * @param blog the owning Blog instance
   * @return a Collection of StaticPage instances
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if static pages cannot be loaded
   */
  public Collection<StaticPage> loadStaticPages(Blog blog) throws PersistenceException {
    List<StaticPage> list = new ArrayList<StaticPage>();
    File root = new File(blog.getRoot(), STATIC_PAGES_DIRECTORY_NAME);
    File files[] = root.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return new File(dir, name).isDirectory() && name.matches("\\d+");
        }
    });
    
    if (files != null) {
      for (File file : files) {
        StaticPage staticPage = loadStaticPage(blog, file.getName());
        if (staticPage != null) {
          list.add(staticPage);
        }
      }
    }

    return list;
  }

  /**
   * Loads a specific static page.
   *
   * @param blog   the owning Blog
   * @param pageId the page ID
   * @return a StaticPage instance
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the static page cannot be loaded
   */
  public StaticPage loadStaticPage(Blog blog, String pageId) throws PersistenceException {
    File path = new File(getPath(blog, pageId));
    File file = new File(path, pageId + STATIC_PAGE_FILE_EXTENSION);
    return loadStaticPage(blog, file);
  }

  /**
   * Stores the specified static page.
   *
   * @param staticPage the static page to store
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong storing the static page
   */
  public void storeStaticPage(StaticPage staticPage) throws PersistenceException {
    File outputDir = new File(getPath(staticPage.getBlog(), staticPage.getId()));
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    File outputFile = new File(outputDir, staticPage.getId() + STATIC_PAGE_FILE_EXTENSION);
    storeStaticPage(staticPage, outputFile);
  }

  /**
   * Stores a static page to the specified file.
   *
   * @param staticPage    the StaticPage that is being stored
   * @param destination the File pointing to the destination
   * @throws PersistenceException if something goes wrong storing the static page
   */
  private void storeStaticPage(StaticPage staticPage, File destination) throws PersistenceException {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      factory.setNamespaceAware(true);
      factory.setIgnoringElementContentWhitespace(true);
      factory.setIgnoringComments(true);

      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.newDocument();

      Element root = doc.createElement("staticPage");
      doc.appendChild(root);

      Element titleNode = doc.createElement("title");
      Element subtitleNode = doc.createElement("subtitle");
      Element bodyNode = doc.createElement("body");
      Element dateNode = doc.createElement("date");
      Element stateNode = doc.createElement("state");
      Element authorNode = doc.createElement("author");
      Element staticNameNode = doc.createElement("staticName");

      root.appendChild(titleNode);
      root.appendChild(subtitleNode);
      root.appendChild(bodyNode);
      root.appendChild(dateNode);
      root.appendChild(stateNode);
      root.appendChild(authorNode);
      root.appendChild(staticNameNode);

      if (staticPage.isAggregated()) {
        Element permalinkNode = doc.createElement("originalPermalink");
        permalinkNode.appendChild(doc.createTextNode(staticPage.getOriginalPermalink()));
        root.appendChild(permalinkNode);
      }

      titleNode.appendChild(doc.createTextNode(staticPage.getTitle()));
      subtitleNode.appendChild(doc.createTextNode(staticPage.getSubtitle()));
      bodyNode.appendChild(doc.createCDATASection(staticPage.getBody()));

      if (staticPage.getAuthor() != null) {
        authorNode.appendChild(doc.createTextNode(staticPage.getAuthor()));
      }

      if (staticPage.getName() != null) {
        staticNameNode.appendChild(doc.createTextNode(staticPage.getName()));
      }

      SimpleDateFormat sdf = new SimpleDateFormat(NEW_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
      sdf.setTimeZone(staticPage.getBlog().getTimeZone());
      dateNode.appendChild(doc.createTextNode(sdf.format(staticPage.getDate())));

      stateNode.appendChild(createTextNode(doc, staticPage.getState().getName()));

      // write the XMl to a String, and then write this string to a file
      // (if the XML format fails, we don't corrupt the file)
      StringWriter sw = new StringWriter();
      Source source = new DOMSource(doc);
      Result result = new StreamResult(sw);
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.setOutputProperty(OutputKeys.METHOD, "xml");
      xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      xformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
      xformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "body");
      xformer.setOutputProperty(OutputKeys.INDENT, "yes");
      xformer.transform(source, result);

      // now take a backup of the correct file
      if (destination.exists() && destination.length() > 0) {
        removeStaticPage(staticPage); // this archives the current version
      }

      log.debug("Saving to " + destination.getAbsolutePath());
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination), "UTF-8"));
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
   * Removes the specified static page.
   *
   * @param staticPage the static page to remove
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong removing the page
   */
  public void removeStaticPage(StaticPage staticPage) throws PersistenceException {
    File path = new File(getPath(staticPage.getBlog(), staticPage.getId()));
    File file = new File(path, staticPage.getId() + STATIC_PAGE_FILE_EXTENSION);
    log.debug("Removing " + staticPage.getGuid());

    // for archive purposes, the current version of the file will be saved with
    // the timestamp as the extension
    SimpleDateFormat archiveFileExtension = new SimpleDateFormat("yyyyMMdd-HHmmss");
    archiveFileExtension.setTimeZone(staticPage.getBlog().getTimeZone());
    Date date = new Date();
    if (file.exists()) {
      date = new Date(file.lastModified());
    }
    File backupFile = new File(
        file.getParentFile(),
        file.getName() + "." + archiveFileExtension.format(date));

    if (!backupFile.exists()) {
      log.debug("Archiving current version to " + backupFile.getAbsolutePath());
      boolean success = file.renameTo(backupFile);

      if (!success) {
        throw new PersistenceException("Deletion of " + staticPage.getGuid() + " failed");
      }
    }
  }

  /**
   * Loads a blog entry from the specified file.
   *
   * @param source    the File pointing to the source
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the blog entry can't be loaded
   */
  private StaticPage loadStaticPage(Blog blog, File source) throws PersistenceException {
    if (source.exists()) {
      log.debug("Loading static page from " + source.getAbsolutePath());
      StaticPage staticPage = new StaticPage(blog);

      try {
        DefaultHandler handler = new StaticPageHandler(staticPage);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setValidating(false);
        saxFactory.setNamespaceAware(true);
        SAXParser parser = saxFactory.newSAXParser();
        parser.parse(source, handler);

      } catch (Exception e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
        throw new PersistenceException(e.getMessage());
      }

      // and is the page locked?
      staticPage.setLockedBy(getUsernameHoldingLock(staticPage));

      return staticPage;
    } else {
      return null;
    }
  }

  /**
   * Stores the specified blog entry.
   *
   * @param blogEntry the blog entry to store
   * @throws net.sourceforge.pebble.dao.PersistenceException if something goes wrong storing the entry
   */
  public void storeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    File outputDir = new File(getPath(blogEntry.getBlog(), blogEntry.getId()));
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    File outputFile = new File(outputDir, blogEntry.getId() + ".xml");
    storeBlogEntry(blogEntry, outputFile);
  }


  /**
   * Stores a blog entry to the specified file.
   *
   * @param blogEntry   the BlogEntry that is being stored
   * @param destination the File pointing to the destination
   * @throws net.sourceforge.pebble.dao.PersistenceException if something goes wrong storing the entry
   */
  private void storeBlogEntry(BlogEntry blogEntry, File destination) throws PersistenceException {
    File backupFile = new File(destination.getParentFile(), destination.getName() + ".bak");
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);
      factory.setNamespaceAware(true);
      factory.setIgnoringElementContentWhitespace(true);
      factory.setIgnoringComments(true);

      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.newDocument();

      Element root = doc.createElement("blogEntry");
      doc.appendChild(root);

      Element titleNode = doc.createElement("title");
      Element subtitleNode = doc.createElement("subtitle");
      Element excerptNode = doc.createElement("excerpt");
      Element bodyNode = doc.createElement("body");
      Element categoryNode;
      Element tagsNode = doc.createElement("tags");
      Element dateNode = doc.createElement("date");
      Element stateNode = doc.createElement("state");
      Element authorNode = doc.createElement("author");
      Element staticNameNode = doc.createElement("staticName");
      Element commentsEnabledNode = doc.createElement("commentsEnabled");
      Element trackBacksEnabledNode = doc.createElement("trackBacksEnabled");
      Element attachmentNode = doc.createElement("attachment");

      root.appendChild(titleNode);
      root.appendChild(subtitleNode);
      root.appendChild(excerptNode);
      root.appendChild(bodyNode);
      root.appendChild(dateNode);
      root.appendChild(stateNode);
      root.appendChild(authorNode);
      root.appendChild(staticNameNode);

      if (blogEntry.isAggregated()) {
        Element permalinkNode = doc.createElement("originalPermalink");
        permalinkNode.appendChild(doc.createTextNode(blogEntry.getOriginalPermalink()));
        root.appendChild(permalinkNode);
      }

      titleNode.appendChild(doc.createTextNode(blogEntry.getTitle()));
      subtitleNode.appendChild(doc.createTextNode(blogEntry.getSubtitle()));
      bodyNode.appendChild(doc.createCDATASection(blogEntry.getBody()));

      if (blogEntry.getExcerpt() != null) {
        excerptNode.appendChild(doc.createCDATASection(blogEntry.getExcerpt()));
      }

      root.appendChild(commentsEnabledNode);
      commentsEnabledNode.appendChild(doc.createTextNode("" + blogEntry.isCommentsEnabled()));

      root.appendChild(trackBacksEnabledNode);
      trackBacksEnabledNode.appendChild(doc.createTextNode("" + blogEntry.isTrackBacksEnabled()));

      Iterator it = blogEntry.getCategories().iterator();
      Category category;
      while (it.hasNext()) {
        category = (Category) it.next();
        categoryNode = doc.createElement("category");
        categoryNode.appendChild(doc.createTextNode(category.getId()));
        root.appendChild(categoryNode);
      }

      if (blogEntry.getTags() != null) {
        root.appendChild(tagsNode);
        tagsNode.appendChild(doc.createTextNode(blogEntry.getTags()));
      }

      if (blogEntry.getAuthor() != null) {
        authorNode.appendChild(doc.createTextNode(blogEntry.getAuthor()));
      }

//      if (blogEntry.getStaticName() != null) {
//        staticNameNode.appendChild(doc.createTextNode(blogEntry.getStaticName()));
//      }

      SimpleDateFormat sdf = new SimpleDateFormat(NEW_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
      sdf.setTimeZone(blogEntry.getBlog().getTimeZone());
      dateNode.appendChild(doc.createTextNode(sdf.format(blogEntry.getDate())));

      stateNode.appendChild(createTextNode(doc, blogEntry.getState().getName()));

      Attachment attachment = blogEntry.getAttachment();
      if (attachment != null) {
        root.appendChild(attachmentNode);
        Element attachmentUrlNode = doc.createElement("url");
        attachmentUrlNode.appendChild(createTextNode(doc, attachment.getUrl()));
        attachmentNode.appendChild(attachmentUrlNode);
        Element attachmentSizeNode = doc.createElement("size");
        attachmentSizeNode.appendChild(createTextNode(doc, "" + attachment.getSize()));
        attachmentNode.appendChild(attachmentSizeNode);
        Element attachmentTypeNode = doc.createElement("type");
        attachmentTypeNode.appendChild(createTextNode(doc, attachment.getType()));
        attachmentNode.appendChild(attachmentTypeNode);
      }

      // and now store the comments
      it = blogEntry.getComments().iterator();
      while (it.hasNext()) {
        Comment comment = (Comment) it.next();
        storeComment(comment, doc, root);
      }

      // and finally the trackbacks
      it = blogEntry.getTrackBacks().iterator();
      while (it.hasNext()) {
        TrackBack trackBack = (TrackBack) it.next();
        storeTrackBack(trackBack, doc, root);
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
      xformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "body");
      xformer.setOutputProperty(OutputKeys.INDENT, "yes");
      xformer.transform(source, result);

      // now take a backup of the correct file
      if (destination.exists() && destination.length() > 0) {
        log.debug("Backing up to " + backupFile.getAbsolutePath());
        destination.renameTo(backupFile);
      }

      log.debug("Saving to " + destination.getAbsolutePath());
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination), "UTF-8"));
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
   * Helper method to store an individual comment.
   *
   * @param comment the Comment being stored
   * @param doc     the Document into which the comment is to be inserted
   * @param root    the root Node for the comment
   * @throws Exception if something goes wrong
   */
  private void storeComment(Comment comment, Document doc, Node root) throws Exception {
    Element commentNode = doc.createElement("comment");
    root.appendChild(commentNode);

    Element titleNode = doc.createElement("title");
    Element bodyNode = doc.createElement("body");
    Element authorNode = doc.createElement("author");
    Element emailNode = doc.createElement("email");
    Element websiteNode = doc.createElement("website");
    Element ipAddressNode = doc.createElement("ipAddress");
    Element dateNode = doc.createElement("date");
    Element parentNode = doc.createElement("parent");
    Element stateNode = doc.createElement("state");

    commentNode.appendChild(titleNode);
    commentNode.appendChild(bodyNode);
    commentNode.appendChild(authorNode);
    commentNode.appendChild(emailNode);
    commentNode.appendChild(websiteNode);
    commentNode.appendChild(ipAddressNode);
    commentNode.appendChild(dateNode);
    commentNode.appendChild(stateNode);

    titleNode.appendChild(createTextNode(doc, comment.getTitle()));
    bodyNode.appendChild(createCDATASection(doc, comment.getBody()));
    authorNode.appendChild(createTextNode(doc, comment.getAuthor()));
    emailNode.appendChild(createTextNode(doc, comment.getEmail()));
    websiteNode.appendChild(createTextNode(doc, comment.getWebsite()));
    ipAddressNode.appendChild(createTextNode(doc, comment.getIpAddress()));
    SimpleDateFormat sdf = new SimpleDateFormat(NEW_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
    sdf.setTimeZone(comment.getBlogEntry().getBlog().getTimeZone());
    dateNode.appendChild(createTextNode(doc, sdf.format(comment.getDate())));
    stateNode.appendChild(createTextNode(doc, comment.getState().getName()));

    if (comment.getParent() != null) {
      commentNode.appendChild(parentNode);
      parentNode.appendChild(createTextNode(doc, "" + comment.getParent().getId()));
    }
  }

  private Node createCDATASection(Document doc, String text) {
    if (text != null) {
      return doc.createCDATASection(text);
    } else {
      return doc.createCDATASection("");
    }
  }

  private Node createTextNode(Document doc, String text) {
    if (text != null) {
      return doc.createTextNode(text);
    } else {
      return doc.createTextNode("");
    }
  }

  /**
   * Helper method to store an individual trackback.
   *
   * @param trackBack the TrackBack being stored
   * @param doc       the Document into which the trackback is to be inserted
   * @param root      the root Node for the comment
   * @throws Exception if something goes wrong
   */
  private void storeTrackBack(TrackBack trackBack, Document doc, Node root) throws Exception {
    Element commentNode = doc.createElement("trackback");
    root.appendChild(commentNode);

    Element titleNode = doc.createElement("title");
    Element excerptNode = doc.createElement("excerpt");
    Element urlNode = doc.createElement("url");
    Element blogNameNode = doc.createElement("blogName");
    Element ipAddressNode = doc.createElement("ipAddress");
    Element dateNode = doc.createElement("date");
    Element stateNode = doc.createElement("state");

    commentNode.appendChild(titleNode);
    commentNode.appendChild(excerptNode);
    commentNode.appendChild(urlNode);
    commentNode.appendChild(blogNameNode);
    commentNode.appendChild(ipAddressNode);
    commentNode.appendChild(dateNode);
    commentNode.appendChild(stateNode);

    titleNode.appendChild(createTextNode(doc, trackBack.getTitle()));
    excerptNode.appendChild(createCDATASection(doc, trackBack.getExcerpt()));
    urlNode.appendChild(createTextNode(doc, trackBack.getUrl()));
    blogNameNode.appendChild(createTextNode(doc, trackBack.getBlogName()));
    ipAddressNode.appendChild(createTextNode(doc, trackBack.getIpAddress()));
    SimpleDateFormat sdf = new SimpleDateFormat(NEW_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
    sdf.setTimeZone(trackBack.getBlogEntry().getBlog().getTimeZone());
    dateNode.appendChild(doc.createTextNode(sdf.format(trackBack.getDate())));
    stateNode.appendChild(createTextNode(doc, trackBack.getState().getName()));
  }

  /**
   * Removes the specified blog entry.
   *
   * @param blogEntry the blog entry to remove
   * @throws net.sourceforge.pebble.dao.PersistenceException if something goes wrong removing the entry
   */
  public void removeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    File path = new File(getPath(blogEntry.getBlog(), blogEntry.getId()));
    File file = new File(path, blogEntry.getId() + ".xml");
    log.debug("Removing " + blogEntry.getGuid());

    boolean success = file.delete();
    if (!success) {
      throw new PersistenceException("Deletion of blog entry " + blogEntry.getGuid() + " failed");
    }
  }

  /**
   * Given a blog and blog entry ID, this method determines the path where
   * that blog entry is stored.
   *
   * @param blog    the owning Blog
   * @param staticPageId   the ID of the static page
   * @return  a String of the form blogroot/yyyy/MM/dd
   */
  private String getPath(Blog blog, String staticPageId) {
    StringBuffer buf = new StringBuffer();
    buf.append(blog.getRoot());
    buf.append(File.separator);
    buf.append("pages");
    buf.append(File.separator);
    buf.append(staticPageId);

    return buf.toString();
  }

  /**
   * Locks the specified static page.
   *
   * @param staticPage the static page to lock
   * @return  true if the page could be locked, false otherwise
   */
  public boolean lock(StaticPage staticPage) {
    File lockFile = getLockFile(staticPage);
    try {
      boolean success = lockFile.createNewFile();
      if (success) {
        FileWriter writer = new FileWriter(lockFile);
        writer.write(SecurityUtils.getUsername());
        writer.flush();
        writer.close();
        return true;
      } else {
        String lockedBy = getUsernameHoldingLock(staticPage);
        return (lockedBy != null && lockedBy.equals(SecurityUtils.getUsername()));
      }
    } catch (IOException e) {
      log.warn("Exceptoin while attempting to lock static page " + staticPage.getGuid(), e);
    }

    return false;
  }

  /**
   * Unlocks the specified static page.
   *
   * @param staticPage the static page to unlock
   * @return true if the page could be unlocked, false otherwise
   */
  public boolean unlock(StaticPage staticPage) {
    File lockFile = getLockFile(staticPage);
    if (lockFile.exists()) {
      return lockFile.delete();
    } else {
      return true;
    }
  }

  /**
   * Given a blog and blog entry ID, this method determines the path where
   * that blog entry is stored.
   *
   * @param staticPage    a StaticPage instance
   * @return  a File instance, representing a lock
   */
  private File getLockFile(StaticPage staticPage) {
    StringBuffer buf = new StringBuffer();
    buf.append(staticPage.getBlog().getRoot());
    buf.append(File.separator);
    buf.append(STATIC_PAGES_DIRECTORY_NAME);
    buf.append(File.separator);
    buf.append(staticPage.getId());
    buf.append(STATIC_PAGE_LOCK_EXTENSION);

    return new File(buf.toString());
  }

  private String getUsernameHoldingLock(StaticPage staticPage) {
    String username = null;
    try {
      File lockFile = getLockFile(staticPage);
      if (lockFile.exists()) {
        BufferedReader reader = new BufferedReader(new FileReader(lockFile));
        username = reader.readLine();
        reader.close();
      }
    } catch (IOException ioe) {
      log.warn("Error reading lock file", ioe);
    }

    return username;
  }

}