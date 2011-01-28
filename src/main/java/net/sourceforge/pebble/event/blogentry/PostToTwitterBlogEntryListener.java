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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import twitter4j.Twitter;

/**
 * Post new blog entries to twitter.
 * This class is based on a patch by Steve Carton (PEBBLE-15), but changes it to use the
 * pebble plugin mechanism by implementing the {@link BlogEntryListener} interface
 * 
 * @author Steve Carton, Olaf Kock
 */
public class PostToTwitterBlogEntryListener extends BlogEntryListenerSupport {

	/** the log used by this class */
	private static final Log log = LogFactory
			.getLog(PostToTwitterBlogEntryListener.class);
	private static final String DEFAULT_TWEET_URL="https://twitter.com/";

	/**
	 * Called when a blog entry has been published.
	 * 
	 * @param event
	 *            a BlogEntryEvent instance
	 */
	public void blogEntryPublished(BlogEntryEvent event) {
		BlogEntry blogEntry = event.getBlogEntry();
		String twitterUsername = getTwitterUsername(blogEntry);
		String twitterPassword = getTwitterPassword(blogEntry);
		String twitterUrl = getTwitterUrl(blogEntry);
		if(twitterUsername == null || twitterPassword == null) {
			blogEntry.getBlog().error("Please configure twitter credentials in order to post to twitter");
			return;
		}
		String longUrl = blogEntry.getLocalPermalink();
//		if(!checkUrl(longUrl)) {
//			blogEntry.getBlog().error("cowardly refusing to post url '" + longUrl + "' to twitter");
//			return;
//		}
		String tinyUrl = makeTinyURL(longUrl);
		if (tinyUrl.equalsIgnoreCase("error"))
			tinyUrl = longUrl;
		String msg = composeMessage(blogEntry.getTitle(), longUrl, tinyUrl);
		try {
			if(getProperty(blogEntry, "simulate") != null) {
				blogEntry.getBlog().info("Found property 'twitter.simulate' - This would have been posted to twitter with username '" + twitterUsername + "':\n" + msg);
			} else {
				post(twitterUrl, twitterUsername, twitterPassword, msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("Blog entry <a href=\"" + longUrl
				+ "\">" + blogEntry.getTitle() + "</a> tweeted.");
	}

	/**
	 * make sure that the url we are about to post is one that makes sense to post - e.g. don't post 'localhost' urls.
	 * @param longUrl
	 * @return true if the url passes the (simple) tests and may be posted
	 */
	boolean checkUrl(String longUrl) {
		return ! (longUrl.contains("://localhost:") || longUrl.contains("://localhost/"));
	}

	/**
	 * combine message with url. This will use longUrl when enough characters are left, tinyUrl otherwise.
	 * If necessary, the title will be shortened in order to include the full URL
	 * @param title
	 * @param longUrl
	 * @param tinyUrl
	 * @return the "up to 140 character" message to post to twitter
	 */
	String composeMessage(String title, String longUrl, String tinyUrl) {
		if(longUrl.length() + title.length() > 139 ) {
			if(tinyUrl.length() + title.length() > 139) {
				return title.substring(0, 139-tinyUrl.length()) + " " + tinyUrl;
			} else {
				return title + " " + tinyUrl;
			}
		} else {
			return title + " " + longUrl ;
		}
	}

	/**
	 * get twitter URL to post to. This can be overridden with the blog property twitter.url - e.g. for testing purposes.
	 * @param blogEntry
	 * @return
	 */
	private String getTwitterUrl(BlogEntry blogEntry) {
		String twitterUrl = getProperty(blogEntry, "url");
		if(twitterUrl == null) twitterUrl = DEFAULT_TWEET_URL;
		return twitterUrl;
	}

	/**
	 * the password to post to twitter as configured in the blog properties
	 * @param blogEntry
	 * @return
	 */
	private String getTwitterPassword(BlogEntry blogEntry) {
		return getProperty(blogEntry, "password");
	}

	/**
	 * the username to post to twitter as configured in the blog properties
	 * @param blogEntry
	 * @return
	 */
	private String getTwitterUsername(BlogEntry blogEntry) {
		return getProperty(blogEntry, "username");
	}
	
	private String getProperty(BlogEntry blogEntry, String property) {
		Blog blog = blogEntry.getBlog();
		String blogName = blog.getName();
		PluginProperties pluginProperties = blog.getPluginProperties();
		String result = pluginProperties.getProperty("twitter." + blogName + "." + property);
		if(result == null) {
			result = pluginProperties.getProperty("twitter." + property);
			if(result == null) {
				log.error("Twitter credentials (" + property + ") not found. Please configure twitter." + property + " in order to post to twitter");
			} else {
				log.debug("found twitter credentials in twitter." + property );
			}
		} else {
			log.debug("found twitter credentials in twitter." + blogName + "." + property);
		}
		return result;
	}

	/**
	 * Post the given message to twitter, using the given postUrl and credentials.
	 * @param twitterUrl URL to post to
	 * @param twitterUsername username to post as
	 * @param twitterPassword password to authenticate username
	 * @param msg the message to post to twitter.
	 * @throws Exception
	 */
	private void post(String twitterUrl, String twitterUsername,
			String twitterPassword, String msg) throws Exception {
		System.out.println("Posting to Twitter: " + msg);
		Twitter twitter = new Twitter(twitterUsername, twitterPassword, twitterUrl);
		twitter.updateStatus(msg);
	}


	/**
	 * create a shortened version of the given url
	 * @param url
	 * @return
	 */
	private String makeTinyURL(String url) {
		// http://tinyurl.com/api-create.php?...
		StringBuffer response = new StringBuffer();
		try {
			URL turl = new URL("http://tinyurl.com/api-create.php?"+url);
			URLConnection connection = turl.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(false);
			connection.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String r;
			while ((r = in.readLine()) != null) {
				response.append(r);
			}
			in.close();
		}
		catch (MalformedURLException e) {
			log.error(e.getMessage());
			return url;
		}
		catch (IOException e) {
			log.error(e.getMessage());
			return url;
		}
		log.debug("tinyurl for " + url + " is " + response.toString());
		return response.toString();
	}
}
