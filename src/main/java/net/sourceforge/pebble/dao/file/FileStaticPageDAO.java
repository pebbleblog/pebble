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

import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.util.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

public class FileStaticPageDAO implements StaticPageDAO {

  /**
   * the log used by this class
   */
  private static Log log = LogFactory.getLog(FileStaticPageDAO.class);

  private static final String STATIC_PAGES_DIRECTORY_NAME = "pages";
  private static final String STATIC_PAGE_FILE_EXTENSION = ".xml";
  private static final String STATIC_PAGE_LOCK_EXTENSION = ".lock";

  private JAXBContext jaxbContext;

  public FileStaticPageDAO() {
    try {
      jaxbContext = JAXBContext.newInstance(getClass().getPackage().getName());
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
   * Loads a static page from the specified file.
   *
   * @param blog      the Blog to which the static page belongs
   * @param source    the File pointing to the source
   * @return    a StaticPage instance
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the static page can't be loaded
   */
  private StaticPage loadStaticPage(Blog blog, File source) throws PersistenceException {
    if (source.exists()) {
      log.debug("Loading static page from " + source.getAbsolutePath());
      StaticPage staticPage = new StaticPage(blog);

      try {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<StaticPageType> controller = (JAXBElement)unmarshaller.unmarshal(source);
        StaticPageType spt = controller.getValue();

        staticPage.setTitle(spt.getTitle());
        staticPage.setSubtitle(spt.getSubtitle());
        staticPage.setBody(spt.getBody());
        staticPage.setTags(spt.getTags());
        staticPage.setAuthor(spt.getAuthor());
        staticPage.setOriginalPermalink(spt.getOriginalPermalink());
        staticPage.setName(spt.getStaticName());
        StaticPageDateConverter converter = new StaticPageDateConverter(staticPage);
        staticPage.setDate(converter.parse(spt.getDate()));
        staticPage.setTemplate(spt.getTemplate());

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
      Marshaller marshaller = jaxbContext.createMarshaller();
      StaticPageType type = new StaticPageType();

      type.setTitle(staticPage.getTitle());
      type.setSubtitle(staticPage.getSubtitle());
      type.setBody(staticPage.getBody());
      type.setTags(staticPage.getTags());
      type.setAuthor(staticPage.getAuthor());
      type.setStaticName(staticPage.getName());
      type.setOriginalPermalink(staticPage.getOriginalPermalink());
      type.setState(ContentState.PUBLISHED);
      type.setTitle(staticPage.getTitle());
      type.setTemplate(staticPage.getTemplate());

      StaticPageDateConverter converter = new StaticPageDateConverter(staticPage);
      type.setDate(converter.format(staticPage.getDate()));

      // now take a backup of the correct file
      if (destination.exists() && destination.length() > 0) {
        removeStaticPage(staticPage); // this archives the current version
      }

      log.debug("Saving to " + destination.getAbsolutePath());
      ObjectFactory objectFactory = new ObjectFactory();
      JAXBElement jaxbElement = objectFactory.createStaticPage(type);

      marshaller.setProperty("jaxb.formatted.output", true);
      marshaller.setProperty("jaxb.encoding", staticPage.getBlog().getCharacterEncoding());
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

    if (backupFile.exists()) {
      backupFile.delete();
    }

    log.debug("Archiving current version to " + backupFile.getAbsolutePath());
    boolean success = file.renameTo(backupFile);

    if (!success) {
      throw new PersistenceException("Deletion of " + staticPage.getGuid() + " failed");
    }
  }

  /**
   * Given a blog and static page ID, this method determines the path where
   * that static page is stored.
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
   * Given a static page, this method determines the path where
   * that static page is stored.
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

  class StaticPageDateConverter {

    private SimpleDateFormat dateTimeFormats[];

    StaticPageDateConverter(StaticPage staticPage) {
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

    Date parse(String s) {
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

    String format(Date date) {
      return dateTimeFormats[0].format(date);
    }

  }

}