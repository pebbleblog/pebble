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
package net.sourceforge.pebble.web.view.impl;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.decorator.ContentDecoratorChain;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.web.view.View;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Abstract view from which all ROME feeds are rendered.
 *
 * @author James Roper
 */
public abstract class AbstractRomeFeedView extends View {

  private final FeedType feedType;
  private final SimpleDateFormat idDateFormat;

  protected AbstractRomeFeedView(FeedType feedType) {
    this.feedType = feedType;
    this.idDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    idDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  /**
   * Prepares the view for presentation.
   */
  @SuppressWarnings("unchecked")
  public void prepare() {
    ContentDecoratorContext context = new ContentDecoratorContext();
    context.setView(ContentDecoratorContext.SUMMARY_VIEW);
    context.setMedia(ContentDecoratorContext.NEWS_FEED);

    List<BlogEntry> blogEntries = (List<BlogEntry>) getModel().get(Constants.BLOG_ENTRIES);
    ContentDecoratorChain.decorate(context, blogEntries);
    getModel().put(Constants.BLOG_ENTRIES, blogEntries);
  }

  /**
   * Gets the content type of this view.
   *
   * @return the content type as a String
   */
  public String getContentType() {
    AbstractBlog blog = (AbstractBlog) getModel().get(Constants.BLOG_KEY);
    return feedType.getContentType() + "; charset=" + blog.getCharacterEncoding();
  }

  /**
   * Dispatch the view using ROME
   *
   * @param request  the request
   * @param response the resopnse
   * @param context  the the servlet context
   * @throws ServletException
   */
  public void dispatch(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws ServletException {
    SyndFeed syndFeed = getFeed();
    syndFeed.setFeedType(getFeedType().getFeedType());

    SyndFeedOutput output = new SyndFeedOutput();

    try {
      output.output(syndFeed, response.getWriter());
    } catch (IOException e) {
      throw new ServletException("Error generating feed", e);
    } catch (FeedException e) {
      throw new ServletException("Error generating feed", e);
    }
  }

  /**
   * Get the feed to return.
   *
   * @return The feed to return.
   */
  protected abstract SyndFeed getFeed();

  /**
   * Get the feed type for this view
   *
   * @return The feed type
   */
  public FeedType getFeedType() {
    return feedType;
  }

  protected SimpleDateFormat getIdDateFormat() {
    return idDateFormat;
  }

  protected String generateId(AbstractBlog blog, Date date, String contentId) {
    StringBuilder id = new StringBuilder("tag:");
    id.append(blog.getDomainName()).append(",");
    if (date != null) {
      id.append(getIdDateFormat().format(date));
    } else {
      id.append("0000-00-00");
    }
    id.append(":").append(blog.getId());
    if (contentId != null) {
      id.append("/").append(contentId);
    }
    return id.toString();
  }

  /**
   * The type of feed
   */
  public enum FeedType {
    ATOM("atom_1.0", "atom.xml", "application/atom+xml"),
    RSS("rss_2.0", "rss.xml", "application/xml");

    private final String feedType;
    private final String fileName;
    private final String contentType;

    FeedType(String feedType, String fileName, String contentType) {
      this.feedType = feedType;
      this.fileName = fileName;
      this.contentType = contentType;
    }

    /**
     * Get the ROME feed type, eg atom_1.0
     *
     * @return The ROME feed type
     */
    public String getFeedType() {
      return feedType;
    }

    /**
     * Get the file name that this feed uses, eg atom.xml, rss.xml
     *
     * @return The filename
     */
    public String getFileName() {
      return fileName;
    }

    /**
     * Get the content type to set when sending this feed
     *
     * @return The content type
     */
    public String getContentType() {
      return contentType;
    }
  }

}
