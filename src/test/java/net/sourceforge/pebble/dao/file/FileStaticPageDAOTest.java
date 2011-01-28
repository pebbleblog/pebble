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

package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.util.FileUtils;

import java.io.File;
import java.util.Locale;

/**
 * Tests for the FileStaticPageDAO class.
 *
 * @author    Simon Brown
 */
public class FileStaticPageDAOTest extends SingleBlogTestCase {

  private StaticPageDAO dao= new FileStaticPageDAO();
  private Locale defaultLocale;

  protected void setUp() throws Exception {
    super.setUp();

    defaultLocale = Locale.getDefault();
    Locale.setDefault(Locale.ENGLISH);
  }


  public void tearDown() throws Exception {
    super.tearDown();

    Locale.setDefault(defaultLocale);
  }

  public void testLoadStaticPageFomFile() throws Exception {

    File source = new File(TEST_RESOURCE_LOCATION, "1152083300843.xml");
    File destination = new File(blog.getRoot(), "pages/1152083300843");
    destination.mkdirs();
    FileUtils.copyFile(source, new File(destination, "1152083300843.xml"));

    StaticPage page = dao.loadStaticPage(blog, "1152083300843");

    // test that the static page properties were loaded okay
    assertEquals("Static page title", page.getTitle());
    assertEquals("Static page subtitle", page.getSubtitle());
    assertEquals("<p>Static page body.</p>", page.getBody());
    assertEquals("some tags", page.getTags());
    assertEquals(1152083300843L, page.getDate().getTime());
    assertEquals("http://pebble.sourceforge.net", page.getOriginalPermalink());
  }

}
