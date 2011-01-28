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

import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Tests for the BlogEntry class.
 *
 * @author    Simon Brown
 */
public class BlogEntryTest extends SingleBlogTestCase {

  private BlogEntry blogEntry;

  protected void setUp() throws Exception {
    super.setUp();
    blogEntry = new BlogEntry(blog);
    blogEntry.setTitle("A title");
    blogEntry.setBody("Some body");
    blogEntry.setExcerpt("Some excerpt");
    blogEntry.setAuthor("An author");
    blogEntry.setDate(new Date());
    blogEntry.setEventsEnabled(true);
  }

  /**
   * Tests the construction.
   */
  public void testConstruction() {
    assertNotNull(blogEntry.getId());
    assertEquals("A title", blogEntry.getTitle());
    assertEquals("Some body", blogEntry.getBody());

    assertNotNull(blogEntry.getCategories());
    assertEquals(0, blogEntry.getCategories().size());
    assertTrue(blogEntry.isCommentsEnabled());
    assertNotNull(blogEntry.getComments());
    assertEquals(0, blogEntry.getComments().size());
    assertNotNull(blogEntry.getTrackBacks());
    assertEquals(0, blogEntry.getTrackBacks().size());

    assertNotNull(blogEntry.getDate());
    assertNotNull(blogEntry.getAuthor());
    assertFalse(blogEntry.isAggregated());
    assertNotNull(blogEntry.getTags());
    assertEquals(0, blogEntry.getTagsAsList().size());
  }

  /**
   * Tests that the root blog is setup correctly.
   */
  public void testGetRootBlog() {
    assertEquals(blog, blogEntry.getBlog());
  }

  /**
   * Tests that the root is setup correctly.
   */
  public void testDay() {
    //assertEquals(dailyBlog, blogEntry.getDay());
  }

  /**
   * Tests for the id.
   */
  public void testId() {
    String id = "" + blogEntry.getDate().getTime();
    assertEquals(id, blogEntry.getId());
  }

  /**
   * Tests for the title.
   */
  public void testTitle() {
    blogEntry.setTitle("A new title");
    assertEquals("A new title", blogEntry.getTitle());
  }

  /**
   * Tests for the subtitle.
   */
  public void testSubtitle() {
    blogEntry.setSubtitle("A new subtitle");
    assertEquals("A new subtitle", blogEntry.getSubtitle());
  }

  /**
   * Tests for the body.
   */
  public void testBody() {
    blogEntry.setBody("A new body");
    assertEquals("A new body", blogEntry.getBody());
  }

  /**
   * Tests for the excerpt.
   */
  public void testExcerpt() {
    blogEntry.setExcerpt("An excerpt");
    assertEquals("An excerpt", blogEntry.getExcerpt());
  }

  /**
   * Tests for the categories.
   */
  public void testCategories() {
    assertEquals(0, blogEntry.getCategories().size());

    Category category = new Category("1", "One");
    blogEntry.addCategory(category);
    assertEquals(1, blogEntry.getCategories().size());
    assertTrue(blogEntry.getCategories().contains(category));

    // just check that we can't add null categories!
    blogEntry.addCategory(null);
    assertFalse(blogEntry.getCategories().contains(null));
  }

  /**
   * Tests for the "in category" check.
   */
  public void testInCategory() {
    CategoryBuilder builder = new CategoryBuilder(blog);
    Category apple = new Category("/apple", "Apple");
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    builder.addCategory(apple);
    builder.addCategory(java);
    builder.addCategory(junit);

    blogEntry.addCategory(apple);
    assertTrue(blogEntry.inCategory(apple));
    assertFalse(blogEntry.inCategory(java));
    assertFalse(blogEntry.inCategory(junit));
    assertTrue(blogEntry.inCategory(null));

    blogEntry.addCategory(junit);
    assertTrue(blogEntry.inCategory(apple));
    assertTrue(blogEntry.inCategory(java));
    assertTrue(blogEntry.inCategory(junit));
  }

  /**
   * Tests that all categories can be removed.
   */
  public void testAllCategoriesCanBeRemoved() {
    CategoryBuilder builder = new CategoryBuilder(blog);
    Category apple = new Category("/apple", "Apple");
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    builder.addCategory(apple);
    builder.addCategory(java);
    builder.addCategory(junit);

    blogEntry.addCategory(apple);
    blogEntry.addCategory(junit);

    blogEntry.removeAllCategories();
    assertEquals(0, blogEntry.getCategories().size());
  }

  /**
   * Tests for the tags.
   */
  public void testTagsSeparatedByCommas() {
    blogEntry.setTags("some, tags");
    assertEquals("some tags", blogEntry.getTags());
    List tags = blogEntry.getTagsAsList();
    assertEquals(2, tags.size());
    assertEquals(blog.getTag("some"), tags.get(0));
    assertEquals(blog.getTag("tags"), tags.get(1));
  }

  /**
   * Tests for the tags.
   */
  public void testTagsSeparatedByWhitespace() {
    blogEntry.setTags("some tags");
    assertEquals("some tags", blogEntry.getTags());
    List tags = blogEntry.getTagsAsList();
    assertEquals(2, tags.size());
    assertEquals(blog.getTag("some"), tags.get(0));
    assertEquals(blog.getTag("tags"), tags.get(1));
  }

  /**
   * Tests for the date.
   */
  public void testDate() {
    assertNotNull(blogEntry.getDate());
  }

  /**
   * Tests for the author.
   */
  public void testAuthor() {
    blogEntry.setAuthor("A new author");
    assertEquals("A new author", blogEntry.getAuthor());
  }

  /**
   * Tests the aggregated property.
   */
  public void testAggregated() {
    blogEntry.setOriginalPermalink("http://www.simongbrown.com/blog/2003/04/01.html#a123456789");
    assertTrue(blogEntry.isAggregated());

    blogEntry.setOriginalPermalink(null);
    assertFalse(blogEntry.isAggregated());

    blogEntry.setOriginalPermalink("");
    assertFalse(blogEntry.isAggregated());
  }

  /**
   * Tests the comments enabled property.
   */
  public void testCommentsEnabled() {
    assertTrue(blogEntry.isCommentsEnabled());
    blogEntry.setCommentsEnabled(false);
    assertFalse(blogEntry.isCommentsEnabled());
  }

  /**
   * Tests the links that will refer to a blog entry and its comments.
   */
  public void testLinks() {
    DecimalFormat format = new DecimalFormat("00");
    Calendar cal = blog.getCalendar();
    cal.setTime(blogEntry.getDate());
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    assertEquals("http://www.yourdomain.com/blog/" +
        year + "/" + format.format(month) + "/" + format.format(day) + "/" +
        blogEntry.getId() + ".html", blogEntry.getPermalink());
    assertEquals("http://www.yourdomain.com/blog/" +
        year + "/" + format.format(month) + "/" + format.format(day) + "/" +
        blogEntry.getId() + ".html", blogEntry.getLocalPermalink());
    assertEquals(blogEntry.getLocalPermalink() + "#comments", blogEntry.getCommentsLink());
    assertEquals(blogEntry.getLocalPermalink() + "#trackbacks"  , blogEntry.getTrackBacksLink());
  }

  /**
   * Tests that a blog entry can correctly manage blog comments.
   */
  public void testComments() {
    Comment blogComment1, blogComment2;
    Calendar cal1, cal2;
    cal1 = blog.getCalendar();
    cal1.set(Calendar.DAY_OF_MONTH, 1);
    cal2 = blog.getCalendar();
    cal2.set(Calendar.DAY_OF_MONTH, 2);

    // check that there are no comments to start with
    assertEquals(0, blogEntry.getNumberOfComments());
    assertEquals(0, blogEntry.getComments().size());

    // now create a new comment
    blogComment1 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", cal1.getTime(), State.APPROVED);
    assertNotNull(blogComment1);
    assertEquals("Re: " + blogEntry.getTitle(), blogComment1.getTitle());
    assertEquals("Body", blogComment1.getBody());
    assertEquals("Author", blogComment1.getAuthor());
    assertEquals("http://www.google.com", blogComment1.getWebsite());
    assertNotNull(blogComment1.getDate());
    assertEquals(blogEntry, blogComment1.getBlogEntry());

    // the comment hasn't been added yet
    assertEquals(0, blogEntry.getNumberOfComments());
    assertEquals(0, blogEntry.getComments().size());
    assertFalse(blogEntry.getComments().contains(blogComment1));

    // let's now add the comment
    blogEntry.addComment(blogComment1);
    assertEquals(1, blogEntry.getNumberOfComments());
    assertEquals(1, blogEntry.getComments().size());
    assertTrue(blogEntry.getComments().contains(blogComment1));

    // and now add another comment
    blogComment2 = blogEntry.createComment("Title 2", "Body 2", "Author 2", "me2@somedomain.com", "http://www.yahoo.com", "http://graph.facebook.com/user/picture", "127.0.0.1", cal2.getTime(), State.APPROVED);
    assertEquals("Title 2", blogComment2.getTitle());
    assertEquals(1, blogEntry.getNumberOfComments());
    assertTrue(blogEntry.getComments().contains(blogComment1));
    assertFalse(blogEntry.getComments().contains(blogComment2));
    blogEntry.addComment(blogComment2);
    assertEquals(2, blogEntry.getNumberOfComments());
    assertEquals(2, blogEntry.getComments().size());
    assertTrue(blogEntry.getComments().contains(blogComment1));
    assertTrue(blogEntry.getComments().contains(blogComment2));

    // check that we can't add the same comment more than once
    blogEntry.addComment(blogComment2);
    assertEquals(2, blogEntry.getNumberOfComments());

    // and now delete the comments
    blogEntry.removeComment(blogComment1.getId());
    assertFalse(blogEntry.getComments().contains(blogComment1));
    assertTrue(blogEntry.getComments().contains(blogComment2));
    assertEquals(1, blogEntry.getNumberOfComments());
    blogEntry.removeComment(blogComment2.getId());
    assertFalse(blogEntry.getComments().contains(blogComment1));
    assertFalse(blogEntry.getComments().contains(blogComment2));
    assertEquals(0, blogEntry.getNumberOfComments());
  }

  /**
   * Tests that a blog entry can correctly manage blog comments when two are
   * added at the same time.
   */
  public void testTwoCommentsAddedAtTheSameTime() {
    Comment blogComment1, blogComment2;
    blogComment1 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    blogComment2 = (Comment)blogComment1.clone();
    assertEquals(blogComment1.getId(), blogComment2.getId());
    blogEntry.addComment(blogComment1);
    assertEquals(1, blogEntry.getNumberOfComments());
    assertEquals(1, blogEntry.getComments().size());
    assertTrue(blogEntry.getComments().contains(blogComment1));
    blogEntry.addComment(blogComment2);
    assertEquals(2, blogEntry.getNumberOfComments());
    assertEquals(2, blogEntry.getComments().size());
    assertTrue(blogEntry.getComments().contains(blogComment1));
    assertTrue(blogEntry.getComments().contains(blogComment2));
  }

  /**
   * Tests that nested comments can be accessed.
   */
  public void testNestedComments() {
    Comment comment1, comment2;
    Date date1 = new Date(0);
    Date date2 = new Date(1000);

    comment1 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", date1, State.APPROVED);
    blogEntry.addComment(comment1);

    comment2 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", date2, State.APPROVED);
    comment2.setParent(comment1);
    blogEntry.addComment(comment2);

    assertEquals(2, blogEntry.getComments().size());
    assertEquals(comment1, blogEntry.getComments().get(0));
    assertEquals(comment2, blogEntry.getComments().get(1));

    assertEquals(comment1, blogEntry.getComment(comment1.getId()));
    assertEquals(comment2, blogEntry.getComment(comment2.getId()));
  }

  /**
   * Tests that the number of comments reported is correct when some of those
   * comments are nested.
   */
  public void testNumberOfCommentsCorrectWhenNestedCommentsPresent() {
    Comment comment1, comment2;
    Calendar cal1, cal2;
    cal1 = blog.getCalendar();
    cal1.set(Calendar.DAY_OF_MONTH, 1);
    cal2 = blog.getCalendar();
    cal2.set(Calendar.DAY_OF_MONTH, 2);

    comment1 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", cal1.getTime(), State.APPROVED);
    blogEntry.addComment(comment1);

    comment2 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", cal2.getTime(), State.APPROVED);
    comment2.setParent(comment1);
    blogEntry.addComment(comment2);

    assertEquals(2, blogEntry.getNumberOfComments());
  }

  /**
   * Tests that removing a comment also removes all of its children.
   */
  public void testRemovingCommentRemovesAllChildren() {
    Comment comment1, comment2;
    Date date1 = new Date(0);
    Date date2 = new Date(1000);

    comment1 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", date1, State.APPROVED);
    blogEntry.addComment(comment1);

    comment2 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", date2, State.APPROVED);
    comment2.setParent(comment1);
    blogEntry.addComment(comment2);

    // now remove the top-level comment, and hopefully all of its children
    blogEntry.removeComment(comment1.getId());
    assertEquals(0, blogEntry.getNumberOfComments());
  }

  /**
   * Tests that a nested comment can be removed.
   */
  public void testNestedCommentCanBeRemoved() {
    Comment comment1, comment2;
    Date date1 = new Date(0);
    Date date2 = new Date(1000);

    comment1 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", date1, State.APPROVED);
    blogEntry.addComment(comment1);

    comment2 = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", date2, State.APPROVED);
    comment2.setParent(comment1);
    blogEntry.addComment(comment2);

    // now remove the nested comment
    blogEntry.removeComment(comment2.getId());
    assertEquals(1, blogEntry.getNumberOfComments());
    assertNull(blogEntry.getComment(comment2.getId()));
  }

  /**
   * Tests that the permalink for a comment is correctly set.
   */
  public void testCommentPermalink() {
    Comment blogComment;

    blogComment = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    blogEntry.addComment(blogComment);
    assertEquals(blogEntry.getLocalPermalink() + "#comment" + blogComment.getId(), blogComment.getPermalink());
  }

  /**
   * Tests that a blog entry can correctly manage trackbacks.
   */
  public void testTrackBacks() {
    // check that there are no trackbacks to start with
    assertEquals(0, blogEntry.getNumberOfTrackBacks());
    assertEquals(0, blogEntry.getTrackBacks().size());
  }

  /**
   * Tests that a blog entry is cloneable.
   */
  public void testCloneableNormalBlogEntry() {
    blogEntry.setTitle("An old title");
    blogEntry.setSubtitle("An old subtitle");
    blogEntry.setBody("An old body");
    blogEntry.setAuthor("An old author");
    blogEntry.setPersistent(true);
    blogEntry.setCommentsEnabled(false);
    blogEntry.setTrackBacksEnabled(false);
    blogEntry.setTimeZoneId("Europe/Paris");

    Category category1 = blog.getCategory("/testCategory1");
    Category category2 = blog.getCategory("/testCategory2");
    Category category3 = blog.getCategory("/testCategory3");
    blogEntry.addCategory(category1);
    blogEntry.addCategory(category2);
    blogEntry.setTags("some+tag");

    BlogEntry clonedBlogEntry = (BlogEntry)blogEntry.clone();

    assertEquals(blogEntry.getTitle(), clonedBlogEntry.getTitle());
    assertEquals(blogEntry.getSubtitle(), clonedBlogEntry.getSubtitle());
    assertEquals(blogEntry.getBody(), clonedBlogEntry.getBody());
    assertEquals(blogEntry.getAuthor(), clonedBlogEntry.getAuthor());
    assertEquals(blogEntry.getPermalink(), clonedBlogEntry.getPermalink());
    assertEquals(blogEntry.inCategory(category1), clonedBlogEntry.inCategory(category1));
    assertEquals(blogEntry.inCategory(category2), clonedBlogEntry.inCategory(category2));
    assertEquals(blogEntry.inCategory(category3), clonedBlogEntry.inCategory(category3));
    assertEquals("some+tag", clonedBlogEntry.getTags());
    assertEquals(blogEntry.isPersistent(), clonedBlogEntry.isPersistent());
    assertFalse(clonedBlogEntry.isCommentsEnabled());
    assertFalse(clonedBlogEntry.isTrackBacksEnabled());
    assertNull(clonedBlogEntry.getAttachment());
    assertEquals(blogEntry.getTimeZoneId(), clonedBlogEntry.getTimeZoneId());
  }

  /**
   * Tests that a blog entry is cloneable.
   */
  public void testCloneableAggregatedBlogEntry() {
    blogEntry.setTitle("An old title");
    blogEntry.setBody("An old body");
    blogEntry.setAuthor("An old author");
    blogEntry.setOriginalPermalink("An old alternative permalink");

    Category category1 = blog.getCategory("/testCategory1");
    Category category2 = blog.getCategory("/testCategory2");
    Category category3 = blog.getCategory("/testCategory3");
    blogEntry.addCategory(category1);
    blogEntry.addCategory(category2);

    BlogEntry clonedBlogEntry = (BlogEntry)blogEntry.clone();

    assertEquals(blogEntry.getTitle(), clonedBlogEntry.getTitle());
    assertEquals(blogEntry.getBody(), clonedBlogEntry.getBody());
    assertEquals(blogEntry.getAuthor(), clonedBlogEntry.getAuthor());
    assertEquals(blogEntry.getOriginalPermalink(), clonedBlogEntry.getOriginalPermalink());
    assertEquals(blogEntry.inCategory(category1), clonedBlogEntry.inCategory(category1));
    assertEquals(blogEntry.inCategory(category2), clonedBlogEntry.inCategory(category2));
    assertEquals(blogEntry.inCategory(category3), clonedBlogEntry.inCategory(category3));
    assertNull(clonedBlogEntry.getAttachment());
  }

  public void testTopLevelCommentsClonedProperly() {
    Comment comment = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    blogEntry.addComment(comment);

    BlogEntry clonedBlogEntry = (BlogEntry)blogEntry.clone();
    Comment clonedComment = clonedBlogEntry.getComments().get(0);
    assertSame(clonedBlogEntry, clonedComment.getBlogEntry());

  }

  public void testNestedCommentsClonedProperly() {
    Comment comment1 = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    blogEntry.addComment(comment1);
    Comment comment2 = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    comment2.setParent(comment1);
    blogEntry.addComment(comment2);

    BlogEntry clonedBlogEntry = (BlogEntry)blogEntry.clone();
    Comment clonedComment1 = clonedBlogEntry.getComments().get(0);
    Comment clonedComment2 = clonedBlogEntry.getComments().get(1);
    assertSame(clonedComment1, clonedComment2.getParent());
    assertSame(clonedBlogEntry, clonedComment1.getBlogEntry());
    assertSame(clonedBlogEntry, clonedComment2.getBlogEntry());
  }

// todo
//  public void testValidationForStaticPage() {
//    blogEntry.setType(BlogEntry.STATIC_PAGE);
//
//    ValidationContext context = new ValidationContext();
//    blogEntry.validate(context);
//    assertTrue("Name shouldn't be empty", context.hasErrors());
//
//    context = new ValidationContext();
//    blogEntry.setStaticName("someStoryName");
//    blogEntry.validate(context);
//    assertFalse(context.hasErrors());
//
//    context = new ValidationContext();
//    blogEntry.setStaticName("2004/someStoryName");
//    blogEntry.validate(context);
//    assertFalse(context.hasErrors());
//
//    context = new ValidationContext();
//    blogEntry.setStaticName("some-story-name");
//    blogEntry.validate(context);
//    assertFalse(context.hasErrors());
//
//    context = new ValidationContext();
//    blogEntry.setStaticName("someStoryName.html");
//    blogEntry.validate(context);
//    assertTrue("Name shouldn't contain punctuation", context.hasErrors());
//  }

  /**
   * Tests that the next blog entry can be accessed.
   */
  public void testGetNextBlogEntry() throws Exception {
    BlogService service = new BlogService();
    Day today = blog.getBlogForToday();
    Day oneDayAgo = today.getPreviousDay();
    Day twoDaysAgo = today.getPreviousDay().getPreviousDay();

    BlogEntry b1 = new BlogEntry(blog);
    b1.setDate(twoDaysAgo.getDate());
    b1.setPublished(true);

    BlogEntry b2 = new BlogEntry(blog);
    b2.setDate(oneDayAgo.getDate());
    b2.setPublished(true);

    BlogEntry b3 = new BlogEntry(blog);
    b3.setDate(today.getDate());
    b3.setPublished(true);

    service.putBlogEntry(b1);
    service.putBlogEntry(b2);
    service.putBlogEntry(b3);

    assertNull(b3.getNextBlogEntry());
    assertEquals(b3, b2.getNextBlogEntry());
    assertEquals(b2, b1.getNextBlogEntry());
  }

  /**
   * Tests that the previous blog entry can be accessed.
   */
  public void testGetPreviousBlogEntry() throws Exception {
    BlogService service = new BlogService();
    Day today = blog.getBlogForToday();
    Day oneDayAgo = today.getPreviousDay();
    Day twoDaysAgo = today.getPreviousDay().getPreviousDay();

    BlogEntry b1 = new BlogEntry(blog);
    b1.setDate(twoDaysAgo.getDate());
    b1.setPublished(true);

    BlogEntry b2 = new BlogEntry(blog);
    b2.setDate(oneDayAgo.getDate());
    b2.setPublished(true);

    BlogEntry b3 = new BlogEntry(blog);
    b3.setDate(today.getDate());
    b3.setPublished(true);

    service.putBlogEntry(b1);
    service.putBlogEntry(b2);
    service.putBlogEntry(b3);

    assertNull(b1.getPreviousBlogEntry());
    assertEquals(b1, b2.getPreviousBlogEntry());
    assertEquals(b2, b3.getPreviousBlogEntry());
  }

  /**
   * Tests for a blog entry attachment.
   */
  public void testAttachment() {
    Attachment a = new Attachment("url", 1024, "image/jpeg");
    blogEntry.setAttachment(a);
    assertEquals(a, blogEntry.getAttachment());

    BlogEntry clonedBlogEntry = (BlogEntry)blogEntry.clone();
    assertEquals(a, clonedBlogEntry.getAttachment());
  }

// todo
//  public void testPermalinkForStaticPage() {
//    blogEntry.setType(BlogEntry.STATIC_PAGE);
//    blogEntry.setStaticName("SomePage");
//    assertEquals("http://www.yourdomain.com/blog/pages/SomePage.html", blogEntry.getPermalink());
//  }

  /**
   * Tests that listeners are not fired when a comment is added/removed on a clone.
   */
  public void testCommentListenersNotFiredFromClone() {
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    blogEntry = (BlogEntry)blogEntry.clone();
    blogEntry.addComment(comment);
    assertTrue(blogEntry.getEvents().isEmpty());
  }

  /**
   * Tests that listeners are not fired when a TrackBack is added/removed on a clone.
   */
  public void testTrackBackListenersNotFiredFromClone() {
    final TrackBack trackBack = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    blogEntry = (BlogEntry)blogEntry.clone();
    blogEntry.addTrackBack(trackBack);
    blogEntry.removeTrackBack(trackBack.getId());
    assertTrue(blogEntry.getEvents().isEmpty());
  }

  public void testPropertyChangedEventsNotFiredWhenEventsDisabled() {
    assertFalse(blogEntry.isDirty());
    blogEntry.setEventsEnabled(false);
    blogEntry.setBody("New body");
    assertFalse(blogEntry.isDirty());
  }

  public void testPropertyChangedEventsFiredForBodyProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setBody(blogEntry.getBody());
    assertFalse(blogEntry.isDirty());

    blogEntry.setBody("New body");
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.BODY_PROPERTY, event.getPropertyName());
    assertEquals("Some body", event.getOldValue());
    assertEquals("New body", event.getNewValue());
  }

  public void testPropertyChangedEventsFiredForExcerptProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setExcerpt(blogEntry.getExcerpt());
    assertFalse(blogEntry.isDirty());

    blogEntry.setExcerpt("New excerpt");
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.EXCERPT_PROPERTY, event.getPropertyName());
    assertEquals("Some excerpt", event.getOldValue());
    assertEquals("New excerpt", event.getNewValue());
  }

  public void testPropertyChangedEventsFiredForTitleProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setTitle(blogEntry.getTitle());
    assertFalse(blogEntry.isDirty());

    blogEntry.setTitle("New title");
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.TITLE_PROPERTY, event.getPropertyName());
    assertEquals("A title", event.getOldValue());
    assertEquals("New title", event.getNewValue());
  }

  public void testPropertyChangedEventsNotFiredForAuthorProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setAuthor(blogEntry.getAuthor());
    assertFalse(blogEntry.isDirty());

    blogEntry.setAuthor("New author");
    assertFalse(blogEntry.isDirty());
    //PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    //assertEquals(blogEntry, event.getSource());
    //assertEquals(BlogEntry.AUTHOR_PROPERTY, event.getPropertyName());
    //assertEquals("An author", event.getOldValue());
    //assertEquals("New author", event.getNewValue());
  }

  public void testPropertyChangedEventsFiredForOriginalPermalinkProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setOriginalPermalink(null);
    assertFalse(blogEntry.isDirty());

    blogEntry.setOriginalPermalink("New permalink");
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.ORIGINAL_PERMALINK_PROPERTY, event.getPropertyName());
    assertNull(event.getOldValue());
    assertEquals("New permalink", event.getNewValue());

    blogEntry.clearPropertyChangeEvents();
    blogEntry.setOriginalPermalink(blogEntry.getOriginalPermalink());
    assertFalse(blogEntry.isDirty());
  }

  public void testPropertyChangedEventsFiredForAttachmentProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setAttachment(null);
    assertFalse(blogEntry.isDirty());

    Attachment newAttachment = new Attachment("url", 1234, "image/jpeg");
    blogEntry.setAttachment(newAttachment);
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.ATTACHMENT_PROPERTY, event.getPropertyName());
    assertNull(event.getOldValue());
    assertEquals(newAttachment, event.getNewValue());

    blogEntry.clearPropertyChangeEvents();
    blogEntry.setAttachment(newAttachment);
    assertFalse(blogEntry.isDirty());
  }

  public void testPropertyChangedEventsFiredForCommentsEnabledProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setCommentsEnabled(true);
    assertFalse(blogEntry.isDirty());

    blogEntry.setCommentsEnabled(false);
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.COMMENTS_ENABLED_PROPERTY, event.getPropertyName());
    assertEquals(Boolean.TRUE, event.getOldValue());
    assertEquals(Boolean.FALSE, event.getNewValue());
  }

  public void testPropertyChangedEventsFiredForTrackBacksEnabledProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setTrackBacksEnabled(true);
    assertFalse(blogEntry.isDirty());

    blogEntry.setTrackBacksEnabled(false);
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.TRACKBACKS_ENABLED_PROPERTY, event.getPropertyName());
    assertEquals(Boolean.TRUE, event.getOldValue());
    assertEquals(Boolean.FALSE, event.getNewValue());
  }

  public void testPropertyChangedEventsFiredForCategoriesProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.removeAllCategories();
    assertFalse(blogEntry.isDirty());

    Category c1 = new Category("/c1", "Category 1");
    blog.addCategory(c1);
    Category c2 = new Category("/c2", "Category 2");
    blog.addCategory(c1);
    blogEntry.addCategory(c1);
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.CATEGORIES_PROPERTY, event.getPropertyName());
    assertTrue(((Set)event.getOldValue()).isEmpty());
    assertTrue(((Set)event.getNewValue()).contains(c1));

    blogEntry.clearPropertyChangeEvents();
    blogEntry.addCategory(c2);
    assertTrue(blogEntry.isDirty());
    event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.CATEGORIES_PROPERTY, event.getPropertyName());
    assertTrue(((Set)event.getOldValue()).contains(c1));
    assertTrue(((Set)event.getNewValue()).contains(c1));
    assertTrue(((Set)event.getNewValue()).contains(c2));

    blogEntry.removeAllCategories();
    blogEntry.clearPropertyChangeEvents();
    Set categories = new HashSet();
    categories.add(c1);
    categories.add(c2);
    blogEntry.setCategories(categories);
    assertTrue(blogEntry.isDirty());
    event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.CATEGORIES_PROPERTY, event.getPropertyName());
    assertTrue(((Set)event.getOldValue()).isEmpty());
    assertTrue(((Set)event.getNewValue()).contains(c1));
    assertTrue(((Set)event.getNewValue()).contains(c2));

    blogEntry.clearPropertyChangeEvents();
    categories = new HashSet();
    categories.add(c2);
    blogEntry.setCategories(categories);
    assertTrue(blogEntry.isDirty());
    event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.CATEGORIES_PROPERTY, event.getPropertyName());
    assertTrue(((Set)event.getOldValue()).contains(c1));
    assertTrue(((Set)event.getOldValue()).contains(c2));
    assertTrue(((Set)event.getNewValue()).contains(c2));
  }

  public void testPropertyChangedEventsFiredForTagsProperty() {
    assertFalse(blogEntry.isDirty());

    blogEntry.setTags(blogEntry.getTags());
    assertFalse(blogEntry.isDirty());

    blogEntry.setTags("some tags");
    assertTrue(blogEntry.isDirty());
    PropertyChangeEvent event = (PropertyChangeEvent)blogEntry.getPropertyChangeEvents().get(0);
    assertEquals(blogEntry, event.getSource());
    assertEquals(BlogEntry.TAGS_PROPERTY, event.getPropertyName());
    assertEquals("", event.getOldValue());
    assertEquals("some tags", event.getNewValue());
  }

  /**
   * Tests that all tags can be retrieved.
   */
  public void testGetAllTags() {
    CategoryBuilder builder = new CategoryBuilder(blog);
    Category apple = new Category("/apple", "Apple");
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    builder.getRootCategory().setTags("myblog");
    builder.addCategory(apple);
    apple.setTags("apple");
    builder.addCategory(java);
    java.setTags("java");
    builder.addCategory(junit);
    junit.setTags("junit automated+testing");

    blogEntry.addCategory(apple);
    blogEntry.addCategory(junit);
    blogEntry.setTags("entry+specific+tag");

    List tags = blogEntry.getAllTags();
    assertTrue(tags.contains(blog.getTag("entry+specific+tag")));
    assertTrue(tags.contains(blog.getTag("junit")));
    assertTrue(tags.contains(blog.getTag("automated+testing")));
    assertTrue(tags.contains(blog.getTag("java")));
    assertTrue(tags.contains(blog.getTag("apple")));
    assertTrue(tags.contains(blog.getTag("myblog")));
  }

  /**
   * Tests that all tags can be retrieved, even when the blog entry
   * hasn't been associated with any categories.
   */
  public void testGetAllTagsWhenNotAssociatedWithACategory() {
    CategoryBuilder builder = new CategoryBuilder(blog);
    builder.getRootCategory().setTags("myblog");
    blog.setRootCategory(builder.getRootCategory());

    List tags = blogEntry.getAllTags();
    assertEquals(blog.getTag("myblog"), tags.get(0));
  }

  /**
   * Tests the hasTag() method.
   */
  public void testHasTag() {
    CategoryBuilder builder = new CategoryBuilder(blog);
    Category apple = new Category("/apple", "Apple");
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    builder.getRootCategory().setTags("myblog");
    builder.addCategory(apple);
    apple.setTags("apple");
    builder.addCategory(java);
    java.setTags("java");
    builder.addCategory(junit);
    junit.setTags("junit automated+testing");

    assertFalse(blogEntry.hasTag(null));
    assertFalse(blogEntry.hasTag(""));
    assertFalse(blogEntry.hasTag("java"));

    blogEntry.setTags("entryspecifictag");
    assertTrue(blogEntry.hasTag("entryspecifictag"));

    blogEntry.addCategory(apple);
    assertTrue(blogEntry.hasTag("apple"));
    blogEntry.addCategory(junit);
    assertTrue(blogEntry.hasTag("junit"));
  }

  public void testLastModifiedDate() {
    blogEntry.setDate(new Date(100));
    blogEntry.addComment(blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", new Date(123), State.APPROVED));
    blogEntry.addComment(blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", new Date(456), State.APPROVED));
    blogEntry.addComment(blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", new Date(789), State.APPROVED));

    assertEquals(new Date(789), blogEntry.getLastModified());

    blogEntry.addTrackBack(blogEntry.createTrackBack("Title", "Excerpt", "http://www.somedomain.com", "Some blog", "127.0.0.1", new Date(123), State.APPROVED));
    blogEntry.addTrackBack(blogEntry.createTrackBack("Title", "Excerpt", "http://www.somedomain.com", "Some blog", "127.0.0.1", new Date(543), State.APPROVED));
    blogEntry.addTrackBack(blogEntry.createTrackBack("Title", "Excerpt", "http://www.somedomain.com", "Some blog", "127.0.0.1", new Date(987), State.APPROVED));

    assertEquals(new Date(987), blogEntry.getLastModified());
   }

  public void testRemoveCommentViaRemoveResponse() {
    Comment comment = blogEntry.createComment("", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", new Date(123), State.APPROVED);
    blogEntry.addComment(comment);
    assertEquals(1, blogEntry.getNumberOfComments());
    blogEntry.removeResponse(comment);
    assertEquals(0, blogEntry.getNumberOfComments());
   }

  public void testRemoveTrackBackViaRemoveResponse() {
    TrackBack trackBack = blogEntry.createTrackBack("Title", "Excerpt", "http://www.somedomain.com", "Some blog", "127.0.0.1", new Date(123), State.APPROVED);
    blogEntry.addTrackBack(trackBack);
    assertEquals(1, blogEntry.getNumberOfTrackBacks());
    blogEntry.removeResponse(trackBack);
    assertEquals(0, blogEntry.getNumberOfTrackBacks());
   }

 }