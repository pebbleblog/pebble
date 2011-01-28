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

import net.sourceforge.pebble.domain.FileMetaData;
import net.sourceforge.pebble.web.view.ForbiddenView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.RedirectView;

import java.io.File;

/**
 * Tests for the CreateDirectoryAction class.
 *
 * @author    Simon Brown
 */
public class CreateDirectoryActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new CreateDirectoryAction();

    super.setUp();
  }

  /**
   * Tests that a new directory can be created.
   */
  public void testCreateDirectory() throws Exception {
    File file = new File(blog.getImagesDirectory(), "newdirectory");
    assertFalse("File already exists", file.exists());

    request.setParameter("path", "/");
    request.setParameter("name", "newdirectory");
    request.setParameter("type", FileMetaData.BLOG_IMAGE);

    View view = action.process(request, response);

    // check file now exists and the right view is returned
    file = new File(blog.getImagesDirectory(), "newdirectory");
    assertTrue("File doesn't exist", file.exists());
    assertTrue(view instanceof RedirectView);

    // and clean up
    file.delete();
  }

  /**
   * Tests that a new directory can't be created when it is outside of the root.
   */
  public void testCreateDirectoryReturnsForbiddenWheOutsideOfRoot() throws Exception {
    request.setParameter("path", "/");
    request.setParameter("name", "../newdirectory");
    request.setParameter("type", FileMetaData.BLOG_IMAGE);

    View view = action.process(request, response);

    // check a forbidden response is returned
    assertTrue(view instanceof ForbiddenView);
  }

}
