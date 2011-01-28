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
import net.sourceforge.pebble.web.view.impl.FileFormView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Tests for the EditFileAction class.
 *
 * @author    Simon Brown
 */
public class EditFileActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new EditFileAction();

    super.setUp();
  }

  /**
   * Tests that a file can be loaded for editing.
   */
  public void testEditFile() throws Exception {
    File file = new File(blog.getFilesDirectory(), "afile.txt");
    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    writer.write("Testing...");
    writer.flush();
    writer.close();

    request.setParameter("path", "/");
    request.setParameter("name", "afile.txt");
    request.setParameter("type", FileMetaData.BLOG_FILE);

    View view = action.process(request, response);

    // check file information available and the right view is returned
    assertEquals("Testing...", action.getModel().get("fileContent"));
    assertEquals(FileMetaData.BLOG_FILE, action.getModel().get("type"));
    FileMetaData fileMetaData = (FileMetaData)action.getModel().get("file");
    assertEquals("afile.txt", fileMetaData.getName());
    assertEquals("/", fileMetaData.getPath());
    assertTrue(view instanceof FileFormView);

    // and clean up
    file.delete();
  }

  /**
   * Tests that a file can't be loaded for editing from outside of the root.
   */
  public void testEditFileReturnsForbiddenWheOutsideOfRoot() throws Exception {
    request.setParameter("path", "/");
    request.setParameter("name", "../afile.txt");
    request.setParameter("type", FileMetaData.BLOG_FILE);

    View view = action.process(request, response);

    // check a forbidden response is returned
    assertTrue(view instanceof ForbiddenView);
  }

}
