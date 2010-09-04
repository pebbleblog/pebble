package net.sourceforge.pebble.api.openid;

import net.sourceforge.pebble.api.decorator.PageDecoratorContext;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * Interface that open id comment author providers can implement to add Open ID support to comments.
 *
 * @author James Roper
 */
public interface OpenIdCommentAuthorProvider {

  /**
   * Render the HTML for the provider.  This should contain a link of some sort for the user to log in to  the OpenID
   * provider, that when clicked, and the user is logged in, calls the Javascript function
   * updateOpenIdCommentAuthor(author, website, email, avatar, logoutCallback).  The logoutCallback should be a callback
   * that will be called to log the user out of the open ID provider.
   *
   * @param jspWriter The writer to write to
   * @param pageDecoratorContext The context
   */
  void renderProvider(JspWriter jspWriter, PageDecoratorContext pageDecoratorContext) throws IOException;
}
