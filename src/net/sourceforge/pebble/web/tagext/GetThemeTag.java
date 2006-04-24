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
package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.Blog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * A tag providing a way to retrieve the value of the path to the
 * current theme (for example, themes/default) and output it to the page.
 *
 * @author      Simon Brown
 */
public class GetThemeTag extends TagSupport {

  /**
   * Determines the URI to the current theme and outputs it to the page.
   *
   * @return  SKIP_BODY
   * @throws  JspException    if something goes wrong outputting the property
   */
  public int doStartTag() throws JspException {

    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    Blog blog = (Blog)pageContext.getRequest().getAttribute(Constants.BLOG_KEY);
    String theme = "";

    if (blog != null) {
      theme = blog.getTheme();
    } else {
      theme = "default";
    }

    try {
      JspWriter out = pageContext.getOut();
      out.write(request.getContextPath());
      out.write("/themes/" + theme);
    } catch (IOException e) {
      throw new JspTagException(e.getMessage());
    }

    // and skip the body content for this (empty) tag
    return SKIP_BODY;
  }

}