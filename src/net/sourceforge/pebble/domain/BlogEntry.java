/*
 * Copyright (c) 2003-2006, Simon Brown
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
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.event.comment.CommentEvent;
import net.sourceforge.pebble.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.index.SearchIndex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.comparator.BlogEntryResponseByDateComparator;
import net.sourceforge.pebble.security.PebbleUserDetailsService;
import net.sourceforge.pebble.security.PebbleUserDetails;

import java.util.*;

/**
 * Represents a blog entry.
 *
 * @author Simon Brown
 */
public class BlogEntry extends Content {

  /**
   * the log used by this class
   */
  private static Log log = LogFactory.getLog(BlogEntry.class);

  public static final String TITLE_PROPERTY = "title";
  public static final String SUBTITLE_PROPERTY = "subtitle";
  public static final String BODY_PROPERTY = "body";
  public static final String EXCERPT_PROPERTY = "excerpt";
  public static final String AUTHOR_PROPERTY = "author";
  public static final String COMMENTS_ENABLED_PROPERTY = "commentsEnabed";
  public static final String TRACKBACKS_ENABLED_PROPERTY = "trackBacksEnabled";
  public static final String ATTACHMENT_PROPERTY = "attachment";
  public static final String DATE_PROPERTY = "date";
  public static final String ORIGINAL_PERMALINK_PROPERTY = "originalPermalink";
  public static final String CATEGORIES_PROPERTY = "categories";
  public static final String TAGS_PROPERTY = "tags";

  /**
   * the id of the blog entry
   */
  private String id;

  /** the permalink */
  private String permalink;

  /**
   * the title of the blog entry
   */
  private String title = "";

  /**
   * the subtitle of the blog entry
   */
  private String subtitle = "";

  /**
   * the category that the blog entry falls into
   */
  private Set categories = new HashSet();

  /** the list of tags for this blog entry */
  private String tags = "";

  /** the List of tags for this blog entry */
  private List tagsAsList = new ArrayList();

  /**
   * the excerpt of the blog entry
   */
  private String excerpt = "";

  /**
   * the body/content of the blog entry
   */
  private String body = "";

  /**
   * the date that the entry was created
   */
  private Date date;

  /** the timezone where the entry was posted */
  private TimeZone timeZone;

  /**
   * the author of the blog entry
   */
  private String author = "";

  /** the enricher user details */
  private PebbleUserDetails user;

  /**
   * a flag to indicate whether comments are enabled for this entry
   */
  private boolean commentsEnabled = true;

  /**
   * a flag to indicate whether TrackBacks are enabled for this entry
   */
  private boolean trackBacksEnabled = true;

  /**
   * the collection of comments for the blog entry
   */
  private List comments = new ArrayList();

  /**
   * the collection of trackbacks for the blog entry
   */
  private List trackBacks = new ArrayList();

  /**
   * the alternative permalink for this blog entry
   */
  private String originalPermalink;

  /** the attachment for this blog entry, if applicable */
  private Attachment attachment;

  /** the owning blog */
  private Blog blog;

  public static final int PUBLISHED = 0;
  public static final int NEW = 1;
  public static final int DRAFT = 2;
  public static final int TEMPLATE = 4;
  public static final int STATIC_PAGE = 8;
  private int type = PUBLISHED;

  /**
   * Creates a new blog entry.
   *
   * @param blog    the owning Blog
   */
  public BlogEntry(Blog blog) {
    this.blog = blog;
    setDate(new Date());
    this.type = NEW;
  }

  /**
   * Gets the unique id of this blog entry.
   *
   * @return the id as a String
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the title of this blog entry.
   *
   * @return the title as a String
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of this blog entry.
   *
   * @param newTitle  the title as a String
   */
  public void setTitle(String newTitle) {
    propertyChangeSupport.firePropertyChange(TITLE_PROPERTY, title, newTitle);

    this.title = newTitle;

    // and cause the permalink to be re-generated
    this.permalink = null;
  }

  /**
   * Gets the subtitle of this blog entry.
   *
   * @return the subtitle as a String
   */
  public String getSubtitle() {
    return subtitle;
  }

  /**
   * Sets the subtitle of this blog entry.
   *
   * @param newSubtitle  the subtitle as a String
   */
  public void setSubtitle(String newSubtitle) {
    propertyChangeSupport.firePropertyChange(SUBTITLE_PROPERTY, subtitle, newSubtitle);

    this.subtitle = newSubtitle;
  }

  /**
   * Gets the category of this blog entry.
   *
   * @return the category as a String
   */
  public Set getCategories() {
    return new HashSet(categories);
  }

  /**
   * Gets a list of all tags.
   *
   * @return  a List of tags
   */
  public List getAllTags() {
    List list = new ArrayList();

    if (getCategories().size() > 0) {
      Iterator it = getCategories().iterator();
      while (it.hasNext()) {
        Category category = (Category)it.next();
        List tagsForCategory = category.getAllTags();
        Collections.reverse(tagsForCategory);
        Iterator jt = tagsForCategory.iterator();
        while (jt.hasNext()) {
          Tag tag = (Tag)jt.next();
          if (!list.contains(tag)) {
            list.add(tag);
          }
        }
      }
    } else {
      List tagsForCategory = getBlog().getRootCategory().getAllTags();
      Iterator it = tagsForCategory.iterator();
      while (it.hasNext()) {
        Tag tag = (Tag)it.next();
        if (!list.contains(tag)) {
          list.add(tag);
        }
      }
    }

    Iterator it = getTagsAsList().iterator();
    while (it.hasNext()) {
      Tag tag = (Tag)it.next();
      if (!list.contains(tag)) {
        list.add(tag);
      }
    }

    Collections.reverse(list);
    return list;
  }

  /**
   * Sets the category of this blog entry.
   *
   * @param category the category as a String
   */
  public synchronized void addCategory(Category category) {
    if (category != null && !categories.contains(category)) {
      Set oldCategories = new HashSet(categories);
      categories.add(category);
      Set newCategories = new HashSet(categories);
      propertyChangeSupport.firePropertyChange(CATEGORIES_PROPERTY, oldCategories, newCategories);
    }
  }

  /**
   * Removes all categories from this blog entry.
   */
  public synchronized void removeAllCategories() {
    propertyChangeSupport.firePropertyChange(CATEGORIES_PROPERTY, new HashSet(categories), new HashSet());
    categories.clear();
  }

  /**
   * Sets the categories for this blog entry.
   *
   * @param newCategories   a Collection of Category instances
   */
  public synchronized void setCategories(Collection newCategories) {
    if (newCategories != null) {
      Set oldCategories = new HashSet(categories);
      categories.clear();
      Iterator it = newCategories.iterator();
      while (it.hasNext()) {
        categories.add(it.next());
      }
      propertyChangeSupport.firePropertyChange(CATEGORIES_PROPERTY, oldCategories, new HashSet(newCategories));
    }
  }

  /**
   * Determines whether this blog entry is in the specified category.
   *
   * @param category a Category instance
   * @return true if this entry is in the specified category,
   *         false otherwise
   */
  public boolean inCategory(Category category) {
    if (category != null) {
      Iterator it = categories.iterator();
      while (it.hasNext()) {
        Category c = (Category)it.next();
        if (c.equals(category) || c.hasParent(category)) {
          return true;
        }
      }

      return false;
    } else {
      return true;
    }
  }

  /**
   * Determines whether this blog entry has the specified tag.
   *
   * @param s   a String
   * @return true if this entry has the specified tag,
   *         false otherwise
   */
  public boolean hasTag(String s) {
    if (s != null) {
      Tag tag = getBlog().getTag(s);
      return getAllTags().contains(tag);
    } else {
      return false;
    }
  }

  /**
   * Gets the tags associated with this category.
   *
   * @return  a list of tags
   */
  public String getTags() {
    return this.tags;
  }

  /**
   * Gets the tags associated with this category, as a List.
   *
   * @return  a List of tags
   */
  public List getTagsAsList() {
    return this.tagsAsList;
  }

  /**
   * Sets the set of tags associated with this category.
   *
   * @param newTags    a set of tags
   */
  public void setTags(String newTags) {
    if (newTags != null && newTags.indexOf(",") > -1) {
      // if the tags have been comma separated, convert them to
      // whitespace separated by
      // - remove whitespace
      // - convert commas to whitespace
      newTags = newTags.replaceAll(" ", "").replaceAll(",", " ");
    }
    propertyChangeSupport.firePropertyChange(TAGS_PROPERTY, tags, newTags);
    this.tags = newTags;
    this.tagsAsList = Tag.parse(getBlog(), tags);
  }


  /**
   * Gets the body of this blog entry.
   *
   * @return the body as a String
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets the content of this response.
   *
   * @return a String
   */
  public String getContent() {
    if (excerpt != null && excerpt.length() > 0) {
      return excerpt;
    } else {
      return body;
    }
  }

  /**
   * Gets the excerpt of this blog entry.
   *
   * @return the excerpt as a String
   */
  public String getExcerpt() {
    return excerpt;
  }

  /**
   * Gets an excerpt of this blog entry, by truncating the body to a maximum
   * of 255 characters.
   *
   * @deprecated
   * @return the first 255 characters of the body, chopped to 252 + ...
   *         if the length of the body is greater then 255
   */
  public String getExcerptFromBody() {
    return getTruncatedContent();
  }

  /**
   * Sets the excerpt of this blog entry.
   *
   * @param newExcerpt    the excerpt as a String
   */
  public void setExcerpt(String newExcerpt) {
    if (newExcerpt != null) {
      newExcerpt = newExcerpt.trim();
    }
    propertyChangeSupport.firePropertyChange(EXCERPT_PROPERTY, excerpt, newExcerpt);
    this.excerpt = newExcerpt;
  }

  /**
   * Sets the body of this blog entry.
   *
   * @param newBody the body as a String
   */
  public void setBody(String newBody) {
    propertyChangeSupport.firePropertyChange(BODY_PROPERTY, body, newBody);
    this.body = newBody;
  }

  /**
   * Gets the date that this blog entry was created.
   *
   * @return a java.util.Date instance
   */
  public Date getDate() {
    return date;
  }

  /**
   * Gets the date that this blog entry was last updated.
   *
   * @return  a Date instance representing the time of the last comment/TrackBack
   */
  public Date getLastModified() {
    Date date = getDate();

    Iterator it = comments.iterator();
    while (it.hasNext()) {
      Comment comment = (Comment)it.next();
      if (comment.getDate().after(date)) {
        date = comment.getDate();
      }
    }

    it = trackBacks.iterator();
    while (it.hasNext()) {
      TrackBack trackBack = (TrackBack)it.next();
      if (trackBack.getDate().after(date)) {
        date = trackBack.getDate();
      }
    }

    return date;
  }

  /**
   * Sets the date that this blog entry was created.
   *
   * @param newDate a java.util.Date instance
   */
  public void setDate(Date newDate) {
    propertyChangeSupport.firePropertyChange(DATE_PROPERTY, date, newDate);
    //Calendar cal = getBlog().getCalendar();
    //cal.setTime(newDate);
    //this.date = cal.getTime();
    this.date = newDate;
    this.id = "" + this.date.getTime();

    // and cause the permalink to be re-generated
    this.permalink = null;
  }

  /**
   * Gets the timezone of this blog entry.
   *
   * @return  a TimeZone instance of this entry, or the timezone instance
   *          of the owning blog as a default
   */
  public TimeZone getTimeZone() {
    if (this.timeZone != null) {
      return this.timeZone;
    } else {
      return getBlog().getTimeZone();
    }
  }

  /**
   * Sets the timezone of this blog entry.
   *
   * @param timeZone    a TimeZone instance
   */
  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * Gets the author of this blog entry.
   *
   * @return the author as a String
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Gets full user details about the author including name, email-address, etc.
   *
   * @return  a PebbleUserDetails instance
   */
  public PebbleUserDetails getUser() {
    if (this.user == null) {
      PebbleUserDetailsService puds = new PebbleUserDetailsService();
      puds.setPebbleContext(BlogManager.getInstance().getPebbleContext());
      try {
        this.user = (PebbleUserDetails)puds.loadUserByUsername(getAuthor());
      } catch (Exception e) {
        // do nothing
      }
    }

    return this.user;
  }

  /**
   * Sets the author of this blog entry.
   *
   * @param newAuthor the author as a String
   */
  public void setAuthor(String newAuthor) {
    //propertyChangeSupport.firePropertyChange(AUTHOR_PROPERTY, author, newAuthor);
    this.author = newAuthor;
  }

  /**
   * Determines whether this blog entry has been aggregated from another
   * source. An aggregated blog entry will have a specified permalink.
   *
   * @return true if this blog entry has been aggegrated, false otherwise
   */
  public boolean isAggregated() {
    return (originalPermalink != null);
  }

  /**
   * Gets the alternative permalink for this blog entry.
   *
   * @return an absolute URL as a String
   */
  public String getOriginalPermalink() {
    return this.originalPermalink;
  }

  /**
   * Sets the alternative permalink for this blog entry.
   *
   * @param newPermalink an absolute URL as a String
   */
  public void setOriginalPermalink(String newPermalink) {
    if (newPermalink == null || newPermalink.length() == 0) {
      propertyChangeSupport.firePropertyChange(ORIGINAL_PERMALINK_PROPERTY, originalPermalink, null);
      this.originalPermalink = null;
    } else {
      propertyChangeSupport.firePropertyChange(ORIGINAL_PERMALINK_PROPERTY, originalPermalink, newPermalink);
      this.originalPermalink = newPermalink;
    }
  }

  /**
   * Gets a permalink for this blog entry.
   *
   * @return an absolute URL as a String
   */
  public String getPermalink() {
    if (isAggregated()) {
      return getOriginalPermalink();
    } else {
      return getLocalPermalink();
    }
  }

  /**
   * Gets a permalink for this blog entry that is local to the blog. In other
   * words, it doesn't take into account the original permalink for
   * aggregated content.
   *
   * @return an absolute URL as a String
   */
  public String getLocalPermalink() {
    if (type == BlogEntry.STATIC_PAGE) {
      return getBlog().getUrl() + "pages/" + staticName + ".html";
    } else {
      if (this.permalink == null) {
        String s = getBlog().getPermalinkProvider().getPermalink(this);
        if (s != null && s.length() > 0) {
          this.permalink = getBlog().getUrl() + s.substring(1);
        }
      }

      return permalink;
    }
  }

  /**
   * Gets the attachment associated with this blog entry.
   *
   * @return  an Attachment instance, or null if one doesn't exist
   */
  public Attachment getAttachment() {
    return attachment;
  }

  /**
   * Sets the attachment associated with thie blog entry.
   *
   * @param newAttachment    an Attachment instance
   */
  public void setAttachment(Attachment newAttachment) {
    propertyChangeSupport.firePropertyChange(ATTACHMENT_PROPERTY, attachment, newAttachment);
    this.attachment = newAttachment;
  }

  private String staticName;

  public void setStaticName(String staticName) {
    this.staticName = staticName;
  }

  public String getStaticName() {
    return this.staticName;
  }

  /**
   * Determines whether comments are enabled for this blog entry.
   *
   * @return true if comments are enabled, false otherwise
   */
  public boolean isCommentsEnabled() {
    return this.commentsEnabled;
  }

  /**
   * Sets whether comments are enabled for this blog entry.
   *
   * @param newCommentsEnabled true if comments should be enabled,
   *                        false otherwise
   */
  public void setCommentsEnabled(boolean newCommentsEnabled) {
    propertyChangeSupport.firePropertyChange(COMMENTS_ENABLED_PROPERTY, commentsEnabled, newCommentsEnabled);
    this.commentsEnabled = newCommentsEnabled;
  }

  /**
   * Gets a link to the comments for this blog entry.
   *
   * @return an absolute URL as a String
   */
  public String getCommentsLink() {
    return getLocalPermalink() + "#comments";
  }

  /**
   * Determines whether TrackBacks are enabled for this blog entry.
   *
   * @return true if TrackBacks are enabled, false otherwise
   */
  public boolean isTrackBacksEnabled() {
    return this.trackBacksEnabled;
  }

  /**
   * Sets whether TrackBacks are enabled for this blog entry.
   *
   * @param newTrackBacksEnabled true if TrackBacks should be enabled,
   *                          false otherwise
   */
  public void setTrackBacksEnabled(boolean newTrackBacksEnabled) {
    propertyChangeSupport.firePropertyChange(TRACKBACKS_ENABLED_PROPERTY, trackBacksEnabled, newTrackBacksEnabled);
    this.trackBacksEnabled = newTrackBacksEnabled;
  }

  /**
   * Gets a link to the trackbacks for this blog entry.
   *
   * @return an absolute URL as a String
   */
  public String getTrackBacksLink() {
    return getLocalPermalink() + "#trackbacks";
  }

  /**
   * Helper method to get the owning Blog instance.
   *
   * @return the overall owning Blog instance
   */
  public Blog getBlog() {
    return this.blog;
  }

  /**
   * Gets a list of all comments and TrackBacks.
   *
   * @return  a List of all BlogEntryResponse instances
   */
  public List getResponses() {
    List responses = new ArrayList();
    responses.addAll(getComments());
    responses.addAll(getTrackBacks());
    Collections.sort(responses, new BlogEntryResponseByDateComparator());
    return responses;
  }

  /**
   * Gets a collection of all comments.
   *
   * @return a List of Comment instances
   */
  public List getComments() {
    List allComments = new ArrayList();
    Iterator it = comments.iterator();
    while (it.hasNext()) {
      allComments.addAll(getComments((Comment)it.next()));
    }

    return allComments;
  }

  private List getComments(Comment comment) {
    List allComments = new ArrayList();
    allComments.add(comment);
    Iterator it = comment.getComments().iterator();
    while (it.hasNext()) {
      allComments.addAll(getComments((Comment)it.next()));
    }

    return allComments;
  }

  /**
   * Gets the number of comments that have been left for this blog entry.
   *
   * @return the number of comments as a int
   */
  public int getNumberOfComments() {
    return getComments().size();
  }

  /**
   * Gets a collection of all trackbacks.
   *
   * @return a List of TrackBack instances
   */
  public List getTrackBacks() {
    return Collections.unmodifiableList(trackBacks);
  }

  /**
   * Gets the number of trackbacks that have been left for this blog entry.
   *
   * @return the number of trackbacks as a int
   */
  public int getNumberOfTrackBacks() {
    return trackBacks.size();
  }

  /**
   * Creates a new comment for this blog entry. This method doesn't actually
   * <b>add</b> the comment too.
   *
   * @param title     the title of the comment
   * @param body      the body of the comment
   * @param author    the author of the comment
   * @param email     the author's e-mail address
   * @param website   the author's website
   * @param ipAddress the IP address of the author
   * @param date      the date that the comment was created
   * @param state     the state of the comment
   * @return a new Comment instance with the specified properties
   */
  public Comment createComment(String title, String body, String author, String email, String website, String ipAddress, Date date, State state) {
    return new Comment(title, body, author, email, website, ipAddress, date, state, this);
  }

  /**
   * Creates a new comment for this blog entry, with a creation date of now.
   * This method doesn't actually <b>add</b> the comment too.
   *
   * @param title   the title of the comment
   * @param body    the body of the comment
   * @param author  the author of the comment
   * @param email   the author's e-mail address
   * @param website the author's website
   * @param ipAddress the IP address of the author
   * @return a new Comment instance with the specified properties
   */
  public Comment createComment(String title, String body, String author, String email, String website, String ipAddress) {
    Calendar cal = getBlog().getCalendar();
    return createComment(title, body, author, email, website, ipAddress, cal.getTime(), State.APPROVED);
  }

  /**
   * Adds the specified comment.
   *
   * @param comment a Comment instance
   */
  public synchronized void addComment(Comment comment) {
    if (comment == null) {
      return;
    }

    Comment existingComment = getComment(comment.getId());
    if (existingComment != null && existingComment != comment) {
      // there is an existing comment with the same ID, but it's
      // not the same instance
      comment.setDate(new Date(comment.getDate().getTime() + 1));
      addComment(comment);
    } else if (existingComment != null && existingComment == comment) {
      return;
    } else {
      if (comment.getParent() != null) {
        comment.getParent().addComment(comment);
      } else {
        comments.add(comment);
      }

      if (areEventsEnabled()) {
        getBlog().getEventDispatcher().fireCommentEvent(new CommentEvent(comment, CommentEvent.COMMENT_ADDED));
        comment.setEventsEnabled(true);
      }
    }
  }

  /**
   * Creates a new trackback for this blog entry. This method doesn't actually
   * <b>add</b> the trackback too.
   *
   * @param title    the title of the entry
   * @param excerpt  the excerpt of the entry
   * @param url      the url (permalink) of the entry
   * @param blogName the name of the blog
   * @param ipAddress   the IP address of the author
   * @param date     the date the trackback was received
   * @return a new TrackBack instance with the specified properties
   */
  public TrackBack createTrackBack(String title, String excerpt, String url, String blogName, String ipAddress, Date date, State state) {
    return new TrackBack(title, excerpt, url, blogName, ipAddress, date, state, this);
  }

  /**
   * Creates a new trackback for this blog entry with a date of now.
   * This method doesn't actually <b>add</b> the trackback too.
   *
   * @param title       the title of the entry
   * @param excerpt     the excerpt of the entry
   * @param url         the url (permalink) of the entry
   * @param blogName    the name of the blog
   * @param ipAddress   the IP address of the author
   * @return a new Comment instance with the specified properties
   */
  public TrackBack createTrackBack(String title, String excerpt, String url, String blogName, String ipAddress) {
    Calendar cal = getBlog().getCalendar();
    return createTrackBack(title, excerpt, url, blogName, ipAddress, cal.getTime(), State.APPROVED);
  }

  /**
   * Adds the specified trackback.
   *
   * @param trackBack a TrackBack instance
   */
  public synchronized void addTrackBack(TrackBack trackBack) {
    if (trackBack == null || trackBacks.contains(trackBack)) {
      return;
    }

    trackBacks.add(trackBack);

    if (areEventsEnabled()) {
      getBlog().getEventDispatcher().fireTrackBackEvent(new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_ADDED));
      trackBack.setEventsEnabled(true);
    }
  }

  /**
   * Removes the specified comment.
   *
   * @param id    the id of the comment to be removed
   */
  public synchronized void removeComment(long id) {
    Comment comment = getComment(id);
    if (comment != null) {

      if (comment.getParent() != null) {
        comment.getParent().removeComment(comment);
      } else {
        comments.remove(comment);
      }

      if (areEventsEnabled()) {
        getBlog().getEventDispatcher().fireCommentEvent(new CommentEvent(comment, CommentEvent.COMMENT_REMOVED));
      }
    } else {
      log.warn("A comment with id=" + id + " could not be found - " +
        "perhaps it has been removed already.");
    }
  }

  /**
   * Gets the specified comment.
   *
   * @param id    the id of the comment
   */
  public Comment getComment(long id) {
    Iterator it = getComments().iterator();
    while (it.hasNext()) {
      Comment comment = (Comment) it.next();
      if (comment.getId() == id) {
        return comment;
      }
    }

    return null;
  }

  /**
   * Gets the specified TrackBack.
   *
   * @param id    the id of the TrackBack
   */
  public TrackBack getTrackBack(long id) {
    Iterator it = getTrackBacks().iterator();
    while (it.hasNext()) {
      TrackBack trackBack = (TrackBack)it.next();
      if (trackBack.getId() == id) {
        return trackBack;
      }
    }

    return null;
  }

  /**
   * Removes the specified TrackBack.
   *
   * @param id    the id of the TrackBack to be removed
   */
  public synchronized void removeTrackBack(long id) {
    TrackBack trackBack = getTrackBack(id);
    if (trackBack != null) {
      trackBacks.remove(trackBack);

      if (areEventsEnabled()) {
        getBlog().getEventDispatcher().fireTrackBackEvent(new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_REMOVED));
      }
    } else {
      log.warn("A TrackBack with id=" + id + " could not be found - " +
          "perhaps it has been removed already.");
    }
  }

  /**
   * Removes the specified comment or TrackBack.
   *
   * @param response    the BlogEntryResponse to be removed
   */
  public void removeResponse(BlogEntryResponse response) {
    if (response instanceof Comment) {
      removeComment(response.getId());
    } else if (response instanceof TrackBack) {
      removeTrackBack(response.getId());
    }
  }

  public int getType() {
    return this.type;
  }

  public void setType(int type) {
    this.type = type;
  }

  /**
   * Stores this BlogEntry.
   *
   * @throws BlogException if the blog entry cannot be stored
   */
  public synchronized void store() throws BlogException {
    try {
      log.debug("Storing " + getTitle() + " (" + getId() + ")");
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      BlogEntryDAO dao = factory.getBlogEntryDAO();
      dao.storeBlogEntry(this);

      if (areEventsEnabled() && isDirty()) {
        BlogEntryEvent event = new BlogEntryEvent(this, getPropertyChangeEvents());
        clearPropertyChangeEvents();
        getBlog().getEventDispatcher().fireBlogEntryEvent(event);
      }
    } catch (PersistenceException pe) {
      throw new BlogException(pe.getMessage());
    }
  }

  /**
   * Removes this blog entry from the filing system.
   *
   * @throws BlogException if the file backing this blog entry
   *                       cannot be deleted
   */
  public synchronized void remove() throws BlogException {
    try {
      log.debug("Removing " + getTitle() + " (" + getId() + ")");
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      BlogEntryDAO dao = factory.getBlogEntryDAO();
      dao.removeBlogEntry(this);

      if (type == PUBLISHED || isStaticPage()) {
        // and finally un-index the published entry
        blog.getSearchIndex().removeIndex(this);
      }

      if (isStaticPage()) {
        getBlog().getStaticPageIndex().reindex();
      }
    } catch (PersistenceException pe) {
      throw new BlogException(pe.getMessage());
    }
  }

  public void validate(ValidationContext context) {
    if (isStaticPage()) {
      if (staticName == null || staticName.length() == 0) {
        context.addError("Name cannot be empty");
      } else if (!staticName.matches("[\\w_/-]+")) {
        context.addError("Name \"" + staticName + "\" must contain only A-Za-z0-9_-/");
      }

      BlogEntry page = getBlog().getStaticPageIndex().getStaticPage(staticName);
      if (page != null && !page.equals(this)) {
        context.addError("A page with the name \"" + staticName + "\" already exists");
      }
    }
  }

  /**
   * Returns the blog entry that was posted before this one.
   *
   * @return  a BlogEntry instance, or null if this is the first entry
   */
  public BlogEntry getPreviousBlogEntry() {
    return getBlog().getPreviousBlogEntry(this);
  }

  /**
   * Returns the blog entry that was posted after this one.
   *
   * @return  a BlogEntry instance, or null is this is the last entry
   */
  public BlogEntry getNextBlogEntry() {
    return getBlog().getNextBlogEntry(this);
  }

  /**
   * Determines whether this blog entry in fact represents a static page.
   *
   * @return  true if the type is STATIC_PAGE, false otherwise
   */
  public boolean isStaticPage() {
      return type == STATIC_PAGE;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param o   the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj
   *         argument; <code>false</code> otherwise.
   * @see #hashCode()
   * @see java.util.Hashtable
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof BlogEntry)) {
      return false;
    }

    BlogEntry blogEntry = (BlogEntry)o;
    return
        id.equals(blogEntry.getId()) &&
        getBlog().equals(blogEntry.getBlog());
  }

  public String getGuid() {
    return getBlog().getId() + "/" + getId();
  }

  public int hashCode() {
    return getGuid().hashCode();
  }

  /**
   * Gets a string representation of this object.
   *
   * @return  a String
   */
  public String toString() {
    return getBlog().getId() + "/" + getTitle();
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance.
   * @see Cloneable
   */
  public Object clone() {
    BlogEntry entry = new BlogEntry(blog);
    entry.setTitle(title);
    entry.setSubtitle(subtitle);
    entry.setExcerpt(excerpt);
    entry.setBody(body);
    entry.setDate(date);
    entry.setState(getState());
    entry.setAuthor(author);
    entry.setOriginalPermalink(originalPermalink);
    entry.setStaticName(staticName);
    entry.setType(type);
    entry.setTimeZone(timeZone);
    entry.setCommentsEnabled(commentsEnabled);
    entry.setTrackBacksEnabled(trackBacksEnabled);
    if (attachment != null) {
      entry.setAttachment((Attachment)attachment.clone());
    }

    // copy the categories
    Iterator it = categories.iterator();
    while (it.hasNext()) {
      entry.addCategory((Category)it.next());
    }

    entry.setTags(tags);

    // also copy the comments
    it = comments.iterator();
    while (it.hasNext()) {
      entry.comments.add((Comment)((Comment)it.next()).clone());
    }

    // and TrackBacks
    it = trackBacks.iterator();
    while (it.hasNext()) {
      entry.trackBacks.add((TrackBack)((TrackBack)it.next()).clone());
    }

    return entry;
  }

  /**
   * Sets whether events are enabled.
   *
   * @param b   true to enable events, false otherwise
   */
  void setEventsEnabled(boolean b) {
    super.setEventsEnabled(b);

    // and cascade
    Iterator it = getComments().iterator();
    while (it.hasNext()) {
      Comment comment = (Comment)it.next();
      comment.setEventsEnabled(b);
    }

    it = getTrackBacks().iterator();
    while (it.hasNext()) {
      TrackBack trackBack = (TrackBack)it.next();
      trackBack.setEventsEnabled(b);
    }
  }

  /**
   * Sets the state of this blog entry.
   */
  public void setState(State state) {
    super.setState(state);

    if (areEventsEnabled()) {
      if (state == State.APPROVED) {
        getBlog().getEventDispatcher().fireBlogEntryEvent(new BlogEntryEvent(this, BlogEntryEvent.BLOG_ENTRY_APPROVED));
      } else if (state == State.REJECTED) {
        getBlog().getEventDispatcher().fireBlogEntryEvent(new BlogEntryEvent(this, BlogEntryEvent.BLOG_ENTRY_REJECTED));
      }
    }
  }

}
