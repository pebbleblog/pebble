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
package net.sourceforge.pebble.web.controller;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.MultiBlog;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.web.action.Action;
import net.sourceforge.pebble.web.action.ActionFactory;
import net.sourceforge.pebble.web.action.ActionNotFoundException;
import net.sourceforge.pebble.web.action.SecureAction;
import net.sourceforge.pebble.web.model.Model;
import net.sourceforge.pebble.web.security.SecurityTokenValidator;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.MultiBlogNotSupportedView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * An implementation of the front controller pattern, using the command
 * and controller strategy.
 *
 * @author Simon Brown
 */
public class DefaultHttpController implements HttpController {

  private static final Log log = LogFactory.getLog(DefaultHttpController.class);

  /**
   * a reference to the factory used to create Action instances
   */
  private ActionFactory actionFactory;

  /**
   * the extension used to refer to actions
   */
  private String actionExtension = ".action";

  /**
   * The security token validator
   */
  @Inject
  private SecurityTokenValidator securityTokenValidator;

  /**
   * Processes the request - this is delegated to from doGet and doPost.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @throws ServletException if an error occured
   * @throws IOException if an error occured writing/reading the response/request
   */
  public void processRequest(HttpServletRequest request,
                                HttpServletResponse response,
                                ServletContext servletContext)
          throws ServletException, IOException {

    AbstractBlog blog = (AbstractBlog) request.getAttribute(Constants.BLOG_KEY);

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
      boolean validated = securityTokenValidator.validateSecurityToken(request, response, action);
      if (!validated) {
        // Forward to no security url
        request.getRequestDispatcher("/noSecurityToken.action").forward(request, response);
      } else {
        try {
          Model model = new Model();
          model.put(Constants.BLOG_KEY, blog);
          model.put(Constants.BLOG_URL, blog.getUrl());
          action.setModel(model);
          View view;
          try {
            view = action.process(request, response);
          } catch (ClassCastException cce) {
            // PEBBLE-45 Actions intended for single blog mode should fail nicely.  This is a simple method that will
            // allow has to handle all actions with minimal effort
            if (cce.getMessage().contains(MultiBlog.class.getName()) && cce.getMessage().contains(Blog.class.getName())) {
              view = new MultiBlogNotSupportedView();
            } else {
              throw cce;
            }
          }
          if (view != null) {

            view.setModel(model);
            view.setServletContext(servletContext);

            view.prepare();

            for (Object key : model.keySet()) {
              request.setAttribute(key.toString(), model.get(key.toString()));
            }

            response.setContentType(view.getContentType());
            view.dispatch(request, response, servletContext);

          }
        } catch (Exception e) {
          request.setAttribute("exception", e);
          throw new ServletException(e);
        }
      }
    }
  }

  private boolean isAuthorised(HttpServletRequest request, Action action) {
    if (action instanceof SecureAction) {
      SecureAction secureAction = (SecureAction) action;
      return isUserInRole(request, secureAction);
    } else {
      return true;
    }
  }

  /**
   * Determines whether the current user in one of the roles specified
   * by the secure action.
   *
   * @param request the HttpServletRequest
   * @param action  the SecureAction to check against
   * @return true if the user is in one of the roles, false otherwise
   */
  private boolean isUserInRole(HttpServletRequest request, SecureAction action) {
    AbstractBlog ab = (AbstractBlog) request.getAttribute(Constants.BLOG_KEY);
    String currentUser = SecurityUtils.getUsername();
    String roles[] = action.getRoles(request);
    for (String role : roles) {
      if (role.equals(Constants.ANY_ROLE)) {
        return true;
      } else if (SecurityUtils.isUserInRole(role)) {
        if (ab instanceof Blog) {
          Blog blog = (Blog) ab;
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

  public void setActionFactory(ActionFactory actionFactory) {
    this.actionFactory = actionFactory;
  }

  public void setActionExtension(String actionExtension) {
    this.actionExtension = actionExtension;
  }
}
