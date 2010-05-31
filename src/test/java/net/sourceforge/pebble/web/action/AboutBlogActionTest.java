package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.BlogDetailsView;

/**
 * Tests for the AboutBlogAction class.
 *
 * @author    Simon Brown
 */
public class AboutBlogActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new AboutBlogAction();

    super.setUp();
  }

  public void testProcess() throws Exception {
    View view = action.process(request, response);
    assertTrue(view instanceof BlogDetailsView);
  }

  /**
   * Test that only blog contributors have access to add a blog entry.
   */
  public void testOnlyBlogContributorsHaveAccess() {
    String roles[] = action.getRoles(request);
    assertEquals(2, roles.length);
    assertEquals(Constants.BLOG_ADMIN_ROLE, roles[0]);
    assertEquals(Constants.BLOG_OWNER_ROLE, roles[1]);
  }

}
