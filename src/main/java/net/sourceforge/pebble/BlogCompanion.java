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
package net.sourceforge.pebble;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.util.I18n;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * Contains properties that can be used by Pebble plugins.
 *
 * @author Pieroxy
 */
public class BlogCompanion {

  /**
   * the log used by this class
   */
  private static final Log log = LogFactory.getLog(BlogCompanion.class);

  /**
   * the value of the companion. Null if not loaded.
   */
  private String content;

  /**
   * the owning blog
   */
  private Blog blog;

  /**
   * Creates a new instance with the specified owning blog.
   *
   * @param blog the owning Blog instance
   */
  public BlogCompanion(Blog blog) {
    this.blog = blog;
  }


  /**
   * Helper method to load the content from disk.
   */
  private void loadFile() {
    content = I18n.getMessage(blog, "blogEntry.defaultCompanion");
    if (content == null) content = "";
    File contentFile = new File(blog.getCompanionFile());
    if (contentFile.exists()) {
      Reader reader = null;
      try {
        reader = new InputStreamReader(new FileInputStream(contentFile), blog.getCharacterEncoding());
        content = IOUtils.toString(reader);
      } catch (IOException e) {
        log.error(e.getMessage());
      } finally {
        IOUtils.closeQuietly(reader);
      }
    }

  }


  /**
   * Gets the content of the companion. Lazily initializes the companion if necessary.
   *
   * @return the content of the companion
   */
  public String getContent() {
    if (content == null) loadFile();
    return content;
  }

  /**
   * Sets the content of the companion.
   *
   * @param value the value of the companion
   */
  public void setContent(String value) {
    content = value;
  }

  /**
   * Helper method to store the companion.
   */
  public synchronized void store() throws IOException {
    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(blog.getCompanionFile()), blog.getCharacterEncoding());
      writer.write(content);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

}