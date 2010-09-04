package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.CategoriesView;
import net.sourceforge.pebble.Constants;

/**
 * Tests for the ViewCategoriesAction class.
 *
 * @author    Simon Brown
 */
public class ViewCategoriesActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new ViewCategoriesAction();

    super.setUp();
  }

  public void testViewTags() throws Exception {
    View view = action.process(request, response);

    assertTrue(view instanceof CategoriesView);
    assertEquals("/WEB-INF/jsp/manageCategories.jsp", ((CategoriesView)view).getUri());
  }

  public void testOnlyBlogContributorsHaveAccess() {
    String roles[] = action.getRoles(request);
    assertEquals(1, roles.length);
    assertEquals(Constants.BLOG_CONTRIBUTOR_ROLE, roles[0]);
  }

}
