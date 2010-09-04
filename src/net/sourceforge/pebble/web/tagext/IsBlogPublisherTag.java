package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.util.SecurityUtils;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

/**
 * A custom tag that includes its body content if the current user belongs to
 * the "blog publisher" role for the blog.
 *
 * @author    Simon Brown
 */
public class IsBlogPublisherTag extends TagSupport {

  /**
   * Implementation from the Tag interface - this is called when the opening tag
   * is encountered.
   *
   * @return  an integer specifying what to do afterwards
   * @throws  javax.servlet.jsp.JspException    if something goes wrong
   */
  public int doStartTag() throws JspException {
    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    AbstractBlog abstractBlog = (AbstractBlog)request.getAttribute(Constants.BLOG_KEY);

    if (abstractBlog instanceof Blog) {
      Blog blog = (Blog)abstractBlog;
      if (SecurityUtils.isUserAuthorisedForBlogAsBlogPublisher(blog)) {
        return EVAL_BODY_INCLUDE;
      }
    }

    return SKIP_BODY;
  }

}
