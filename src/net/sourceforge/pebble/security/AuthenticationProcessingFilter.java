package net.sourceforge.pebble.security;

import org.acegisecurity.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Overidden to redirect the user to the blog that they were trying to log in to.
 *
 * @author    Simon Brown
 */
public class AuthenticationProcessingFilter extends org.acegisecurity.ui.webapp.AuthenticationProcessingFilter {

  /**
   * Overidden to set the value of the defaultTargetUrl property based upon a form parameter.
   *
   * @param request
   * @param response
   * @throws AuthenticationException
   * @throws IOException
   */
  protected void onPreAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
    setDefaultTargetUrl(request.getParameter("redirectUrl"));
    super.onPreAuthentication(request, response);
  }

}