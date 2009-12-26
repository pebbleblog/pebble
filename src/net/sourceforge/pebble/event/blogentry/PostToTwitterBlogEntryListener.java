package net.sourceforge.pebble.event.blogentry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

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
	public static final String tweetURL="https://twitter.com/statuses/update.xml";

	/**
	 * Called when a blog entry has been published.
	 * 
	 * @param event
	 *            a BlogEntryEvent instance
	 */
	public void blogEntryPublished(BlogEntryEvent event) {
		BlogEntry blogEntry = event.getBlogEntry();
		sendNotification(blogEntry);
	}

	private void sendNotification(BlogEntry blogEntry) {
		String twitterUsername = getTwitterUsername(blogEntry);
		String twitterPassword = getTwitterPassword(blogEntry);
		if(twitterUsername == null || twitterPassword == null) {
			log.error("Please configure twitter credentials in order to post to twitter");
		}
		String url = makeTinyURL(blogEntry.getLocalPermalink());
		if (url.equalsIgnoreCase("error"))
			url = blogEntry.getLocalPermalink();
		String msg = composeMessage(blogEntry, url);
		try {
			post(twitterUsername, twitterPassword, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("Blog entry <a href=\"" + blogEntry.getLocalPermalink()
				+ "\">" + blogEntry.getTitle() + "</a> tweeted.");
	}

	private void post(String twitterUsername, String twitterPassword, String msg)
			throws Exception {
		log.info("Posting to Twitter as user " + twitterUsername + ": " + msg);
		URL url = new URL(PostToTwitterBlogEntryListener.tweetURL);
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		String authorization = twitterUsername + ":" + twitterPassword;
		BASE64Encoder encoder = new BASE64Encoder();
		String encoded = new String(encoder.encodeBuffer(authorization.getBytes())).trim();
		connection.setRequestProperty("Authorization", "Basic " + encoded);
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.write("status=" + URLEncoder.encode(msg.substring(
						0, Math.min(140, msg.length())).toString(), "UTF-8"));
		out.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String r;
		StringBuffer response = new StringBuffer();
		while ((r = in.readLine()) != null) {
			response.append(r+"\n");
		}
		in.close();
		response.toString();
	}

	private String composeMessage(BlogEntry blogEntry, String url) {
		return "New Blog Post at " + url + " \"" + blogEntry.getTitle()
				+ "\"";
	}

	private String getTwitterPassword(BlogEntry blogEntry) {
		return getProperty(blogEntry, "password");
	}

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

	public String makeTinyURL(String url) {
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
