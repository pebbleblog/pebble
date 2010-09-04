package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.TagsView;

/**
 * Tests for the ViewTagsAction class.
 *
 * @author    Simon Brown
 */
public class ViewTagsActionTest extends SingleBlogActionTestCase {

  protected void setUp() throws Exception {
    action = new ViewTagsAction();

    super.setUp();
  }

  public void testViewTags() throws Exception {
    View view = action.process(request, response);

    assertTrue(view instanceof TagsView);
  }

}
