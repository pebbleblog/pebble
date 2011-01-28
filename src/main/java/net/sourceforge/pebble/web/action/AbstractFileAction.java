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
import net.sourceforge.pebble.domain.FileMetaData;

import javax.servlet.http.HttpServletRequest;

/**
 * Superclass for file manipulation actions.
 *
 * @author    Simon Brown
 */
public abstract class AbstractFileAction extends SecureAction {

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
  public String[] getRoles(HttpServletRequest request) {
    String type = request.getParameter("type");

    // the roles permitted to access this action depend on the
    // type of file that is being manipulated (viewed, edited, etc)
    if (type != null && type.equals(FileMetaData.BLOG_IMAGE)) {
      return new String[]{Constants.BLOG_CONTRIBUTOR_ROLE};
    } else if (type != null && type.equals(FileMetaData.BLOG_FILE)) {
      return new String[]{Constants.BLOG_CONTRIBUTOR_ROLE};
    } else if (type != null && type.equals(FileMetaData.THEME_FILE)) {
      return new String[]{Constants.BLOG_ADMIN_ROLE, Constants.BLOG_OWNER_ROLE};
    } else if (type != null && type.equals(FileMetaData.BLOG_DATA)) {
      return new String[]{Constants.BLOG_ADMIN_ROLE, Constants.BLOG_OWNER_ROLE};
    } else {
      return new String[]{};
    }
  }

}