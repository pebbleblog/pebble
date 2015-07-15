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
 * The view for the feed
 *
 * @author James Roper
 */
public class FeedView extends AbstractRomeFeedView {

  public FeedView(FeedType feedType) {
    super(feedType);
  }

  @SuppressWarnings("unchecked")
  protected SyndFeed getFeed() {
    AbstractBlog blog = (AbstractBlog) getModel().get(Constants.BLOG_KEY);
    Collection<FeedDecorator> feedDecorators = Collections.emptyList();
    if (blog instanceof Blog) {
      feedDecorators = ((Blog) blog).getFeedDecorators();
    }
    Collection<BlogEntry> blogEntries = (Collection<BlogEntry>) getModel().get(Constants.BLOG_ENTRIES);
    SyndFeed syndFeed = new SyndFeedImpl();

    populateFeedInfo(blog, syndFeed);

    List<SyndEntry> feedEntries = new ArrayList<SyndEntry>();

    for (BlogEntry entry : blogEntries) {
      SyndEntry feedEntry = convertBlogEntry(blog, entry);
      for (FeedDecorator feedDecorator : feedDecorators) {
        feedDecorator.decorate(feedEntry, (Blog) blog, entry);
      }
      feedEntries.add(feedEntry);
    }
    syndFeed.setEntries(feedEntries);
    for (FeedDecorator feedDecorator : feedDecorators) {
      feedDecorator.decorate(syndFeed, (Blog) blog);
    }
    return syndFeed;
  }

  private SyndEntry convertBlogEntry(AbstractBlog blog, BlogEntry entry) {
    SyndEntry feedEntry = new SyndEntryImpl();
    feedEntry.setUri(generateId(blog, entry.getDate(), entry.getId()));
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

    ContentModule content = new ContentModuleImpl();
    List<String> contents = new ArrayList<String>();
    contents.add(getSyndicatedBody(entry));
    content.setEncodeds(contents);
    feedEntry.getModules().add(content);

    if (entry.getAttachment() != null) {
      SyndEnclosure enclosure = new SyndEnclosureImpl();
      enclosure.setUrl(entry.getAttachment().getUrl());
      enclosure.setType(entry.getAttachment().getType());
      enclosure.setLength(entry.getAttachment().getSize());
      feedEntry.setEnclosures(Collections.singletonList(enclosure));
    }
    return feedEntry;
  }
  
  private String getSyndicatedBody(BlogEntry entry) {
    String prefix = "";
    
    if (entry.getSubtitle() != null && entry.getSubtitle().length() > 0) {
      prefix = "<h2>" + entry.getSubtitle() + "</h2>";
    }
    
    if (entry.getExcerpt() == null || entry.getExcerpt().length() == 0) {
      return prefix + entry.getBody();
    } else {
      return prefix + entry.getExcerpt();
    }
  }

  private void populateFeedInfo(AbstractBlog blog, SyndFeed syndFeed) {
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
      xmlLink = permalink + getFeedType().getFileName();
    } else {
      xmlLink = permalink + "/" + getFeedType().getFileName();
    }
    syndFeed.setUri(generateId(blog, null, null));
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
    self.setType(getFeedType().getContentType());
    syndFeed.setLinks(Arrays.asList(alternate, self));

    syndFeed.setPublishedDate(blog.getLastModified());
    syndFeed.setAuthor(feedAuthor);

    if (StringUtils.isNotBlank(blog.getImage())) {
      SyndImage syndImage = new SyndImageImpl();
      syndImage.setUrl(blog.getImage());
      syndImage.setTitle(title);
      syndImage.setLink(permalink);
      syndFeed.setImage(syndImage);
      // Unfortunately, ROME doesn't support the logo attribute for Atom in a feed agnostic way
    }

    syndFeed.setCopyright(feedAuthor);
    syndFeed.setLanguage(blog.getLanguage());
  }
}
