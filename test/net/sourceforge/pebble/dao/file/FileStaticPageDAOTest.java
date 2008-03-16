package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.util.FileUtils;

import java.io.File;
import java.util.Locale;

/**
 * Tests for the FileStaticPageDAO class.
 *
 * @author    Simon Brown
 */
public class FileStaticPageDAOTest extends SingleBlogTestCase {

  private StaticPageDAO dao= new FileStaticPageDAO();
  private Locale defaultLocale;

  protected void setUp() throws Exception {
    super.setUp();

    defaultLocale = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
  }


  public void tearDown() throws Exception {
    super.tearDown();

    Locale.setDefault(defaultLocale);
  }

  public void testLoadStaticPageFomFile() throws Exception {

    File source = new File("test", "1152083300843.xml");
    File destination = new File(blog.getRoot(), "pages/1152083300843");
    destination.mkdirs();
    FileUtils.copyFile(source, new File(destination, "1152083300843.xml"));

    StaticPage page = dao.loadStaticPage(blog, "1152083300843");

    // test that the static page properties were loaded okay
    assertEquals("Static page title", page.getTitle());
    assertEquals("Static page subtitle", page.getSubtitle());
    assertEquals("<p>Static page body.</p>", page.getBody());
    assertEquals("some tags", page.getTags());
    assertEquals(1152083300843L, page.getDate().getTime());
    assertEquals("http://pebble.sourceforge.net", page.getOriginalPermalink());
  }

}
