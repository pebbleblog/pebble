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
