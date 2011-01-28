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
