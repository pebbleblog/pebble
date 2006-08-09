package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;

/**
 * Tests for the AddBlogAction class.
 *
 * @author    Simon Brown
 */
public class AddBlogActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new AddBlogAction();

    super.setUp();
  }

  /**
   * Tests that only Pebble admins can add blogs.
   */
  public void testSecurityRolesForBlogImages() {
    String roles[] = action.getRoles(request);

    assertEquals(1, roles.length);
    assertEquals(Constants.BLOG_ADMIN_ROLE, roles[0]);
  }

}
