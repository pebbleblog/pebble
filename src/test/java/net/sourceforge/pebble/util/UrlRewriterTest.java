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

package net.sourceforge.pebble.util;

import junit.framework.TestCase;

/**
 * The HttpsUrlRewriter implementation is actually not tested, as it 
 * delegates its functionality to another utility class, depends upon
 * configuration and is badly testable. This test makes sure that the
 * mechanics of using the currently configured threadlocal 
 * implementation - be it whatever it might be - is used. 
 * @author Olaf Kock
 */

public class UrlRewriterTest extends TestCase {
	private static final String HTTP_URL = "http://pebble.sf.net";
	private static final String HTTPS_URL = "https://sf.net/projects/pebble";

	public void testDefaultDoesntRewrite() throws Exception {
		assertEquals(HTTP_URL, UrlRewriter.doRewrite(HTTP_URL));
	}

	public void testMockRewriterIsUsed() {
		MockRewriter mockRewriter = new MockRewriter(HTTP_URL, HTTPS_URL);
		UrlRewriter.useThisRewriter(mockRewriter);
		assertEquals(HTTPS_URL, UrlRewriter.doRewrite(HTTP_URL));
		UrlRewriter.clear();
		assertEquals(HTTP_URL, UrlRewriter.doRewrite(HTTP_URL));
		assertEquals(1, mockRewriter.count);
	}

	public void testThreadedUsage() throws InterruptedException {
		MockRewriter m1 = new MockRewriter("s1", "u1");
		MockRewriter m2 = new MockRewriter("s2", "u2");
		MockRewriter m3 = new MockRewriter("s3", "u3");
		Thread t1 = new Thread(new UrlRewriteTestRunnable(m1));
		Thread t2 = new Thread(new UrlRewriteTestRunnable(m2));
		Thread t3 = new Thread(new UrlRewriteTestRunnable(m3));
		t1.start();
		t2.start();
		t3.start();
		t1.join(1000);
		t2.join(1000);
		t3.join(1000);
		assertEquals(1, m1.count);
		assertEquals(1, m2.count);
		assertEquals(1, m3.count);
	}
	
	static void sleep() {
		try { Thread.sleep(500); } catch (InterruptedException ignore) {}
	}
	
	/**
	 * Test the usage of the correct thread local rewriter. This is hacked by
	 * all Threads waiting half a second before the actual rewrite - hopefully 
	 * this will be enough to make sure that the most obvious race conditions
	 * are met.
	 */
	
	static class UrlRewriteTestRunnable implements Runnable {
		public boolean done = false;
		private final MockRewriter m;
		
		public UrlRewriteTestRunnable(MockRewriter m) {
			this.m = m;
		}
		
		public void run() {
			UrlRewriter.useThisRewriter(m);
			sleep();
			assertEquals(m.result, UrlRewriter.doRewrite(m.expectation));
		}
	}
	
	/**
	 * Very simple Mock implementation without external dependencies
	 * to a mocking library...
	 */
	static class MockRewriter extends UrlRewriter {
		private final String expectation;
		private final String result;
		public int count = 0;
		
		public MockRewriter(String expectation, String result) {
			this.expectation = expectation;
			this.result = result;
		}
		
		@Override
		public String rewrite(String url) {
			assertEquals(expectation, url);
			count++;
			return result;
		}
	}
}
