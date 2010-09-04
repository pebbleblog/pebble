package net.sourceforge.pebble.security;

import org.acegisecurity.AuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Overidden to redirect the user to the blog that they were trying to log in to.
 *
 * @author    Simon Brown
 */
public class AuthenticationProcessingFilter extends org.acegisecurity.ui.webapp.AuthenticationProcessingFilter {

  private static final Log log = LogFactory.getLog(AuthenticationProcessingFilter.class);

  /**
   * Overidden to set the value of the defaultTargetUrl property based upon a form parameter.
   *
   * @param request
   * @param response
   * @throws AuthenticationException
   * @throws IOException
   */
  protected void onPreAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
//    PebbleContext context = PebbleContext.getInstance();
//    if (context.getConfiguration().isVirtualHostingEnabled()) {
//      Cookie cookie = CookieUtils.getCookie(request.getCookies(), TokenBasedRememberMeServices.ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY);
//      if (cookie != null) {
//        cookie.setDomain("." + context.getConfiguration().getDomainName());
//        response.addCookie(cookie);
//      }
//    }
//
    setDefaultTargetUrl(request.getParameter("redirectUrl"));
    super.onPreAuthentication(request, response);
  }

}