package net.sourceforge.pebble.service;

import net.sourceforge.pebble.comparator.BlogEntryComparator;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogManager;
import net.sourceforge.pebble.domain.BlogService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiBlogServiceImpl implements MultiBlogService {
  @Inject
  private BlogManager blogManager;
  @Inject
  private BlogService blogService;

  public List<BlogEntry> getRecentPublishedBlogEntries(int max) {
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();

    for (Blog blog : blogManager.getPublicBlogs()) {
      blogEntries.addAll(blogService.getRecentPublishedBlogEntries(blog));
    }

    Collections.sort(blogEntries, new BlogEntryComparator());

    if (blogEntries.size() >= max) {
      return new ArrayList<BlogEntry>(blogEntries).subList(0, max);
    } else {
      return new ArrayList<BlogEntry>(blogEntries);
    }
  }

  public List<BlogEntry> getRecentPublishedBlogEntries() {
    return getRecentPublishedBlogEntries(blogManager.getMultiBlog().getRecentBlogEntriesOnHomePage());
  }
}
