package net.sourceforge.pebble.event.blogentry;

import junit.framework.TestCase;

public class PostToTwitterBlogEntryListenerTest extends TestCase {
	PostToTwitterBlogEntryListener listener = new PostToTwitterBlogEntryListener();
	
	public void testRejectLocalhostUrl() throws Exception {
		assertFalse(listener.checkUrl("http://localhost:8080/pebble/somewhere"));
		assertFalse(listener.checkUrl("http://localhost/pebble/somewhere"));
		assertFalse(listener.checkUrl("https://localhost:8443/pebble/somewhere"));
		assertFalse(listener.checkUrl("https://localhost/pebble/somewhere"));
	}
	
	public void testDontBeTrickedByLocalhost() throws Exception {
		assertTrue(listener.checkUrl("http://localhost.example.com/pebble/something/"));
		assertTrue(listener.checkUrl("https://localhost.example.com/pebble/something/"));
	}
	
	public void testComposeMessageWithLongUrl() throws Exception {
		String title = text(100);
		assertEquals(text(100) + " longUrl", listener.composeMessage(title, "longUrl", "tiny"));
	}
	
	public void testComposeMessageWithShortUrl() throws Exception {
		String title = text(120);
		String longUrl = "longUrl-that-doesn't-fit-into-the-message"; // more than 20 characters
		String tinyUrl = "myTinyUrl-fits";
		assertEquals(text(120) + " myTinyUrl-fits", listener.composeMessage(title, longUrl, tinyUrl));
	}

	public void testShortenMessageTitle() throws Exception {
		String title = text(135);
		String longUrl = "longUrl-that-doesn't-fit-into-the-message"; // more than 20 characters
		String tinyUrl = "myTinyUrl-doesn't-fit-either";
		String message = listener.composeMessage(title, longUrl, tinyUrl);
		assertTrue(message.length() <= 140);
		assertEquals(text(111) + " myTinyUrl-doesn't-fit-either", message);
	}
	
	public void testHelperMethod() throws Exception {
		assertEquals("123456789012", text(12));
	}
	
	private String text(int count) {
		StringBuilder result = new StringBuilder();
		for(int i = 1; i <= count; i++ ) {
			result.append(String.valueOf(i%10));
		}
		return result.toString();
	}
}
