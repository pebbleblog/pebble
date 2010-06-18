package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.PageDecorator;
import net.sourceforge.pebble.api.decorator.PageDecoratorContext;
import net.sourceforge.pebble.api.openid.OpenIdCommentAuthorProvider;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Renders the renderable plugins for a blog
 *
 * @author James Roper
 */
public class PluginRendererTag extends TagSupport {

  private static final String PAGE_DECORATOR_HEAD = "head";
  private static final String PAGE_DECORATOR_HEADER = "header";
  private static final String PAGE_DECORATOR_FOOTER = "footer";
  private static final String OPEN_ID_COMMENT_AUTHOR_PROVIDER = "openidcommentauthorprovider";

  /** The plugin point to render */
  private String plugin;

  @Override
  public int doStartTag() throws JspException {
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    AbstractBlog abstractBlog = (AbstractBlog) request.getAttribute(Constants.BLOG_KEY);

    try {
      if (abstractBlog instanceof Blog) {
        PageDecoratorContext context = new PageDecoratorContext(request);
        Blog blog = (Blog) abstractBlog;
        if (PAGE_DECORATOR_HEAD.equals(plugin)) {
          for (PageDecorator decorator : blog.getPageDecorators()) {
            decorator.decorateHead(pageContext.getOut(), context);
          }
        } else if (PAGE_DECORATOR_HEADER.equals(plugin)) {
          for (PageDecorator decorator : blog.getPageDecorators()) {
            decorator.decorateHeader(pageContext.getOut(), context);
          }
        } else if (PAGE_DECORATOR_FOOTER.equals(plugin)) {
          for (PageDecorator decorator : blog.getPageDecorators()) {
            decorator.decorateFooter(pageContext.getOut(), context);
          }
        }
        if (OPEN_ID_COMMENT_AUTHOR_PROVIDER.equals(plugin)) {
          for (OpenIdCommentAuthorProvider provider : blog.getOpenIdCommentAuthorProviders()) {
            provider.renderProvider(pageContext.getOut(), context);
          }
        }
      }
    } catch (IOException ioe) {
      throw new JspException(ioe);
    }
    return SKIP_BODY;
  }


  public void setPlugin(String plugin) {
    this.plugin = plugin;
  }
}
