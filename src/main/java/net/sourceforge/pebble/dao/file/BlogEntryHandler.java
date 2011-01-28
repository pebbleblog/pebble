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

import net.sourceforge.pebble.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BlogEntryHandler extends DefaultHandler {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(BlogEntryHandler.class);

  private static final int NOT_DEFINED = -1;
  private static final int TITLE = 0;
  private static final int EXCERPT = 1;
  private static final int BODY = 2;
  private static final int DATE = 3;
  private static final int AUTHOR = 4;
  private static final int ORIGINAL_PERMALINK = 5;
  private static final int STATIC_NAME = 6;
  private static final int CATEGORY = 7;
  private static final int COMMENTS_ENABLED = 8;
  private static final int TRACKBACKS_ENABLED = 9;
  private static final int EMAIL = 10;
  private static final int WEBSITE = 11;
  private static final int BLOG_NAME = 12;
  private static final int URL = 13;
  private static final int PARENT = 14;
  private static final int IP_ADDRESS = 15;
  private static final int SIZE = 16;
  private static final int TYPE = 17;
  private static final int STATE = 18;
  private static final int TAGS = 19;
  private static final int SUBTITLE = 20;
  private static final int TIME_ZONE = 21;
  private static final int AUTHENTICATED = 22;
  private static final int AVATAR = 23;

  private static final int IN_BLOG_ENTRY = 100;
  private static final int IN_COMMENT = 101;
  private static final int IN_TRACKBACK = 102;
  private static final int IN_ATTACHMENT = 103;

  private BlogEntry blogEntry;
  private int groupStatus = IN_BLOG_ENTRY;
  private int elementStatus = NOT_DEFINED;
  private SimpleDateFormat dateTimeFormats[];

  private StringBuffer elementContent;

  private String attachmentUrl;
  private String attachmentSize;
  private String attachmentType;

  private String commentTitle;
  private String commentBody;
  private String commentAuthor;
  private String commentWebsite;
  private String commentAvatar;
  private String commentIpAddress;
  private String commentEmail;
  private Date commentDate;
  private long commentParent = -1;
  private State commentState = State.APPROVED;
  private boolean commentAuthenticated = false;

  private String trackBackTitle;
  private String trackBackExcerpt;
  private String trackBackBlogName;
  private String trackBackUrl;
  private String trackBackIpAddress;
  private Date trackBackDate;
  private State trackBackState = State.APPROVED;

  public BlogEntryHandler(BlogEntry blogEntry) {
    this.blogEntry = blogEntry;

    // create all date/time formats, for backwards compatibility
    SimpleDateFormat format;
    dateTimeFormats = new SimpleDateFormat[6];

    format = new SimpleDateFormat(FileBlogEntryDAO.NEW_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
    format.setTimeZone(blogEntry.getBlog().getTimeZone());
    dateTimeFormats[0] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.NEW_PERSISTENT_DATETIME_FORMAT, blogEntry.getBlog().getLocale());
    format.setTimeZone(blogEntry.getBlog().getTimeZone());
    dateTimeFormats[1] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.NEW_PERSISTENT_DATETIME_FORMAT);
    format.setTimeZone(blogEntry.getBlog().getTimeZone());
    dateTimeFormats[2] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.OLD_PERSISTENT_DATETIME_FORMAT, Locale.ENGLISH);
    format.setTimeZone(blogEntry.getBlog().getTimeZone());
    dateTimeFormats[3] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.OLD_PERSISTENT_DATETIME_FORMAT, blogEntry.getBlog().getLocale());
    format.setTimeZone(blogEntry.getBlog().getTimeZone());
    dateTimeFormats[4] = format;

    format = new SimpleDateFormat(FileBlogEntryDAO.OLD_PERSISTENT_DATETIME_FORMAT);
    format.setTimeZone(blogEntry.getBlog().getTimeZone());
    dateTimeFormats[5] = format;
  }

  public void startElement(String uri, String name, String qName, Attributes attributes) throws SAXException {
    //log.info("startElement : " + name);
    elementContent = new StringBuffer();
    if (name.equals("title")) {
      elementStatus = TITLE;
    } else if (name.equals("subtitle")) {
      elementStatus = SUBTITLE;
    } else if (name.equals("excerpt")) {
      elementStatus = EXCERPT;
    } else if (name.equals("body")) {
      elementStatus = BODY;
    } else if (name.equals("date")) {
      elementStatus = DATE;
    } else if (name.equals("timeZone")) {
      elementStatus = TIME_ZONE;
    } else if (name.equals("author")) {
      elementStatus = AUTHOR;
    } else if (name.equals("originalPermalink")) {
      elementStatus = ORIGINAL_PERMALINK;
    } else if (name.equals("staticName")) {
      elementStatus = STATIC_NAME;
    } else if (name.equals("category")) {
      elementStatus = CATEGORY;
    } else if (name.equals("tags")) {
      elementStatus = TAGS;
    } else if (name.equals("commentsEnabled")) {
      elementStatus = COMMENTS_ENABLED;
    } else if (name.equals("trackBacksEnabled")) {
      elementStatus = TRACKBACKS_ENABLED;
    } else if (name.equals("email")) {
      elementStatus = EMAIL;
    } else if (name.equals("website")) {
      elementStatus = WEBSITE;
    } else if (name.equals("avatar")) {
      elementStatus = AVATAR;
    } else if (name.equals("ipAddress")) {
      elementStatus = IP_ADDRESS;
    } else if (name.equals("authenticated")) {
      elementStatus = AUTHENTICATED;
    } else if (name.equals("blogName")) {
      elementStatus = BLOG_NAME;
    } else if (name.equals("url")) {
      elementStatus = URL;
    } else if (name.equals("parent")) {
      elementStatus = PARENT;
    } else if (name.equals("state")) {
      elementStatus = STATE;
    } else if (name.equals("size")) {
      elementStatus = SIZE;
    } else if (name.equals("type")) {
      elementStatus = TYPE;
    } else if (name.equals("attachment")) {
      groupStatus = IN_ATTACHMENT;
      elementStatus = NOT_DEFINED;
    } else if (name.equals("comment")) {
      groupStatus = IN_COMMENT;
      elementStatus = NOT_DEFINED;
    } else if (name.equals("trackback")) {
      groupStatus = IN_TRACKBACK;
      elementStatus = NOT_DEFINED;
    } else {
      elementStatus = NOT_DEFINED;
    }
  }

  public void endElement(String uri, String name, String qName) throws SAXException {
    //log.info("endElement : " + name);
    if (groupStatus == IN_BLOG_ENTRY) {
      switch (elementStatus) {
        case TITLE :
          blogEntry.setTitle(elementContent.toString());
          break;
        case SUBTITLE :
          blogEntry.setSubtitle(elementContent.toString());
          break;
        case EXCERPT :
          blogEntry.setExcerpt(elementContent.toString());
          break;
        case BODY :
          blogEntry.setBody(elementContent.toString());
          break;
        case DATE :
          blogEntry.setDate(getDate(elementContent.toString()));
          break;
        case TIME_ZONE :
          blogEntry.setTimeZoneId(elementContent.toString());
          break;
        case STATE :
          if (elementContent.toString().equals(State.UNPUBLISHED.getName())) {
            blogEntry.setPublished(false);
          } else {
            blogEntry.setPublished(true);
          }
          break;
        case AUTHOR :
          blogEntry.setAuthor(elementContent.toString());
          break;
        case ORIGINAL_PERMALINK :
          blogEntry.setOriginalPermalink(elementContent.toString());
          break;
        case CATEGORY :
          blogEntry.addCategory(blogEntry.getBlog().getCategory(elementContent.toString()));
          break;
        case TAGS :
          blogEntry.setTags(elementContent.toString());
          break;
        case COMMENTS_ENABLED :
          blogEntry.setCommentsEnabled(Boolean.valueOf(elementContent.toString()).booleanValue());
          break;
        case TRACKBACKS_ENABLED :
          blogEntry.setTrackBacksEnabled(Boolean.valueOf(elementContent.toString()).booleanValue());
          break;
      }
    } else if (groupStatus == IN_ATTACHMENT && name.equals("attachment")) {
      Attachment attachment = new Attachment();
      attachment.setUrl(attachmentUrl);
      attachment.setSize(Long.parseLong(attachmentSize));
      attachment.setType(attachmentType);
      blogEntry.setAttachment(attachment);
      groupStatus = IN_BLOG_ENTRY;

      attachmentUrl = null;
      attachmentSize = null;
      attachmentType = null;
    } else if (groupStatus == IN_ATTACHMENT) {
      switch (elementStatus) {
        case URL :
          attachmentUrl = elementContent.toString();
          break;
        case SIZE :
          attachmentSize = elementContent.toString();
          break;
        case TYPE :
          attachmentType = elementContent.toString();
          break;
      }
    } else if (groupStatus == IN_COMMENT && name.equals("comment")) {
      Comment comment = blogEntry.createComment(commentTitle, commentBody, commentAuthor, commentEmail, commentWebsite, commentAvatar, commentIpAddress, commentDate, commentState);
      if (commentParent != -1) {
        comment.setParent(blogEntry.getComment(commentParent));
      }
      comment.setAuthenticated(commentAuthenticated);
      blogEntry.addComment(comment);
      groupStatus = IN_BLOG_ENTRY;

      // and blank all the comment variables
      commentTitle = null;
      commentBody = null;
      commentAuthor = null;
      commentWebsite = null;
      commentAvatar = null;
      commentIpAddress = null;
      commentEmail = null;
      commentDate = null;
      commentParent = -1;
      commentState = State.APPROVED;
      commentAuthenticated = false;
    } else if (groupStatus == IN_COMMENT) {
      switch (elementStatus) {
        case TITLE :
          commentTitle = elementContent.toString();
          break;
        case BODY :
          commentBody = elementContent.toString();
          break;
        case DATE :
          commentDate = getDate(elementContent.toString());
          break;
        case AUTHOR :
          commentAuthor = elementContent.toString();
          break;
        case EMAIL :
          commentEmail = elementContent.toString();
          break;
        case WEBSITE :
          commentWebsite = elementContent.toString();
          break;
        case AVATAR :
          commentAvatar = elementContent.toString();
          break;
        case IP_ADDRESS :
          commentIpAddress = elementContent.toString();
          break;
        case PARENT :
          commentParent = Long.parseLong(elementContent.toString());
          break;
        case STATE :
          commentState = State.getState(elementContent.toString());
          break;
        case AUTHENTICATED :
          commentAuthenticated = Boolean.parseBoolean(elementContent.toString());
          break;
      }
    } else if (groupStatus == IN_TRACKBACK && name.equals("trackback")) {
      TrackBack trackBack = blogEntry.createTrackBack(trackBackTitle, trackBackExcerpt, trackBackUrl, trackBackBlogName, trackBackIpAddress, trackBackDate, trackBackState);
      blogEntry.addTrackBack(trackBack);
      groupStatus = IN_BLOG_ENTRY;

      // and blank all the TrackBack variables
      trackBackTitle = null;
      trackBackExcerpt = null;
      trackBackBlogName = null;
      trackBackUrl = null;
      trackBackIpAddress = null;
      trackBackDate = null;
      trackBackState = State.APPROVED;
    } else if (groupStatus == IN_TRACKBACK) {
      switch (elementStatus) {
        case TITLE :
          trackBackTitle = elementContent.toString();
          break;
        case EXCERPT :
          trackBackExcerpt = elementContent.toString();
          break;
        case DATE :
          trackBackDate = getDate(elementContent.toString());
          break;
        case BLOG_NAME :
          trackBackBlogName = elementContent.toString();
          break;
        case URL :
          trackBackUrl = elementContent.toString();
          break;
        case IP_ADDRESS :
          trackBackIpAddress = elementContent.toString();
          break;
        case STATE :
          trackBackState = State.getState(elementContent.toString());
          break;
      }
    }

    elementStatus = NOT_DEFINED;
  }

  public void characters(char ch[], int start, int length) throws SAXException {
    elementContent.append(new String(ch, start, length));
    //log.info("characters : " + s);
  }

  public void warning(SAXParseException e) throws SAXException {
    log.warn("Exception encountered", e);
  }

  public void error(SAXParseException e) throws SAXException {
    log.error("Exception encountered", e);
  }

  public void fatalError(SAXParseException e) throws SAXException {
    log.fatal("Exception encountered", e);
  }

  private Date getDate(String s) {
    for (int i = 0; i < dateTimeFormats.length; i++) {
      try {
        return dateTimeFormats[i].parse(s);
      } catch (ParseException pe) {
      }
    }

    log.error("Could not parse date of " + s);
    return null;
  }

}