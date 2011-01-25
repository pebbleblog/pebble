package net.sourceforge.pebble.web.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Responds to HTTP pings, for keeping the session alive
 *
 * @author James Roper
 */
public class PingServlet extends HttpServlet {
  private static final Log log = LogFactory.getLog(PingServlet.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session == null) {
      log.debug("Received ping with no session");
    } else {
      log.debug("Received ping from session " + session.getId());
    }
    resp.setContentType("text/plain");
    resp.getWriter().write("Pong response at " + new Date().toString());
  }
}
