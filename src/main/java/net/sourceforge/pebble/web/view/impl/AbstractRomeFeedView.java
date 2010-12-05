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
package net.sourceforge.pebble.web.view.impl;

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.decorator.ContentDecoratorChain;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.security.PebbleUserDetails;
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

  /**
   * Prepares the view for presentation.
   */
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
    Blog blog = (Blog) getModel().get(Constants.BLOG_KEY);
    return "application/xml; charset=" + blog.getCharacterEncoding();
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
    AbstractBlog blog = (AbstractBlog) getModel().get(Constants.BLOG_KEY);
    List<BlogEntry> blogEntries = (List<BlogEntry>) getModel().get(Constants.BLOG_ENTRIES);
    SyndFeed syndFeed = new SyndFeedImpl();

    String blogUrl = blog.getUrl();

    Tag tag = (Tag) getModel().get("tag");
    Category category = (Category) getModel().get("category");
    String author = (String) getModel().get("author");

    String permalink;
    String feedAuthor = blog.getAuthor();
    String title = blog.getName();

    if (tag != null) {
      permalink = tag.getPermalink();
      title += tag.getName();
    } else if (category != null) {
      permalink = category.getPermalink();
      title += category.getName();
    } else if (author != null) {
      permalink = blogUrl + "authors/" + author;
      title += author;
    } else {
      permalink = blogUrl;
      syndFeed.setTitle(blog.getName());
    }
    String xmlLink;
    if (permalink.endsWith("/")) {
      xmlLink = permalink + getFileName();
    } else {
      xmlLink = permalink + "/" + getFileName();
    }
    syndFeed.setUri(xmlLink);
    syndFeed.setTitle(title);
    syndFeed.setDescription(blog.getDescription());
    syndFeed.setLink(permalink);
    // Alternate link, used in favour of the above link for atom
    SyndLink alternate = new SyndLinkImpl();
    alternate.setHref(permalink);
    alternate.setRel("alternate");
    alternate.setType("text/html");

    SyndLink self = new SyndLinkImpl();
    self.setHref(xmlLink);
    self.setRel("self");
    self.setType(getContentType());
    syndFeed.setLinks(Arrays.asList(alternate, self));

    syndFeed.setPublishedDate(blog.getLastModified());
    syndFeed.setAuthor(feedAuthor);

    if (blog.getImage() != null) {
      SyndImage syndImage = new SyndImageImpl();
      syndImage.setUrl(blog.getImage());
      syndImage.setTitle(title);
      syndImage.setUrl(permalink);
      syndFeed.setImage(syndImage);
      // Unfortunately, ROME doesn't support the logo attribute for Atom in a feed agnostic way
    }

    syndFeed.setCopyright(feedAuthor);
    syndFeed.setLanguage(blog.getLanguage());

    List<SyndEntry> feedEntries = new ArrayList<SyndEntry>();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(TimeZone.getTimeZone("UTC"));
    for (BlogEntry entry : blogEntries) {
      SyndEntry feedEntry = new SyndEntryImpl();
      feedEntry.setUri("tag:" + blog.getDomainName() + ":" + format.format(entry.getDate()) + ":" + blog.getId() +
          "/" + entry.getId());
      feedEntry.setTitle(entry.getTitle());
      feedEntry.setLink(entry.getPermalink());
      PebbleUserDetails entryUser = entry.getUser();
      if (entryUser == null) {
        feedEntry.setAuthor(entry.getAuthor());
      } else {
        SyndPerson entryAuthor = new SyndPersonImpl();
        entryAuthor.setName(entryUser.getName());
        entryAuthor.setUri(entryUser.getWebsite());
        feedEntry.setAuthors(Collections.singletonList(entryAuthor));
      }
      feedEntry.setUpdatedDate(entry.getLastModified());
      feedEntry.setPublishedDate(entry.getDate());

      List<SyndCategory> feedCategories = new ArrayList<SyndCategory>();
      for (Category cat : entry.getCategories()) {
        SyndCategory feedCategory = new SyndCategoryImpl();
        feedCategory.setName(cat.getName());
        feedCategory.setTaxonomyUri(cat.getPermalink());
        feedCategories.add(feedCategory);
      }
      for (Tag entryTag : entry.getAllTags()) {
        SyndCategory feedCategory = new SyndCategoryImpl();
        feedCategory.setName(entryTag.getName());
        feedCategory.setTaxonomyUri(entryTag.getPermalink());
        feedCategories.add(feedCategory);
      }
      feedEntry.setCategories(feedCategories);

      SyndContent content = new SyndContentImpl();
      content.setType("text/html");
      if (entry.getExcerpt() == null || entry.getExcerpt().length() == 0) {
        content.setValue(entry.getBody());
      } else {
        content.setValue(entry.getExcerpt());
      }
      feedEntry.setContents(Collections.singletonList(content));

      if (entry.getAttachment() != null) {
        SyndEnclosure enclosure = new SyndEnclosureImpl();
        enclosure.setUrl(entry.getAttachment().getUrl());
        enclosure.setType(entry.getAttachment().getType());
        enclosure.setLength(entry.getAttachment().getSize());
        feedEntry.setEnclosures(Collections.singletonList(enclosure));
      }
      feedEntries.add(feedEntry);
    }
    syndFeed.setEntries(feedEntries);

    syndFeed.setFeedType(getFeedType());
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
   * Get the file name for the feed URL, eg atom.xml, rss.xml
   *
   * @return The file name for the feed URL
   */
  protected abstract String getFileName();

  /**
   * Get the feed type, eg atom_1.0, rss_2.0
   *
   * @return The feed type
   */
  protected abstract String getFeedType();
}
