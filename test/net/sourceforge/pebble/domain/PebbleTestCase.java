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

import java.io.File;

/**
 * Superclass for all Pebble unit test cases.
 *
 * @author    Simon Brown
 */
public abstract class PebbleTestCase extends TestCase {

  protected static final File TEST_BLOG_LOCATION;

  protected static final PebbleContext pebbleContext = new PebbleContext();

  static {
    // perform the "global" set up logic
    // create the "test" blog directory
    TEST_BLOG_LOCATION = new File(System.getProperty("java.io.tmpdir"), "pebble");
    TEST_BLOG_LOCATION.mkdir();

    File index = new File(TEST_BLOG_LOCATION, "index");
    index.mkdir();
    File segments = new File(index, "segments");
    segments.mkdir();

    pebbleContext.setDataDirectory(TEST_BLOG_LOCATION.getAbsolutePath());

    // and now register the shutdown hook for tear down logic
    // remove all directories created during the test execution
    Runtime.getRuntime().addShutdownHook(new PebbleTestCaseShutdownHook());
  }

  static class PebbleTestCaseShutdownHook extends Thread {

    public void run() {
      FileUtils.deleteFile(TEST_BLOG_LOCATION);
    }

  }

}