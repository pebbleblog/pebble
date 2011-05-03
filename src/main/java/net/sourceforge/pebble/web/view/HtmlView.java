/*
 * Copyright (c) 2003-2011, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sourceforge.pebble.web.view;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.util.I18n;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a HTML view component, implemented by a JSP
 * and prepares the model for display.
 *
 * @author    Simon Brown
 */
public abstract class HtmlView extends JspView {

  public static final String SYSTEM_THEME = "_pebble";
  public static final String DEFAULT_THEME = "default";

  private static Log log = LogFactory.getLog(HtmlView.class);

  /**
   * Gets the title of this view.
   *
   * @return  the title as a String
   */
  public String getContentType() {
    AbstractBlog blog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);
    return "text/html; charset=" + blog.getCharacterEncoding();
  }

  /**
   * Gets the title of this view.
   *
   * @return  the title as a String
   */
  public abstract String getTitle();

  public String getLocalizedString(String key) {
	  return I18n.getMessage(((AbstractBlog)getModel().get(Constants.BLOG_KEY)).getLocale(), key);
  }
  
  
  /**
   * Gets the name of the theme to use.
   *
   * @return  the theme name as a String
   */
  protected String getTheme() {
    AbstractBlog blog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);
    String theme = blog.getTheme();

    if (!PebbleContext.getInstance().getConfiguration().isUserThemesEnabled()) {
      if (theme != null && !theme.equals(DEFAULT_THEME) && !theme.equals(SYSTEM_THEME)) {
        return DEFAULT_THEME;
      }
    }

    return theme;
  }

  protected int getStatus() {
    return HttpServletResponse.SC_OK;
  }

  /**
   * Dispatches this view.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @param context
   */
  public void dispatch(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws ServletException {
    String theme = getTheme();
    request.setAttribute(Constants.THEME, theme);
    request.setAttribute(Constants.TITLE_KEY, getTitle());
    log.debug("Content is " + getUri());
    request.setAttribute("content", getUri());
    String headUri = "/themes/" + theme + "/head.jsp";
    if (hasThemeHeadUri(headUri)) {
      request.setAttribute("themeHeadUri", headUri);
    }
    String uri = "/themes/" + theme + "/" + getTemplate() + ".jsp";
    log.debug("Dispatching to " + uri);

    response.setHeader("Cache-Control","no-cache, no-store");
    response.setDateHeader("Expires", 0);
    response.setHeader("Pragma","no-cache");

    try {
      RequestDispatcher dispatcher = context.getRequestDispatcher(uri);
      dispatcher.forward(request, response);
    } catch (IOException ioe) {
      ioe.printStackTrace();
      throw new ServletException(ioe);
    } finally {
      AbstractBlog blog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);
      blog.log(request, getStatus());
    }
  }

  private String getTemplate() {
    if (!(getModel().get(Constants.BLOG_KEY) instanceof Blog)) {
      return "template";
    }
    if (!getModel().contains(Constants.STATIC_PAGE_KEY)) {
      return "template";
    }
    StaticPage staticPage = (StaticPage) getModel().get(Constants.STATIC_PAGE_KEY);
    Blog blog = (Blog) getModel().get(Constants.BLOG_KEY);
    String templateFile = blog.getThemeDirectory() + "/" + staticPage.getTemplate() + ".jsp";
    if (new File(templateFile).canRead()) {
      return staticPage.getTemplate();
    }
    return "template";
  }

  private boolean hasThemeHeadUri(String headUri) {
    return new File(getServletContext().getRealPath(headUri)).canRead();
  }
}
