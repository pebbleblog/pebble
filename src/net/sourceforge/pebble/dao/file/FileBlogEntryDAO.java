package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.*;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileBlogEntryDAO implements BlogEntryDAO {

  /**
   * the log used by this class
   */
  private static Log log = LogFactory.getLog(FileBlogEntryDAO.class);

  private DocumentBuilder builder;

  public FileBlogEntryDAO() {
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

  private String getPath(DailyBlog dailyBlog) {
    DecimalFormat format = new DecimalFormat("00");
    StringBuffer path = new StringBuffer();
    path.append(dailyBlog.getBlog().getRoot());
    path.append(File.separator);
    path.append(dailyBlog.getMonthlyBlog().getYearlyBlog().getYear());
    path.append(File.separator);
    path.append(format.format(dailyBlog.getMonthlyBlog().getMonth()));
    path.append(File.separator);
    path.append(format.format(dailyBlog.getDay()));
    path.append(File.separator);

    return path.toString();
  }

  /**
   * Loads the blog entries for a given daily blog.
   *
   * @param dailyBlog the DailyBlog instance
   * @return a List of BlogEntry instances
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if blog entries cannot be loaded
   */
  public List getBlogEntries(DailyBlog dailyBlog) throws PersistenceException {
    String pathToBlogEntries = getPath(dailyBlog);
    log.debug("Loading blog entries from " + pathToBlogEntries);

    File dir = new File(pathToBlogEntries);
    File files[] = dir.listFiles(new BlogEntryFilenameFilter());

    List entries = new ArrayList();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        BlogEntry entry;
        entry = loadBlogEntry(dailyBlog, files[i]);
        entry.setType(BlogEntry.PUBLISHED);
        entries.add(entry);
      }
    }

    return entries;
  }

  /**
   * Loads the draft blog entries for a given blog.
   *
   * @param blog the owning Blog instance
   * @return a List of BlogEntry instances
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if blog entries cannot be loaded
   */
  public Collection getDraftBlogEntries(Blog blog) throws PersistenceException {
    File dir = new File(blog.getRoot(), "drafts");
    File files[] = dir.listFiles(new BlogEntryFilenameFilter());

    List entries = new ArrayList();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        BlogEntry entry;
        entry = loadBlogEntry(blog.getBlogForToday(), files[i]);
        entry.setType(BlogEntry.DRAFT);
        entries.add(entry);
      }
    }

    return entries;
  }

  /**
   * Loads the blog entry templates for a given blog.
   *
   * @param blog the owning Blog instance
   * @return a List of BlogEntry instances
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if blog entries cannot be loaded
   */
  public Collection getBlogEntryTemplates(Blog blog) throws PersistenceException {
    File dir = new File(blog.getRoot(), "templates");
    File files[] = dir.listFiles(new BlogEntryFilenameFilter());

    List entries = new ArrayList();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        BlogEntry entry;
        entry = loadBlogEntry(blog.getBlogForToday(), files[i]);
        entry.setType(BlogEntry.TEMPLATE);
        entries.add(entry);
      }
    }

    return entries;
  }

  /**
   * Loads the static pages for a given blog.
   *
   * @param blog the owning Blog instance
   * @return a List of BlogEntry instances
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if blog entries cannot be loaded
   */
  public Collection getStaticPages(Blog blog) throws PersistenceException {
    File dir = new File(blog.getRoot(), "pages");
    File files[] = dir.listFiles(new BlogEntryFilenameFilter());

    List entries = new ArrayList();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        BlogEntry entry;
        entry = loadBlogEntry(blog.getBlogForToday(), files[i]);
        entry.setType(BlogEntry.STATIC_PAGE);
        entries.add(entry);
      }
    }

    return entries;
  }

  /**
   * Loads a blog entry from the specified file.
   *
   * @param dailyBlog the owning DailyBlog instance
   * @param source    the File pointing to the source
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the blog entry can't be loaded
   */
  private BlogEntry loadBlogEntry(DailyBlog dailyBlog, File source) throws PersistenceException {
      log.debug("Loading " + source.getAbsolutePath());
      BlogEntry blogEntry = new BlogEntry(dailyBlog);

      try {
        DefaultHandler handler = new BlogEntryHandler(blogEntry);
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

  /*
      // quick check that the name of the file isn't messed up
      String filename = source.getName();
      long timeByFilename = Long.parseLong(filename.substring(0, filename.length() - 4));
      if (blogEntry.getDate().getTime() != timeByFilename) {
        log.warn("Filename for " + blogEntry.getTitle() + " is incorrect");
      }
      */

      return blogEntry;
  }

  /**
   * Stores the specified blog entry.
   *
   * @param blogEntry the blog entry to store
   * @throws PersistenceException if something goes wrong storing the entry
   */
  public void store(BlogEntry blogEntry) throws PersistenceException {
    File outputDir = null;

    switch (blogEntry.getType()) {
      case BlogEntry.DRAFT:
        outputDir = new File(blogEntry.getBlog().getRoot(), "drafts");
        break;
      case BlogEntry.TEMPLATE:
        outputDir = new File(blogEntry.getBlog().getRoot(), "templates");
        break;
      case BlogEntry.STATIC_PAGE:
        outputDir = new File(blogEntry.getBlog().getRoot(), "pages");
        break;
      default :
        outputDir = new File(getPath(blogEntry.getDailyBlog()));
        break;
    }

    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    File outputFile = new File(outputDir, blogEntry.getId() + ".xml");
    store(blogEntry, outputFile);
  }


  /**
   * Stores a blog entry to the specified file.
   *
   * @param blogEntry   the BlogEntry that is being stored
   * @param destination the File pointing to the destination
   * @throws PersistenceException if something goes wrong storing the entry
   */
  private void store(BlogEntry blogEntry, File destination) throws PersistenceException {
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

      if (blogEntry.getStaticName() != null) {
        staticNameNode.appendChild(doc.createTextNode(blogEntry.getStaticName()));
      }

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
   * @throws java.lang.Exception if something goes wrong
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
   * @throws java.lang.Exception if something goes wrong
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
   * @throws PersistenceException if something goes wrong removing the entry
   */
  public void remove(BlogEntry blogEntry) throws PersistenceException {
    File path = null;

    switch (blogEntry.getType()) {
      case BlogEntry.DRAFT:
        path = new File(blogEntry.getBlog().getRoot(), "drafts");
        break;
      case BlogEntry.TEMPLATE:
        path = new File(blogEntry.getBlog().getRoot(), "templates");
        break;
      case BlogEntry.STATIC_PAGE:
        path = new File(blogEntry.getBlog().getRoot(), "pages");
        break;
      default :
        path = new File(getPath(blogEntry.getDailyBlog()));
        break;
    }

    File file = new File(path, blogEntry.getId() + ".xml");
    log.debug("Removing " + file.getAbsolutePath());

    boolean success = file.delete();
    if (!success) {
      throw new PersistenceException("Removal of " + file.getAbsoluteFile() + " failed");
    }
  }

  /**
   * Gets the YearlyBlogs that the specified root blog is managing.
   *
   * @param rootBlog the owning Blog instance
   * @throws PersistenceException if the yearly blogs cannot be loaded
   */
  public List getYearlyBlogs(Blog rootBlog) throws PersistenceException {
    List yearlyBlogs = new ArrayList();

    File files[] = new File(rootBlog.getRoot()).listFiles();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        String filename = files[i].getName();
        if (filename.matches(REGEX_FOR_YEAR) && files[i].isDirectory()) {
          int year = Integer.parseInt(filename);
          YearlyBlog yearlyBlog = new YearlyBlog(rootBlog, year);
          yearlyBlogs.add(yearlyBlog);
        }
      }
    }

    return yearlyBlogs;
  }

/*
  public static void main(String[] args) throws Exception {
    DAOFactory.setConfiguredFactory(new FileDAOFactory());
    FileBlogEntryDAO dao = new FileBlogEntryDAO();
    Blog blog = new Blog(args[0]);
    Collection yearlyBlogs = blog.getYearlyBlogs();
    Iterator it = yearlyBlogs.iterator();
    while (it.hasNext()) {
      YearlyBlog yearly = (YearlyBlog) it.next();
      Collection monthlyBlogs = yearly.getMonthlyBlogs();
      Iterator jt = monthlyBlogs.iterator();
      while (jt.hasNext()) {
        MonthlyBlog monthly = (MonthlyBlog) jt.next();
        Collection dailyBlogs = monthly.getDailyBlogs();
        Iterator kt = dailyBlogs.iterator();
        while (kt.hasNext()) {
          DailyBlog daily = (DailyBlog) kt.next();

          String pathToBlogEntries = dao.getPath(daily);

          File dir = new File(pathToBlogEntries);
          File files[] = dir.listFiles(new BlogEntryFilenameFilter());

          if (files != null) {
            for (int i = 0; i < files.length; i++) {
              BlogEntry entry;
              entry = dao.loadBlogEntry(daily, files[i]);

              // quick check that the name of the file isn't messed up
              String filename = files[i].getName();
              long timeByFilename = Long.parseLong(filename.substring(0, filename.length() - 4));
              if (entry.getDate().getTime() != timeByFilename) {
                if (files[i].delete()) {
                  log.info("Removed " + files[i].getName());
                  entry.store();
                }
              }
            }
          }
        }
      }
    }
    */

}

/**
 * Filters out any files that aren't blog entries.
 *
 * @author Simon Brown
 */
class BlogEntryFilenameFilter implements FilenameFilter {

  /**
   * Tests if a specified file should be included in a file list.
   *
   * @param dir  the directory in which the file was found.
   * @param name the name of the file.
   * @return <code>true</code> if and only if the name should be
   *         included in the file list; <code>false</code> otherwise.
   */
  public boolean accept(File dir, String name) {
    return name.matches("\\d+.xml\\z");
  }

}