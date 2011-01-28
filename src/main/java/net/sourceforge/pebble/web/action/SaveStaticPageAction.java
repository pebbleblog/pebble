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
package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.service.StaticPageService;
import net.sourceforge.pebble.service.StaticPageServiceException;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.StaticPageFormView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Saves a static page.
 *
 * @author    Simon Brown
 */
@RequireSecurityToken
public class SaveStaticPageAction extends SecureAction {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(SaveStaticPageAction.class);

  /** the value used if the page is being previewed rather than saved */
  private static final String PREVIEW = "Preview";
  private static final String CANCEL = "Cancel";

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    String submitType = request.getParameter("submit");

    if (submitType != null && submitType.equalsIgnoreCase(PREVIEW)) {
      return previewPage(request);
    } else if (submitType != null && submitType.equalsIgnoreCase(CANCEL)) {
      return unlockPage(request);
    } else {
      return savePage(request);
    }
  }

  private View previewPage(HttpServletRequest request) throws ServletException {
    StaticPage staticPage = getStaticPage(request);

    // we don't want to actually edit the original whilst previewing
    staticPage = (StaticPage)staticPage.clone();
    populateStaticPage(staticPage, request);

    ValidationContext validationContext = new ValidationContext();
    staticPage.validate(validationContext);
    getModel().put("validationContext", validationContext);
    getModel().put(Constants.STATIC_PAGE_KEY, staticPage);

    return new StaticPageFormView();
  }

  private View unlockPage(HttpServletRequest request) throws ServletException {
    StaticPage staticPage = getStaticPage(request);
    StaticPageService service = new StaticPageService();
    service.unlock(staticPage);

    if (staticPage.isPersistent()) {
      return new RedirectView(staticPage.getLocalPermalink());
    } else {
      return new RedirectView(staticPage.getBlog().getUrl() + "viewStaticPages.secureaction");
    }
  }

  private View savePage(HttpServletRequest request) throws ServletException {
    StaticPageService service = new StaticPageService();
    StaticPage staticPage = getStaticPage(request);
    populateStaticPage(staticPage, request);
    getModel().put(Constants.STATIC_PAGE_KEY, staticPage);

    ValidationContext validationContext = new ValidationContext();
    staticPage.validate(validationContext);

    if (validationContext.hasErrors())  {
      getModel().put("validationContext", validationContext);
      return new StaticPageFormView();
    } else {
      try {
        service.putStaticPage(staticPage);
        staticPage.getBlog().info("Static page <a href=\"" + staticPage.getLocalPermalink() + "\">" + staticPage.getTitle() + "</a> saved.");
        service.unlock(staticPage);
        return new RedirectView(staticPage.getLocalPermalink());
      } catch (StaticPageServiceException e) {
        log.error(e.getMessage(), e);

        return new StaticPageFormView();
      }
    }
  }

  private StaticPage getStaticPage(HttpServletRequest request) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String id = request.getParameter("page");
    String persistent = request.getParameter("persistent");

    if (persistent != null && persistent.equalsIgnoreCase("true")) {
      try {
        StaticPageService service = new StaticPageService();
        return service.getStaticPageById(blog, id);
      } catch (StaticPageServiceException e) {
        throw new ServletException(e);
      }
    } else {
      return new StaticPage(blog);
    }
  }

  private void populateStaticPage(StaticPage staticPage, HttpServletRequest request) {
    String title = request.getParameter("title");
    String subtitle = request.getParameter("subtitle");
    String body = StringUtils.filterNewlines(request.getParameter("body"));
    String tags = request.getParameter("tags");
    String originalPermalink = request.getParameter("originalPermalink");
    String name = request.getParameter("name");
    String author = SecurityUtils.getUsername();
    String template = request.getParameter("template");

    staticPage.setTitle(title);
    staticPage.setSubtitle(subtitle);
    staticPage.setBody(body);
    staticPage.setTags(tags);
    staticPage.setAuthor(author);
    staticPage.setOriginalPermalink(originalPermalink);
    staticPage.setName(name);
    staticPage.setTemplate(template);
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{Constants.BLOG_CONTRIBUTOR_ROLE};
  }

}
