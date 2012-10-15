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

import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileBlogEntryDAO implements BlogEntryDAO {

  /** timezone to use for calculating paths on disk, etc */
  private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

  /**
   * the log used by this class
   */
  private static Log log = LogFactory.getLog(FileBlogEntryDAO.class);

  public FileBlogEntryDAO() {
  }

  /** the date/time format used when persisting dates */
  static final String OLD_PERSISTENT_DATETIME_FORMAT = "dd MMM yyyy HH:mm:ss z";
  static final String NEW_PERSISTENT_DATETIME_FORMAT = "dd MMM yyyy HH:mm:ss:S Z";
  static final String REGEX_FOR_YEAR = "\\d\\d\\d\\d";

  /**
   * Loads a specific blog entry.
   *
   * @param blogEntryId   the blog entry ID
   * @return a BlogEntry instance
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the specified blog entry cannot be loaded
   */
  public BlogEntry loadBlogEntry(Blog blog, String blogEntryId) throws PersistenceException {
    File path = new File(getPath(blog, blogEntryId, GMT));
    File file = new File(path, blogEntryId + ".xml");
    return loadBlogEntry(blog, file);
  }

  /**
   * Loads a blog entry from the specified file.
   *
   * @param source    the File pointing to the source
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the blog entry can't be loaded
   */
  private BlogEntry loadBlogEntry(Blog blog, File source) throws PersistenceException {
    if (source.exists()) {
      log.debug("Loading " + source.getAbsolutePath());
      BlogEntry blogEntry = new BlogEntry(blog);

      try {
        DefaultHandler handler = new BlogEntryHandler(blogEntry);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setValidating(false);
        saxFactory.setNamespaceAware(true);
        SAXParser parser = saxFactory.newSAXParser();
        parser.parse(source, handler);

      } catch (Exception e) {
        log.error(e.getMessage() + " while loading blog enty from " + source.getAbsolutePath(), e);
        e.printStackTrace();
        throw new PersistenceException(e.getMessage());
      }

      return blogEntry;
    } else {
      return null;
    }
  }

  /**
   * Loads all blog entries.
   *
   * @param blog the Blog to load all entries for
   * @return a List of BlogEntry objects
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the blog entries cannot be loaded
   */
  public Collection<BlogEntry> loadBlogEntries(Blog blog) throws PersistenceException {
    List<BlogEntry> list = new ArrayList<BlogEntry>();

    File root = new File(blog.getRoot());
    File years[] = root.listFiles(new FourDigitFilenameFilter());
    for (File year : years) {
      File months[] = year.listFiles(new TwoDigitFilenameFilter());
      for (File month : months) {
        File days[] = month.listFiles(new TwoDigitFilenameFilter());
        for (File day : days) {
          File blogEntryFiles[] = day.listFiles(new BlogEntryFilenameFilter());
          for (File blogEntryFile : blogEntryFiles) {
            list.add(loadBlogEntry(blog, blogEntryFile));
          }
        }
      }
    }

    return list;
  }

  /**
   * Stores the specified blog entry.
   *
   * @param blogEntry the blog entry to store
   * @throws PersistenceException if something goes wrong storing the entry
   */
  public void storeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    File outputDir = new File(getPath(blogEntry.getBlog(), blogEntry.getId(), GMT));
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
   * @throws PersistenceException if something goes wrong storing the entry
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
      Element timeZoneNode = doc.createElement("timeZone");
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
      root.appendChild(timeZoneNode);
      root.appendChild(stateNode);
      root.appendChild(authorNode);
      root.appendChild(staticNameNode);

      if (blogEntry.isAggregated()) {
        Element permalinkNode = doc.createElement("originalPermalink");
        permalinkNode.appendChild(createTextNode(doc, blogEntry.getOriginalPermalink()));
        root.appendChild(permalinkNode);
      }

      titleNode.appendChild(createTextNode(doc, blogEntry.getTitle()));
      subtitleNode.appendChild(createTextNode(doc, blogEntry.getSubtitle()));
      bodyNode.appendChild(createCDATASection(doc, blogEntry.getBody()));

      if (blogEntry.getExcerpt() != null) {
        excerptNode.appendChild(createCDATASection(doc, blogEntry.getExcerpt()));
      }

      root.appendChild(commentsEnabledNode);
      commentsEnabledNode.appendChild(createTextNode(doc, "" + blogEntry.isCommentsEnabled()));

      root.appendChild(trackBacksEnabledNode);
      trackBacksEnabledNode.appendChild(createTextNode(doc, "" + blogEntry.isTrackBacksEnabled()));

      Iterator it = blogEntry.getCategories().iterator();
      Category category;
      while (it.hasNext()) {
        category = (Category) it.next();
        categoryNode = doc.createElement("category");
        categoryNode.appendChild(createTextNode(doc, category.getId()));
        root.appendChild(categoryNode);
      }

      if (blogEntry.getTags() != null) {
        root.appendChild(tagsNode);
        tagsNode.appendChild(createTextNode(doc, blogEntry.getTags()));
      }

      if (blogEntry.getAuthor() != null) {
        authorNode.appendChild(createTextNode(doc, blogEntry.getAuthor()));
      }

      SimpleDateFormat sdf = new SimpleDateFormat(NEW_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
      sdf.setTimeZone(GMT);
      dateNode.appendChild(createTextNode(doc, sdf.format(blogEntry.getDate())));

      timeZoneNode.appendChild(createTextNode(doc, blogEntry.getTimeZoneId()));

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
      bw.write(sw.toString());
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
    Element avatarNode = doc.createElement("avatar");
    Element ipAddressNode = doc.createElement("ipAddress");
    Element dateNode = doc.createElement("date");
    Element parentNode = doc.createElement("parent");
    Element stateNode = doc.createElement("state");
    Element authenticatedNode = doc.createElement("authenticated");

    commentNode.appendChild(titleNode);
    commentNode.appendChild(bodyNode);
    commentNode.appendChild(authorNode);
    commentNode.appendChild(emailNode);
    commentNode.appendChild(websiteNode);
    commentNode.appendChild(avatarNode);
    commentNode.appendChild(ipAddressNode);
    commentNode.appendChild(dateNode);
    commentNode.appendChild(stateNode);
    commentNode.appendChild(authenticatedNode);

    titleNode.appendChild(createTextNode(doc, comment.getTitle()));
    bodyNode.appendChild(createCDATASection(doc, comment.getBody()));
    authorNode.appendChild(createTextNode(doc, comment.getAuthor()));
    emailNode.appendChild(createTextNode(doc, comment.getEmail()));
    websiteNode.appendChild(createTextNode(doc, comment.getWebsite()));
    avatarNode.appendChild(createTextNode(doc, comment.getAvatar()));
    ipAddressNode.appendChild(createTextNode(doc, comment.getIpAddress()));
    SimpleDateFormat sdf = new SimpleDateFormat(NEW_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
    sdf.setTimeZone(GMT);
    dateNode.appendChild(createTextNode(doc, sdf.format(comment.getDate())));
    stateNode.appendChild(createTextNode(doc, comment.getState().getName()));
    authenticatedNode.appendChild(createTextNode(doc, "" + comment.isAuthenticated()));

    if (comment.getParent() != null) {
      commentNode.appendChild(parentNode);
      parentNode.appendChild(createTextNode(doc, "" + comment.getParent().getId()));
    }
  }

  private Node createCDATASection(Document doc, String text) {
    if (text != null) {
      return doc.createCDATASection(XmlStringFilter.filter(text));
    } else {
      return doc.createCDATASection("");
    }
  }

  private Node createTextNode(Document doc, String text) {
    if (text != null) {
      return doc.createTextNode(XmlStringFilter.filter(text));
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
    sdf.setTimeZone(GMT);
    dateNode.appendChild(createTextNode(doc, sdf.format(trackBack.getDate())));
    stateNode.appendChild(createTextNode(doc, trackBack.getState().getName()));
  }

  /**
   * Removes the specified blog entry.
   *
   * @param blogEntry the blog entry to remove
   * @throws PersistenceException if something goes wrong removing the entry
   */
  public void removeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    File path = new File(getPath(blogEntry.getBlog(), blogEntry.getId(), GMT));
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
   * @param blogEntryId   the ID of the blog entry
   * @return  a String of the form blogroot/yyyy/MM/dd
   */
  public String getPath(Blog blog, String blogEntryId, TimeZone timeZone) {
    DateFormat year = new SimpleDateFormat("yyyy");
    year.setTimeZone(timeZone);
    DateFormat month = new SimpleDateFormat("MM");
    month.setTimeZone(timeZone);
    DateFormat day = new SimpleDateFormat("dd");
    day.setTimeZone(timeZone);

    long dateInMillis = Long.parseLong(blogEntryId);
    Date date = new Date(dateInMillis);

    StringBuffer buf = new StringBuffer();
    buf.append(blog.getRoot());
    buf.append(File.separator);
    buf.append(year.format(date));
    buf.append(File.separator);
    buf.append(month.format(date));
    buf.append(File.separator);
    buf.append(day.format(date));

    return buf.toString();
  }

}