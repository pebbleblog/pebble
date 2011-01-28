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

import net.sourceforge.pebble.web.model.Model;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents a view component and prepares the model for display.
 *
 * @author    Simon Brown
 */
public abstract class View {

  private Model model;
  private ServletContext servletContext;

  /**
   * Gets the content type of this view.
   *
   * @return the content type as a String
   */
  public abstract String getContentType();

  /**
   * Gets the model.
   *
   * @return  a Model instance
   */
  public Model getModel() {
    return this.model;
  }

  /**
   * Sets the model.
   *
   * @param model   a Model instance
   */
  public void setModel(Model model) {
    this.model = model;
  }


  /**
   * Gets the ServletContext in which this view is operating.
   *
   * @return    a ServletContext instance
   */
  public ServletContext getServletContext() {
    return servletContext;
  }

  /**
   * Gets the ServletContext in which this view is operating.
   *
   * @param   servletContext    a ServletContext instance
   */
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  /**
   * Prepares the view for presentation.
   */
  public void prepare() {
  }

  /**
   * Dispatches this view.
   *
   * @param request     the HttpServletRequest instance
   * @param response    the HttpServletResponse instance
   * @param context     the ServletContext instance
   */
  public abstract void dispatch(HttpServletRequest request, HttpServletResponse response, ServletContext context)
      throws ServletException;

}
