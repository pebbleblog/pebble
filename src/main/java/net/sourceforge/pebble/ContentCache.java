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
package net.sourceforge.pebble;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import net.sourceforge.pebble.util.RelativeDate;
import net.sourceforge.pebble.domain.*;
import net.sf.ehcache.Element;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * A wrapper for a cache used to store blog entries and static pages.
 *
 * @author    Simon Brown
 */
public class ContentCache {

  private static final ContentCache instance = new ContentCache();

  /** the log used by this class */
  private static Log log = LogFactory.getLog(ContentCache.class);

  private Cache cache;

  private ContentCache() {
    URL url = BlogService.class.getResource("/ehcache.xml");
    CacheManager cacheManager = new CacheManager(url);
    cache = cacheManager.getCache("contentCache");

    // size the cache (number of blogs * max elements in memory configured in the ehcache.xml file)
    // Fix: Previously the number of blogs was calculated through blogManager.getBlogs().getSize() which
    // caused the blog to load and access the Cache that is just now being initialized.
    // This lead to NPE because instance is not yet set to this instance.
    cache.getCacheConfiguration().setMaxElementsInMemory(cache.getCacheConfiguration().getMaxElementsInMemory() * BlogManager.getInstance().getNumberOfBlogs());
  }

  public static ContentCache getInstance() {
    return instance;
  }

  public synchronized void putBlogEntry(BlogEntry blogEntry) {
    Element element = new Element(getCompositeKeyForBlogEntry(blogEntry), blogEntry);
    cache.put(element);
  }

  public synchronized BlogEntry getBlogEntry(Blog blog, String blogEntryId) {
    BlogEntry blogEntry = null;
    Element element = cache.get(getCompositeKeyForBlogEntry(blog, blogEntryId));
    if (element != null) {
      blogEntry = (BlogEntry)element.getValue(); 
    }

    return blogEntry;
  }

  public synchronized void removeBlogEntry(BlogEntry blogEntry) {
    cache.remove(getCompositeKeyForBlogEntry(blogEntry));
  }

  private String getCompositeKeyForBlogEntry(BlogEntry blogEntry) {
    return getCompositeKeyForBlogEntry(blogEntry.getBlog(), blogEntry.getId());
  }

  private String getCompositeKeyForBlogEntry(Blog blog, String blogEntryId) {
    return blog.getId() + "/blogEntry/" + blogEntryId;
  }

  public synchronized void putStaticPage(StaticPage staticPage) {
    Element element = new Element(getCompositeKeyForStaticPage(staticPage), staticPage);
    cache.put(element);
  }

  public synchronized StaticPage getStaticPage(Blog blog, String staticPageId) {
    StaticPage staticPage = null;
    Element element = cache.get(getCompositeKeyForStaticPage(blog, staticPageId));
    if (element != null) {
      staticPage = (StaticPage)element.getValue();
    }

    return staticPage;
  }

  public synchronized void removeStaticPage(StaticPage staticPage) {
    cache.remove(getCompositeKeyForStaticPage(staticPage));
  }

  private String getCompositeKeyForStaticPage(StaticPage staticPage) {
    return getCompositeKeyForStaticPage(staticPage.getBlog(), staticPage.getId());
  }

  private String getCompositeKeyForStaticPage(Blog blog, String staticPageId) {
    return blog.getId() + "/staticPage/" + staticPageId;
  }

}