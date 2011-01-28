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
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.comparator.ResponseByDateComparator;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.trackback.TrackBackTokenManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Represents a blog entry.
 *
 * @author Simon Brown
 */
public class BlogEntry extends PageBasedContent {

  /**
   * the log used by this class
   */
  private static Log log = LogFactory.getLog(BlogEntry.class);

  public static final String EXCERPT_PROPERTY = "excerpt";
  public static final String COMMENTS_ENABLED_PROPERTY = "commentsEnabed";
  public static final String TRACKBACKS_ENABLED_PROPERTY = "trackBacksEnabled";
  public static final String ATTACHMENT_PROPERTY = "attachment";
  public static final String CATEGORIES_PROPERTY = "categories";

  /** the permalink */
  private String permalink;

  /**
   * the category that the blog entry falls into
   */
  private Set categories = new HashSet();

  /**
   * the excerpt of the blog entry
   */
  private String excerpt = "";

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

  /** the attachment for this blog entry, if applicable */
  private Attachment attachment;

  /** the timezone that this entry was posted in */
  private String timeZoneId;

  /**
   * Creates a new blog entry.
   *
   * @param blog    the owning Blog
   */
  public BlogEntry(Blog blog) {
    super(blog);
    setPublished(false);
  }

  /**
   * Sets the title of this blog entry.
   *
   * @param newTitle  the title as a String
   */
  public void setTitle(String newTitle) {
    super.setTitle(newTitle);

    // and cause the permalink to be re-generated
    this.permalink = null;
  }

  /**
   * Gets the category of this blog entry.
   *
   * @return the category as a String
   */
  public Set<Category> getCategories() {
    return new HashSet<Category>(categories);
  }

  /**
   * Gets a list of all tags.
   *
   * @return  a List of tags
   */
  public List<Tag> getAllTags() {
    List<Tag> list = new ArrayList<Tag>();

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

    Collections.sort(list);
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
      return getAllTags().contains(new Tag(s, getBlog()));
    } else {
      return false;
    }
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
      return getBody();
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
    super.setDate(newDate);

    // and cause the permalink to be re-generated
    this.permalink = null;
  }

  /**
   * Gets a permalink for this blog entry that is local to the blog. In other
   * words, it doesn't take into account the original permalink for
   * aggregated content.
   *
   * @return an absolute URL as a String
   */
  public String getLocalPermalink() {
    if (this.permalink == null) {
      String s = getBlog().getPermalinkProvider().getPermalink(this);
      if (s != null && s.length() > 0) {
        this.permalink = getBlog().getUrl() + s.substring(1);
      }
    }

    return permalink;
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
   * Gets the link that blogs can send TrackBacks too.
    */
  public String getTrackBackLink() {
    StringBuffer link = new StringBuffer();
    link.append(getBlog().getUrl());
    link.append("addTrackBack.action?entry=");
    link.append(getId());
    link.append("&token=");
    link.append(TrackBackTokenManager.getInstance().generateToken());

    return link.toString();
  }

  /**
   * Gets a list of all comments and TrackBacks.
   *
   * @return  a List of all Response instances
   */
  public List<Response> getResponses() {
    List<Response> responses = new ArrayList();
    responses.addAll(getComments());
    responses.addAll(getTrackBacks());
    Collections.sort(responses, new ResponseByDateComparator());
    return responses;
  }

  /**
   * Gets a collection of all comments.
   *
   * @return a List of Comment instances
   */
  public List<Comment> getComments() {
    List<Comment> allComments = new ArrayList();
    Iterator it = comments.iterator();
    while (it.hasNext()) {
      allComments.addAll(getComments((Comment)it.next()));
    }

    return allComments;
  }

  private List<Comment> getComments(Comment comment) {
    List<Comment> allComments = new ArrayList();
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
  public List<TrackBack> getTrackBacks() {
    return new ArrayList<TrackBack>(trackBacks);
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
   * Gets the number of responses that have been left for this blog entry.
   *
   * @return the number of responses as a int
   */
  public int getNumberOfResponses() {
    return getResponses().size();
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
  public Comment createComment(String title, String body, String author, String email, String website, String avatar, String ipAddress, Date date, State state) {
    return new Comment(title, body, author, email, website, avatar, ipAddress, date, state, this);
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
  public Comment createComment(String title, String body, String author, String email, String website, String avatar, String ipAddress) {
    Calendar cal = getBlog().getCalendar();
    return createComment(title, body, author, email, website, avatar, ipAddress, cal.getTime(), State.APPROVED);
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
    } else if (existingComment != null) {
      return;
    } else {
      if (comment.getParent() != null) {
        Comment parent = getComment(comment.getParent().getId());
        if (parent != null) {
          parent.addComment(comment);
        } else {
          comments.add(comment);
        }
      } else {
        comments.add(comment);
      }
      comment.setBlogEntry(this);

      if (areEventsEnabled()) {
        addEvent(new CommentEvent(comment, CommentEvent.COMMENT_ADDED));
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
      addEvent(new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_ADDED));
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

      // get all children and delete them
      for (Comment child : comment.getComments()) {
        comment.removeComment(child);
      }

      if (comment.getParent() != null) {
        comment.getParent().removeComment(comment);
      } else {
        comments.remove(comment);
      }

      if (areEventsEnabled()) {
        addEvent(new CommentEvent(comment, CommentEvent.COMMENT_REMOVED));
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
   * Gets the response specified by the guid.
   *
   * @param guid    the response guid
   * @return  a Response object, or null if no response exists
   */
  public Response getResponse(String guid) {
    long id = Long.parseLong(guid.substring(guid.lastIndexOf("/")+1));
    if (guid.startsWith("c")) {
      return getComment(id);
    } else {
      return getTrackBack(id);
    }
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
        addEvent(new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_REMOVED));
      }
    } else {
      log.warn("A TrackBack with id=" + id + " could not be found - " +
          "perhaps it has been removed already.");
    }
  }

  /**
   * Removes the specified comment or TrackBack.
   *
   * @param response    the Response to be removed
   */
  public void removeResponse(Response response) {
    if (response instanceof Comment) {
      removeComment(response.getId());
    } else if (response instanceof TrackBack) {
      removeTrackBack(response.getId());
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

  public void validate(ValidationContext context) {
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
    return getGuid().equals(blogEntry.getGuid());
  }

  public String getGuid() {
    return "blogEntry/" + getBlog().getId() + "/" + getId();
  }

  public int hashCode() {
    return getGuid().hashCode();
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance.
   * @see Cloneable
   */
  public Object clone() {
    BlogEntry entry = new BlogEntry(getBlog());
    entry.setEventsEnabled(false);
    entry.setPersistent(isPersistent());
    entry.setPublished(isPublished());
    entry.setTitle(getTitle());
    entry.setSubtitle(getSubtitle());
    entry.setExcerpt(getExcerpt());
    entry.setBody(getBody());
    entry.setDate(getDate());
    entry.setTimeZoneId(timeZoneId);
    entry.setState(getState());
    entry.setAuthor(getAuthor());
    entry.setOriginalPermalink(getOriginalPermalink());
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

    entry.setTags(getTags());

    // also copy the comments
    it = getComments().iterator();
    while (it.hasNext()) {
      Comment comment = (Comment)it.next();
      Comment clonedComment = (Comment)comment.clone();
      entry.addComment(clonedComment);
    }

    // and TrackBacks
    it = getTrackBacks().iterator();
    while (it.hasNext()) {
      TrackBack trackBack = (TrackBack)it.next();
      TrackBack clonedTrackBack = (TrackBack)trackBack.clone();
      clonedTrackBack.setBlogEntry(entry);
      entry.addTrackBack(clonedTrackBack);
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
    for (Response response : getResponses()) {
      response.setEventsEnabled(b);
    }
  }

  public void clearEvents() {
    super.clearEvents();

    for (Response response : getResponses()) {
      response.clearEvents();
    }
  }

  /**
   * Sets the state of this blog entry.
   */
  void setState(State state) {
    State previousState = getState();
    super.setState(state);

    if (areEventsEnabled()) {
      if (isPublished() && previousState == State.UNPUBLISHED) {
        addEvent(new BlogEntryEvent(this, BlogEntryEvent.BLOG_ENTRY_PUBLISHED));
      } else if (isUnpublished() && previousState == State.PUBLISHED) {
        addEvent(new BlogEntryEvent(this, BlogEntryEvent.BLOG_ENTRY_UNPUBLISHED));
      }
    }
  }

  public String toString() {
    return getGuid() + ":" + super.hashCode();
  }

  public TimeZone getTimeZone() {
    return TimeZone.getTimeZone(getTimeZoneId());
  }

  public String getTimeZoneId() {
    if (this.timeZoneId != null) {
      return timeZoneId;
    } else {
      return getBlog().getTimeZoneId();
    }
  }

  public void setTimeZoneId(String timeZoneId) {
    this.timeZoneId = timeZoneId;
  }

}