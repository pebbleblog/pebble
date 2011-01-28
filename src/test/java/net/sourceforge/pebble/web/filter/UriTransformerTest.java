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
package net.sourceforge.pebble.web.filter;

import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.permalink.DefaultPermalinkProvider;
import net.sourceforge.pebble.permalink.TitlePermalinkProvider;

/**
 * Tests for the UriTransformer class.
 *
 * @author    Simon Brown
 */
public class UriTransformerTest extends SingleBlogTestCase {

  private UriTransformer transformer;

  protected void setUp() throws Exception {
    super.setUp();

    transformer = new UriTransformer();
  }

  public void testSimpleUrlsForSingleUserBlog() throws Exception {
    assertEquals("/viewHomePage.action", transformer.getUri("", blog));
    assertEquals("/viewHomePage.action", transformer.getUri("/", blog));
  }

  public void testRssUrlForSingleUserBlog() throws Exception {
    assertEquals("/feed.action?flavor=rss20", transformer.getUri("/rss.xml", blog));
  }

  public void testRssUrlForResponseFeed() throws Exception {
    assertEquals("/responseFeed.action?flavor=rss20", transformer.getUri("/responses/rss.xml", blog));
  }

  public void testRssUrlForResponseFeedForBlogEntry() throws Exception {
    assertEquals("/responseFeed.action?flavor=rss20&entry=1202928360000", transformer.getUri("/responses/rss.xml?entry=1202928360000", blog));
  }

  public void testRdfUrlForSingleUserBlog() throws Exception {
    assertEquals("/feed.action?flavor=rdf", transformer.getUri("/rdf.xml", blog));
  }

  public void testAtomUrlForSingleUserBlog() throws Exception {
    assertEquals("/feed.action?flavor=atom", transformer.getUri("/atom.xml", blog));
  }

  public void testAtomUrlForResponseFeed() throws Exception {
    assertEquals("/responseFeed.action?flavor=atom", transformer.getUri("/responses/atom.xml", blog));
  }

  public void testIncorrectRssUrlForSingleUserBlog() throws Exception {
    assertEquals("/viewFeeds.action/rss.xml", transformer.getUri("/viewFeeds.action/rss.xml", blog));
  }

  public void testRandomUrlForSingleUserBlog() throws Exception {
    // test a random uri that doesn't point to anything special
    assertEquals("/somerandompage.html", transformer.getUri("/somerandompage.html", blog));
  }

  public void testImageUrlForSingleUserBlog() throws Exception {
    // test a uri that points to an image located within the blog
    assertEquals("/file.action?type=" + FileMetaData.BLOG_IMAGE + "&name=/myImage.jpg", transformer.getUri("/images/myImage.jpg", blog));
  }

  public void testFileUrlForSingleUserBlog() throws Exception {
    // test a uri that points to an image located within the blog
    assertEquals("/file.action?type=" + FileMetaData.BLOG_FILE + "&name=/myFile.zip", transformer.getUri("/files/myFile.zip", blog));
  }

  public void testMonthlyUrlForSingleUserBlog() throws Exception {
    // test a url to request a whole month
    assertEquals("/viewMonth.action?year=2003&month=11", transformer.getUri("/2003/11.html", blog));
  }

  public void testDailyUrlForSingleUserBlog() throws Exception {
    // test a url to request a whole day
    assertEquals("/viewDay.action?year=2003&month=11&day=24", transformer.getUri("/2003/11/24.html", blog));
  }

  public void testPermalinkUrlsForSingleUserBlog() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    // test a url to request a single blog entry
    String url = blogEntry.getLocalPermalink();
    assertEquals("/viewBlogEntry.action?entry=" + blogEntry.getId(), transformer.getUri(url.substring(url.indexOf("/blog/")+5), blog));
  }

  public void testStaticPermalinkUrlsForSingleUserBlog() throws Exception {
    // test a url to request a "static" permalink
    assertEquals("/viewStaticPage.action?name=some-story", transformer.getUri("/pages/some-story.html", blog));
  }

  public void testDefaultStaticPageWithNoTrailingSlash() throws Exception {
    assertEquals("/viewStaticPage.action?name=index", transformer.getUri("/pages", blog));
  }

  public void testDefaultStaticPageWithTrailingSlash() throws Exception {
    assertEquals("/viewStaticPage.action?name=index", transformer.getUri("/pages/", blog));
  }

  public void testUrlEndingWithSlashForSingleUserBlog() throws Exception {
    // test a url to request a single blog entry (long)
    assertEquals("/2003/", transformer.getUri("/2003/", blog));
  }

  public void testCategoriesLink() throws Exception {
    assertEquals("/viewCategories.action", transformer.getUri("/categories/", blog));
    assertEquals("/viewCategories.action", transformer.getUri("/categories", blog));
  }

  public void testCategoryPermalink() throws Exception {
    assertEquals("/viewCategory.action?category=/category1", transformer.getUri("/categories/category1/", blog));
  }

  public void testSubCategoryPermalinkWithTrailingSlash() throws Exception {
    assertEquals("/viewCategory.action?category=/category1/subcategory", transformer.getUri("/categories/category1/subcategory/", blog));
  }

  public void testSubCategoryPermalinkWithoutTrailingSlash() throws Exception {
    assertEquals("/viewCategory.action?category=/category1/subcategory", transformer.getUri("/categories/category1/subcategory", blog));
  }

  public void testCategoryNewsFeed() throws Exception {
    assertEquals("/feed.action?category=/category1&flavor=rss20", transformer.getUri("/categories/category1/rss.xml", blog));
    assertEquals("/feed.action?category=/category1&flavor=rdf", transformer.getUri("/categories/category1/rdf.xml", blog));
    assertEquals("/feed.action?category=/category1&flavor=atom", transformer.getUri("/categories/category1/atom.xml", blog));
  }

  public void testTagsLink() throws Exception {
    assertEquals("/viewTags.action", transformer.getUri("/tags/", blog));
    assertEquals("/viewTags.action", transformer.getUri("/tags", blog));
  }

  public void testTagPermalinkWithTrailingSlash() throws Exception {
    assertEquals("/viewTag.action?tag=automated unit testing", transformer.getUri("/tags/automated unit testing", blog));
  }

  public void testTagPermalinkWithoutTrailingSlash() throws Exception {
    assertEquals("/viewTag.action?tag=automated unit testing", transformer.getUri("/tags/automated unit testing/", blog));
  }

  public void testTagNewsFeed() throws Exception {
    assertEquals("/feed.action?tag=automated+unit+testing&flavor=rss20", transformer.getUri("/tags/automated+unit+testing/rss.xml", blog));
    assertEquals("/feed.action?tag=automated+unit+testing&flavor=rdf", transformer.getUri("/tags/automated+unit+testing/rdf.xml", blog));
    assertEquals("/feed.action?tag=automated+unit+testing&flavor=atom", transformer.getUri("/tags/automated+unit+testing/atom.xml", blog));
  }

  public void testAuthorPermalink() throws Exception {
    assertEquals("/aboutAuthor.action?user=sbrown", transformer.getUri("/authors/sbrown/", blog));
  }

  public void testAuthorNewsFeed() throws Exception {
    assertEquals("/feed.action?author=sbrown&flavor=rss20", transformer.getUri("/authors/sbrown/rss.xml", blog));
    assertEquals("/feed.action?author=sbrown&flavor=rdf", transformer.getUri("/authors/sbrown/rdf.xml", blog));
    assertEquals("/feed.action?author=sbrown&flavor=atom", transformer.getUri("/authors/sbrown/atom.xml", blog));
  }

  public void testTodayPage() throws Exception {
    assertEquals("/viewDay.action", transformer.getUri("/today.html", blog));
  }

  public void testBlogEntryWithDefaultPermalinkProvider() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);
    assertEquals("/viewBlogEntry.action?entry=" + blogEntry.getId(), transformer.getUri(blog.getPermalinkProvider().getPermalink(blogEntry), blog));
  }

  public void testBlogEntryWithTitlePermalinkProvider() throws Exception {
    blog.setPermalinkProvider(new TitlePermalinkProvider());

    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setTitle("Some title");
    service.putBlogEntry(blogEntry);

    assertEquals("/viewBlogEntry.action?entry=" + blogEntry.getId(), transformer.getUri(blog.getPermalinkProvider().getPermalink(blogEntry), blog));
  }

  public void testBlogEntryFallsBackToDefaultPermalinkProvider() throws Exception {
    DefaultPermalinkProvider defaultProvider = new DefaultPermalinkProvider();
    defaultProvider.setBlog(blog);
    blog.setPermalinkProvider(new TitlePermalinkProvider());

    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setTitle("Some title");
    service.putBlogEntry(blogEntry);

    assertEquals("/viewBlogEntry.action?entry=" + blogEntry.getId(), transformer.getUri(defaultProvider.getPermalink(blogEntry), blog));
  }

  public void testViewBlogEntriesByPageUrlForSingleUserBlog() throws Exception {
    assertEquals("/viewBlogEntriesByPage.action?page=1", transformer.getUri("/blogentries/1.html", blog));
  }

  public void testAboutUrlForSingleUserBlog() throws Exception {
    assertEquals("/about.action", transformer.getUri("/about.html", blog));
  }

}
