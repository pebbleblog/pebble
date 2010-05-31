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
package net.sourceforge.pebble.domain;

import junit.framework.TestCase;
import net.sourceforge.pebble.util.FileUtils;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.Configuration;

import java.io.File;

/**
 * Superclass for all Pebble unit test cases.
 *
 * @author    Simon Brown
 */
public abstract class PebbleTestCase extends TestCase {

  protected static final File TEST_BLOG_LOCATION;

  static {
    TEST_BLOG_LOCATION = new File(System.getProperty("java.io.tmpdir"), "pebble");
  }

  protected void setUp() throws Exception {
    super.setUp();

    TEST_BLOG_LOCATION.mkdir();
    new File(TEST_BLOG_LOCATION, "blogs").mkdir();

    Configuration config = new Configuration();
    config.setUrl("http://www.yourdomain.com/blog/");
    config.setDataDirectory(TEST_BLOG_LOCATION.getAbsolutePath());
    PebbleContext.getInstance().setConfiguration(config);
  }

  protected void tearDown() throws Exception {
    FileUtils.deleteFile(TEST_BLOG_LOCATION);

    super.tearDown();
  }

}