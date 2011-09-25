package net.sourceforge.pebble.dao.orient.model;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.sourceforge.pebble.domain.*;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OrientDB object for BlogEntry
 */
public class OrientBlogEntry {
  @Id
  private Object internalId;
  private String id;
  private String title;
  private String subtitle;
  private String excerpt;
  private String body;
  private Date date;
  private String timeZone;
  private String state;
  private String author;
  private String originalPermalink;
  private List<String> categories;
  private List<String> tags;
  private boolean commentsEnabled;
  private boolean trackbacksEnabled;
  private OrientAttachment attachment;
  private List<OrientComment> comments;
  private List<OrientTrackBack> trackBacks;

  private OrientBlogEntry() {
  }

  public OrientBlogEntry(BlogEntry blogEntry) {
    populateFrom(blogEntry);
  }

  public void populateFrom(BlogEntry blogEntry) {
    id = blogEntry.getId();
    title = blogEntry.getTitle();
    subtitle = blogEntry.getSubtitle();
    excerpt = blogEntry.getExcerpt();
    body = blogEntry.getBody();
    date = blogEntry.getDate();
    timeZone = blogEntry.getTimeZoneId();
    state = blogEntry.getState().getName();
    author = blogEntry.getAuthor();
    originalPermalink = blogEntry.getOriginalPermalink();
    categories = new ArrayList<String>();
    for (Category category : blogEntry.getCategories()) {
      categories.add(category.getId());
    }
    tags = new ArrayList<String>();
    for (Tag tag : blogEntry.getTagsAsList()) {
      tags.add(tag.getName());
    }
    commentsEnabled = blogEntry.isCommentsEnabled();
    trackbacksEnabled = blogEntry.isTrackBacksEnabled();
    if (blogEntry.getAttachment() != null) {
      attachment = new OrientAttachment(blogEntry.getAttachment());
    }
    comments = new ArrayList<OrientComment>();
    for (Comment comment : blogEntry.getComments()) {
      comments.add(new OrientComment(comment));
    }
    trackBacks = new ArrayList<OrientTrackBack>();
    for (TrackBack trackBack : blogEntry.getTrackBacks()) {
      trackBacks.add(new OrientTrackBack(trackBack));
    }
  }

  public BlogEntry toBlogEntry(final Blog blog) {
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setTitle(title);
    blogEntry.setSubtitle(subtitle);
    blogEntry.setExcerpt(excerpt);
    blogEntry.setBody(body);
    blogEntry.setDate(date);
    blogEntry.setTimeZoneId(timeZone);
    if (state.equals(State.UNPUBLISHED.getName())) {
      blogEntry.setPublished(false);
    } else {
      blogEntry.setPublished(true);
    }
    blogEntry.setAuthor(author);
    blogEntry.setOriginalPermalink(originalPermalink);
    if (categories != null) {
      for (String category : categories) {
        blogEntry.addCategory(blog.getCategory(category));
      }
    }
    if (tags != null) {
      blogEntry.setTags(Tag.format(Lists.transform(tags, new Function<String, Tag>() {
        public Tag apply(String tag) {
          return blog.getTag(tag);
        }
      })));
    }
    blogEntry.setCommentsEnabled(commentsEnabled);
    blogEntry.setTrackBacksEnabled(trackbacksEnabled);
    if (attachment != null) {
      blogEntry.setAttachment(attachment.toAttachment());
    }
    if (comments != null) {
      for (OrientComment comment : comments) {
        blogEntry.addComment(comment.toComment(blogEntry));
      }
    }
    if (trackBacks != null) {
      for (OrientTrackBack trackBack : trackBacks) {
        blogEntry.addTrackBack(trackBack.toTrackBack(blogEntry));
      }
    }
    return blogEntry;
  }
}
