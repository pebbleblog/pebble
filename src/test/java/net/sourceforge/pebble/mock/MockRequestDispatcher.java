package net.sourceforge.pebble.mock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: simon
 * Date: Nov 24, 2003
 * Time: 10:08:36 PM
 * To change this template use Options | File Templates.
 */
public class MockRequestDispatcher implements RequestDispatcher {

  private String uri;

  private boolean forwarded = false;
  private boolean included = false;

  public MockRequestDispatcher(String uri) {
    this.uri = uri;
  }

  public String getUri() {
    return this.uri;
  }

  public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    this.forwarded = true;
  }

  public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    this.included = true;
  }

  public boolean wasForwarded() {
    return this.forwarded;
  }

  public boolean wasIncluded() {
    return this.included;
  }

}
