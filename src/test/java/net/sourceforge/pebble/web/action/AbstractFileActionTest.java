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

/**
 * Tests for the AbstractFileAction class.
 *
 * @author    Simon Brown
 */
public class AbstractFileActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new ViewFilesAction();

    super.setUp();
  }

  /**
   * Tests that only blog contributors can manage blog images.
   */
  public void testSecurityRolesForBlogImages() {
    request.setParameter("type", FileMetaData.BLOG_IMAGE);
    String roles[] = action.getRoles(request);

    assertEquals(1, roles.length);
    assertEquals(Constants.BLOG_CONTRIBUTOR_ROLE, roles[0]);
  }

  /**
   * Tests that only blog contributors can manage blog files.
   */
  public void testSecurityRolesForBlogFiles() {
    request.setParameter("type", FileMetaData.BLOG_FILE);
    String roles[] = action.getRoles(request);

    assertEquals(1, roles.length);
    assertEquals(Constants.BLOG_CONTRIBUTOR_ROLE, roles[0]);
  }

  /**
   * Tests that only blog owners can manage theme files.
   */
  public void testSecurityRolesForThemeFiles() {
    request.setParameter("type", FileMetaData.THEME_FILE);
    String roles[] = action.getRoles(request);

    assertEquals(2, roles.length);
    assertEquals(Constants.BLOG_ADMIN_ROLE, roles[0]);
    assertEquals(Constants.BLOG_OWNER_ROLE, roles[1]);
  }

  /**
   * Tests that only blog owners can manage blog data.
   */
  public void testSecurityRolesForBlogData() {
    request.setParameter("type", FileMetaData.BLOG_DATA);
    String roles[] = action.getRoles(request);

    assertEquals(2, roles.length);
    assertEquals(Constants.BLOG_ADMIN_ROLE, roles[0]);
    assertEquals(Constants.BLOG_OWNER_ROLE, roles[1]);
  }

  /**
   * Tests that no roles are returned if the type is unknown.
   */
  public void testSecurityRolesForNonExistentFIleType() {
    request.setParameter("type", "something");
    String roles[] = action.getRoles(request);

    assertEquals(0, roles.length);
  }

}
