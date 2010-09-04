package net.sourceforge.pebble.api.decorator;

import javax.servlet.http.HttpServletRequest;

/**
 * Context for page decorators
 *
 * @author James Roper
 */
public class PageDecoratorContext {
  private final HttpServletRequest request;

  public PageDecoratorContext(HttpServletRequest request) {
    this.request = request;
  }

  public HttpServletRequest getRequest() {
    return request;
  }
}
