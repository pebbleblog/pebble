package net.sourceforge.pebble.api.decorator;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Interface implemented by page decorators.  Can be used for plugins to add custom HTML to the head, top of the page,
 * and bottom of the page
 *
 * @author James Roper
 */
public interface PageDecorator {

  /**
   * Decorate the head section of the page
   *
   * @param out The writer to write the decorations out to
   * @param pageDecoratorContext The context
   */
  void decorateHead(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException;

  /**
   * Decorate the header (ie, just after the body tag start) section of the page
   *
   * @param out The writer to write the decorations out to
   * @param pageDecoratorContext The context
   */
  void decorateHeader(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException;

  /**
   * Decorate the footer section of the page (ie, just before the body tag end)
   *
   * @param out The writer to write the decorations out to
   * @param pageDecoratorContext The context
   */
  void decorateFooter(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException;
}
