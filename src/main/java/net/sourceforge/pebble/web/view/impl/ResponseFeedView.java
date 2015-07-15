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
import com.rometools.modules.content.ContentModule;
import com.rometools.modules.content.ContentModuleImpl;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.FeedDecorator;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.security.PebbleUserDetails;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * The view for the feed responses
 *
 * @author James Roper
 */
public class ResponseFeedView extends AbstractRomeFeedView {

  public ResponseFeedView(FeedType feedType) {
    super(feedType);
  }

  @SuppressWarnings("unchecked")
  protected SyndFeed getFeed() {
    Blog blog = (Blog) getModel().get(Constants.BLOG_KEY);
    BlogEntry blogEntry = (BlogEntry) getModel().get(Constants.BLOG_ENTRY_KEY);
    List<Response> responses = (List<Response>) getModel().get(Constants.RESPONSES);

    Collection<FeedDecorator> feedDecorators = blog.getFeedDecorators();

    SyndFeed syndFeed = new SyndFeedImpl();
    syndFeed.setUri(generateId(blog, null, "responses"));

    if (blogEntry == null) {
      populateFeedInfo(blog, syndFeed);
    } else {
      populateFeedInfo(blogEntry, syndFeed);
    }

    if (StringUtils.isNotBlank(blog.getImage())) {
      SyndImage syndImage = new SyndImageImpl();
      syndImage.setUrl(blog.getImage());
      syndImage.setTitle(blog.getName());
      syndImage.setLink(blog.getUrl());
      syndFeed.setImage(syndImage);
      // Unfortunately, ROME doesn't support the logo attribute for Atom in a feed agnostic way
    }
    syndFeed.setLanguage(blog.getLanguage());

    List<SyndEntry> feedEntries = new ArrayList<SyndEntry>();

    for (Response response : responses) {
      SyndEntry feedEntry = convertResponse(response);
      for (FeedDecorator feedDecorator : feedDecorators) {
        feedDecorator.decorate(feedEntry, blog, response);
      }
      feedEntries.add(feedEntry);
    }
    syndFeed.setEntries(feedEntries);
    for (FeedDecorator feedDecorator : feedDecorators) {
      feedDecorator.decorate(syndFeed, blog, blogEntry);
    }
    return syndFeed;
  }

  private SyndEntry convertResponse(Response response) {

    SyndEntry feedEntry = new SyndEntryImpl();
    feedEntry.setUri(generateId(response.getBlogEntry().getBlog(), response.getBlogEntry().getDate(), response.getGuid()));
    feedEntry.setTitle(response.getTitle());
    feedEntry.setLink(response.getPermalink());

    SyndPerson entryAuthor = new SyndPersonImpl();
    entryAuthor.setName(response.getSourceName());
    entryAuthor.setUri(response.getSourceLink());
    feedEntry.setAuthors(Collections.singletonList(entryAuthor));

    feedEntry.setUpdatedDate(response.getDate());
    feedEntry.setPublishedDate(response.getDate());

    ContentModule content = new ContentModuleImpl();
    List<String> contents = new ArrayList<String>();
    contents.add(response.getContent());
    content.setEncodeds(contents);
    feedEntry.getModules().add(content);

    return feedEntry;
  }

  private void populateFeedInfo(BlogEntry blogEntry, SyndFeed syndFeed) {
    syndFeed.setTitle(blogEntry.getBlog().getName() + " - " + blogEntry.getTitle());

    syndFeed.setLink(blogEntry.getPermalink());
    SyndLink alternate = new SyndLinkImpl();
    alternate.setHref(blogEntry.getPermalink());
    alternate.setRel("alternate");
    alternate.setType("text/html");
    SyndLink self = new SyndLinkImpl();
    self.setHref(blogEntry.getBlog().getUrl() + "responses/atom.xml?entry=" + blogEntry.getId());
    self.setRel("self");
    self.setType(getFeedType().getContentType());
    syndFeed.setLinks(Arrays.asList(alternate, self));

    syndFeed.setPublishedDate(blogEntry.getLastModified());

    PebbleUserDetails userDetails = blogEntry.getUser();
    SyndPerson author = new SyndPersonImpl();
    author.setName(userDetails.getName());
    author.setUri(userDetails.getWebsite());
    syndFeed.setAuthors(Collections.singletonList(author));

    syndFeed.setDescription(blogEntry.getTruncatedContent());
    syndFeed.setCopyright(userDetails.getName());
  }

  private void populateFeedInfo(Blog blog, SyndFeed syndFeed) {
    syndFeed.setTitle(blog.getName() + " - Responses");

    syndFeed.setLink(blog.getUrl());
    SyndLink alternate = new SyndLinkImpl();
    alternate.setHref(blog.getUrl());
    alternate.setRel("alternate");
    alternate.setType("text/html");
    SyndLink self = new SyndLinkImpl();
    self.setHref(blog.getUrl() + "responses/atom.xml");
    self.setRel("self");
    self.setType(getFeedType().getContentType());
    syndFeed.setLinks(Arrays.asList(alternate, self));

    syndFeed.setPublishedDate(blog.getDateOfLastResponse());

    syndFeed.setAuthor(blog.getAuthor());
    syndFeed.setCopyright(blog.getAuthor());
    syndFeed.setDescription(blog.getDescription());
  }

}