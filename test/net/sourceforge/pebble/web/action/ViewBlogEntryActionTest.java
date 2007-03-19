package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.impl.BlogEntryView;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;

/**
 * Tests for the ViewBlogEntryAction class.
 *
 * @author    Simon Brown
 */
public class ViewBlogEntryActionTest extends SingleBlogActionTestCase {

  protected void setUp() throws Exception {
    action = new ViewBlogEntryAction();

    super.setUp();
  }

  public void testViewBlogEntryWithNullId() throws Exception {
    View view = action.process(request, response);

    assertTrue(view instanceof NotFoundView);
  }

  public void testViewNonExistentBlogEntry() throws Exception {
    request.setParameter("entry", "1234567890123");
    View view = action.process(request, response);

    assertTrue(view instanceof NotFoundView);
  }

  public void testPublishedViewBlogEntry() throws Exception {
    BlogEntry blogEntry1 = new BlogEntry(blog);
    blogEntry1.setPublished(true);
    BlogService service = new BlogService();
    service.putBlogEntry(blogEntry1);

    SecurityUtils.runAsUnauthenticated();
    request.setParameter("entry", blogEntry1.getId());
    View view = action.process(request, response);

    BlogEntry blogEntry2 = (BlogEntry)action.getModel().get(Constants.BLOG_ENTRY_KEY);
    assertEquals(blogEntry1.getId(), blogEntry2.getId());
    assertTrue(view instanceof BlogEntryView);
  }

  public void testUnpublishedViewBlogEntryAsAnonymousUser() throws Exception {
    BlogEntry blogEntry1 = new BlogEntry(blog);
    blogEntry1.setPublished(false);
    BlogService service = new BlogService();
    service.putBlogEntry(blogEntry1);

    SecurityUtils.runAsAnonymous();
    request.setParameter("entry", blogEntry1.getId());
    View view = action.process(request, response);

    BlogEntry blogEntry2 = (BlogEntry)action.getModel().get(Constants.BLOG_ENTRY_KEY);
    assertNull(blogEntry2);
    assertTrue(view instanceof NotFoundView);
  }

  public void testUnpublishedViewBlogEntryAsUserThatIsAuthorisedForBlog() throws Exception {
    BlogEntry blogEntry1 = new BlogEntry(blog);
    blogEntry1.setPublished(false);
    BlogService service = new BlogService();
    service.putBlogEntry(blogEntry1);

    SecurityUtils.runAsBlogContributor();
    request.setParameter("entry", blogEntry1.getId());
    View view = action.process(request, response);

    BlogEntry blogEntry2 = (BlogEntry)action.getModel().get(Constants.BLOG_ENTRY_KEY);
    assertEquals(blogEntry1.getId(), blogEntry2.getId());
    assertTrue(view instanceof BlogEntryView);
  }

}
