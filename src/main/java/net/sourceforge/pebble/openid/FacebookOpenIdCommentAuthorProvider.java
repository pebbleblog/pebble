package net.sourceforge.pebble.openid;

import net.sourceforge.pebble.api.decorator.PageDecoratorContext;
import net.sourceforge.pebble.api.openid.OpenIdCommentAuthorProvider;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Provides OpenID integration from Facebook for comments
 *
 * @author James Roper
 */
public class FacebookOpenIdCommentAuthorProvider implements OpenIdCommentAuthorProvider {
  private static final String HTML_FILE = "facebookOpenIdCommentAuthor.html";

  public void renderProvider(JspWriter jspWriter, PageDecoratorContext pageDecoratorContext) throws IOException {
    // Load the resource
    Reader reader = null;
    try {
      reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(HTML_FILE));
      // 8kb buffer, for single byte characters this will be 4kb reads
      char[] buffer = new char[4096];
      int count = reader.read(buffer);
      while (count >= 0) {
        jspWriter.write(buffer, 0, count);
        count = reader.read(buffer);
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    pageDecoratorContext.getRequest().setAttribute(FacebookOpenIdSupportDecorator.RENDER_FACEBOOK_SUPPORT_KEY, true);
  }
}
