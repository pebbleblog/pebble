package net.sourceforge.pebble.openid;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.PageDecorator;
import net.sourceforge.pebble.api.decorator.PageDecoratorContext;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * Provides support for Facebook OpenID operations
 *
 * @author James Roper
 */
public class FacebookOpenIdSupportDecorator implements PageDecorator {

  public static final String RENDER_FACEBOOK_SUPPORT_KEY = "renderFacebookSupport";
  private static final String FACEBOOK_APP_ID_KEY = "facebook.app.id";

  public void decorateHead(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException {
  }

  public void decorateHeader(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException {
  }

  public void decorateFooter(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException {
    Boolean renderFacebookSupport = (Boolean) pageDecoratorContext.getRequest().getAttribute(RENDER_FACEBOOK_SUPPORT_KEY);
    if (renderFacebookSupport != null && renderFacebookSupport) {
      // Find out the app ID
      AbstractBlog abstractBlog = (AbstractBlog) pageDecoratorContext.getRequest().getAttribute(Constants.BLOG_KEY);
      if (abstractBlog instanceof Blog) {
        String appId = ((Blog) abstractBlog).getPluginProperties().getProperty(FACEBOOK_APP_ID_KEY);
        if (appId != null) {
          out.append("<div id=\"fb-root\"></div>\n" +
                  "<script src=\"http://connect.facebook.net/en_US/all.js\"></script>\n" +
                  "<script>\n" +
                  "FB.init({appId: '").append(appId).append("', status: false, cookie: true, xfbml: true, channelUrl: '")
                  .append(abstractBlog.getUrl()).append("facebook_channel.html'});\n" +
                  "</script>");
        } else {
          // Probably should warn that you need an APP id....
        }
      }
    }
  }
}
