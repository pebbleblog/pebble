package net.sourceforge.pebble.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author James Roper
 */
public class OpenIdAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  public void onAuthenticationFailure(HttpServletRequest request,
                                      HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
    String errorMessage;
    if (exception instanceof UsernameNotFoundException) {
      errorMessage = "openid.not.mapped";
    } else {
      errorMessage = "openid.error";
    }
    redirectStrategy.sendRedirect(request, response, "/loginPage.action?error=" + errorMessage);
  }
}
