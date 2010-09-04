package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.View;

/**
 * Tests for the AddCategoryAction class.
 *
 * @author    Simon Brown
 */
public class AddCategoryActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new AddCategoryAction();

    super.setUp();
  }

  public void testProcess() throws Exception {
    View view = action.process(request, response);

    assertEquals(new Category(), action.getModel().get(Constants.CATEGORY_KEY));
    assertTrue(view instanceof ForwardView);
    assertEquals("/viewCategories.secureaction", ((ForwardView)view).getUri());
  }

  /**
   * Test that only blog contributors have access to add a blog entry.
   */
  public void testOnlyBlogContributorsHaveAccess() {
    String roles[] = action.getRoles(request);
    assertEquals(1, roles.length);
    assertEquals(Constants.BLOG_CONTRIBUTOR_ROLE, roles[0]);
  }

}
