package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.web.controller.HttpController;
import net.sourceforge.pebble.web.security.SecurityTokenValidator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Tag that writes the security token as a hidden input parameter to the request
 * @author James Roper
 */
public class SecurityTokenTag extends TagSupport {

  /** true if we're rendering for a query */
  private boolean query;

  @Override
  public int doStartTag() throws JspException {
    JspWriter out = pageContext.getOut();
    String token = (String) pageContext.getRequest().getAttribute(SecurityTokenValidator.PEBBLE_SECURITY_TOKEN_PARAMETER);
    if (token != null) {
      try {
        if (query) {
          out.append(SecurityTokenValidator.PEBBLE_SECURITY_TOKEN_PARAMETER).append("=").append(token);
        } else {
          out.append("<input type=\"hidden\" name=\"").append(SecurityTokenValidator.PEBBLE_SECURITY_TOKEN_PARAMETER);
          out.append("\" value=\"").append(token).append("\"/>");
        }
      } catch (IOException ioe) {
        throw new JspException(ioe);
      }
    }
    return SKIP_BODY;
  }

  public void setQuery(boolean query) {
    this.query = query;
  }
}
