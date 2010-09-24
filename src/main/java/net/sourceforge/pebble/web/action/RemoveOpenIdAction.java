package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.security.SecurityRealmException;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author James Roper
 */
@RequireSecurityToken
public class RemoveOpenIdAction extends SecureAction {
  @Override
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{Constants.ANY_ROLE};
  }

  @Override
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    PebbleUserDetails userDetails = SecurityUtils.getUserDetails();
    AbstractBlog blog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);

    String openId = request.getParameter("openid");
    try {
      PebbleContext.getInstance().getConfiguration().getSecurityRealm().removeOpenIdFromUser(userDetails, openId);
    } catch (SecurityRealmException sre) {
      throw new ServletException(sre);
    }
    return new RedirectView(blog.getUrl() + "/editUserPreferences.secureaction");
  }
}
