package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.CategoriesView;

/**
 * Tests for the ViewCategoriesAction class.
 *
 * @author    Simon Brown
 */
public class ViewCategoriesListActionTest extends SingleBlogActionTestCase {

  protected void setUp() throws Exception {
    action = new ViewCategoriesListAction();

    super.setUp();
  }

  public void testViewTags() throws Exception {
    View view = action.process(request, response);

    assertTrue(view instanceof CategoriesView);
    assertEquals("/WEB-INF/jsp/viewCategories.jsp", ((CategoriesView)view).getUri());
  }

}
