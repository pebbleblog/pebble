/*
 * Copyright (c) 2003-2006, Simon Brown
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
package net.sourceforge.pebble.web.controller;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.util.Utilities;
import net.sourceforge.pebble.web.action.Action;
import net.sourceforge.pebble.web.action.ActionFactory;
import net.sourceforge.pebble.web.action.ActionNotFoundException;
import net.sourceforge.pebble.web.action.SecureAction;
import net.sourceforge.pebble.web.model.Model;
import net.sourceforge.pebble.web.view.View;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * An implementation of the front controller pattern, using the command
 * and controller strategy.
 *
 * @author    Simon Brown
 */
public class HttpController extends HttpServlet {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(HttpController.class);

  /** a reference to the factory used to create Action instances */
  private ActionFactory actionFactory;

  /** the extension used to refer to actions */
  private String actionExtension = ".action";

  /**
   * Initialises this instance.
   */
  public void init() {
    String actions = getServletConfig().getInitParameter("actions");
    this.actionExtension = getServletConfig().getInitParameter("actionExtension");
    this.actionFactory = new ActionFactory(actions);
  }

  /**
   * Processes the request - this is delegated to from doGet and doPost.
   *
   * @param request   the HttpServletRequest instance
   * @param response   the HttpServletResponse instance
   */
  protected void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
      throws ServletException, IOException {

    AbstractBlog blog = (AbstractBlog)request.getAttribute(Constants.BLOG_KEY);

    // find which action should be used
    String actionName = request.getRequestURI();
    if (actionName.indexOf("?") > -1) {
      // strip of the query string - some servers leave this on
      actionName = actionName.substring(0, actionName.indexOf("?"));
    }
    int index = actionName.lastIndexOf("/");
    actionName = actionName.substring(index + 1, (actionName.length() - actionExtension.length()));
    Action action;

    try {
      log.debug("Action is " + actionName);
      action = actionFactory.getAction(actionName);
    } catch (ActionNotFoundException anfe) {
      log.warn(anfe.getMessage());
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    boolean authorised = isAuthorised(request, action);
    if (!authorised) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    } else {
      try {
        Model model = new Model();
        model.put(Constants.BLOG_KEY, blog);
        String calculatedBaseUrl = Utilities.calcBaseUrl(request.getScheme(), blog.getUrl());
		model.put(Constants.BLOG_URL, blog.getUrl());
        action.setModel(model);
        View view = action.process(request, response);
        if (view != null) {

          view.setModel(model);
          view.setServletContext(this.getServletContext());

          view.prepare();

          for (Object key : model.keySet()) {
            request.setAttribute(key.toString(), model.get(key.toString()));
          }

          response.setContentType(view.getContentType());
          view.dispatch(request, response, getServletContext());

        }
      } catch (Exception e) {
        request.setAttribute("exception", e);
        throw new ServletException(e);
      }
    }
  }

  private boolean isAuthorised(HttpServletRequest request, Action action) {
    if (action instanceof SecureAction) {
      SecureAction secureAction = (SecureAction)action;
      return isUserInRole(request, secureAction);
    } else {
      return true;
    }
  }

  /**
   * Determines whether the current user in one of the roles specified
   * by the secure action.
   *
   * @param request   the HttpServletRequest
   * @param action    the SecureAction to check against
   * @return  true if the user is in one of the roles, false otherwise
   */
  private boolean isUserInRole(HttpServletRequest request, SecureAction action) {
    AbstractBlog ab = (AbstractBlog)request.getAttribute(Constants.BLOG_KEY);
    String currentUser = SecurityUtils.getUsername();
    String roles[] = action.getRoles(request);
    for (String role : roles) {
      if (role.equals(Constants.ANY_ROLE)) {
        return true;
      } else if (SecurityUtils.isUserInRole(role)) {
        if (ab instanceof Blog) {
          Blog blog = (Blog)ab;
          if (blog.isUserInRole(role, currentUser)) {
            return true;
          }
        } else {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * A default implementation of doGet that delegates to the processRequest method.
   *
   * @param req   the HttpServletRequest instance
   * @param res   the HttpServletResponse instance
   */
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    processRequest(req, res);
  }

  /**
   * A default implementation of doPost that delegates to the processRequest method.
   *
   * @param req   the HttpServletRequest instance
   * @param res   the HttpServletResponse instance
   */
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    processRequest(req, res);
  }

}
