package net.sourceforge.pebble.util.importer;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.SingleBlogTestCase;

import java.io.File;
import java.util.List;

public class MovableTypeImporterTest extends SingleBlogTestCase {
  private String testCasesDir = "test" + File.separator + "mt_testcases";

  public void testImport() throws Exception {
    blog.setProperty(Blog.TIMEZONE_KEY, "Japan/Tokyo");
    File source = new File(testCasesDir, "exported.txt");
    MovableTypeImporter.main(new String[]{source.getAbsolutePath(), blog.getRoot(), "Tokyo/Japan"});
    blog.reindex();
    List list = blog.getBlogEntries();
    assertEquals("size of entry", 1, list.size());
    BlogEntry entry = (BlogEntry) list.get(0);
    assertEquals("excerpt", "excerpt", entry.getContent());
    assertEquals("excerpt", "excerpt", entry.getExcerpt());
    //body part needs to include extended body
    assertEquals("body", "body<br />extended body", entry.getBody());
  }

  public void testNoPrimaryCategory() throws Exception {
    blog.setProperty(Blog.TIMEZONE_KEY, "Japan/Tokyo");
    File source = new File(testCasesDir, "noprimarycategory.txt");
    MovableTypeImporter.main(new String[]{source.getAbsolutePath(), blog.getRoot(), "Tokyo/Japan"});
    blog.reindex();
    List list = blog.getBlogEntries();
    assertEquals("size of entry", 1, list.size());
  }

  public void testMultipleSubCategory() throws Exception {
    blog.setProperty(Blog.TIMEZONE_KEY, "Japan/Tokyo");
    File source = new File(testCasesDir, "multiplesubcategory.txt");
    MovableTypeImporter.main(new String[]{source.getAbsolutePath(), blog.getRoot(), "Tokyo/Japan"});
    blog.reindex();
    blog.reindex();
    List list = blog.getBlogEntries();
    assertEquals("size of entry", 1, list.size());
//    BlogEntry entry = (BlogEntry)list.get(0);
//    Set<Category> categories = entry.getCategories();
//    assertEquals("# of categories",3, categories.size());
//    assertTrue(categories.contains("mycategory"));
//    assertTrue(categories.contains("subcategory"));
//    assertTrue(categories.contains("subcategory2"));
  }

  public void testNoExcerpt() throws Exception {
    blog.setProperty(Blog.TIMEZONE_KEY, "Japan/Tokyo");
    File source = new File(testCasesDir, "noexcerpt.txt");
    MovableTypeImporter.main(new String[]{source.getAbsolutePath(), blog.getRoot(), "Tokyo/Japan"});
    blog.reindex();
    List list = blog.getBlogEntries();
    assertEquals("size of entry", 1, list.size());
    BlogEntry entry = (BlogEntry) list.get(0);
    assertEquals("content", "body", entry.getContent());
    assertEquals("excerpt", "body", entry.getExcerpt());
    //body part needs to include extended body
    assertEquals("body", "body<br />extended body", entry.getBody());
  }

  public void testNoExcerptNoExtendedBody() throws Exception {
    blog.setProperty(Blog.TIMEZONE_KEY, "Japan/Tokyo");
    File source = new File(testCasesDir, "noexcerpt_noextendedbody.txt");
    MovableTypeImporter.main(new String[]{source.getAbsolutePath(), blog.getRoot(), "Tokyo/Japan"});
    blog.reindex();
    List list = blog.getBlogEntries();
    assertEquals("size of entry", 1, list.size());
    BlogEntry entry = (BlogEntry) list.get(0);
    assertEquals("content", "body", entry.getContent());
    assertEquals("excerpt", "", entry.getExcerpt());
    //body part needs to include extended body
    assertEquals("body", "body", entry.getBody());
  }

  public void testUTF8() throws Exception {
    blog.setProperty(Blog.TIMEZONE_KEY, "Japan/Tokyo");
    File source = new File(testCasesDir, "utf8.txt");
    MovableTypeImporter.main(new String[]{source.getAbsolutePath(), blog.getRoot(), "Tokyo/Japan"});
    blog.reindex();
    List list = blog.getBlogEntries();
    assertEquals("size of entry", 1, list.size());
    BlogEntry entry = (BlogEntry) list.get(0);
    assertEquals("content", "\u65E5\u672C\u8A9Eexcerpt", entry.getContent());
    assertEquals("title", "\u65E5\u672C\u8A9E", entry.getTitle());
  }

  public void testPublished() throws Exception {
    blog.setProperty(Blog.TIMEZONE_KEY, "Japan/Tokyo");
    File source = new File(testCasesDir, "exported.txt");
    MovableTypeImporter.main(new String[]{source.getAbsolutePath(), blog.getRoot(), "Tokyo/Japan"});
    blog.reindex();
    List list = blog.getBlogEntries();
    assertEquals("size of entry", 1, list.size());
    BlogEntry entry = (BlogEntry) list.get(0);
    assertTrue("publised", entry.isPublished());
  }
  public void testComment() throws Exception {
    blog.setProperty(Blog.TIMEZONE_KEY, "Japan/Tokyo");
    File source = new File(testCasesDir, "withcomment.txt");
    MovableTypeImporter.main(new String[]{source.getAbsolutePath(), blog.getRoot(), "Tokyo/Japan"});
    blog.reindex();
    List list = blog.getBlogEntries();
    assertEquals("size of entry", 1, list.size());
    BlogEntry entry = (BlogEntry) list.get(0);
    assertTrue("publised", entry.isPublished());
    List<Comment> comments = entry.getComments();
    assertEquals("size of comments", 1, comments.size());
    System.out.println(blog.getRoot());
  }
}
