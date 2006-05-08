package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.DailyBlog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: simon
 * Date: 08-May-2006
 * Time: 17:37:49
 * To change this template use File | Settings | File Templates.
 */
public class BlogEntryIndex {

  private static final Log log = LogFactory.getLog(BlogEntryIndex.class);

  private Blog blog;

  private List<String> indexEntries = new ArrayList<String>();

  public BlogEntryIndex(Blog blog) {
    this.blog = blog;

    loadIndex();
  }

  public void clear() {
    indexEntries = new ArrayList<String>();
    writeIndex();
  }

  public synchronized void index(List<BlogEntry> blogEntries) {
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    for (BlogEntry blogEntry : blogEntries) {
      String indexEntry = format.format(blogEntry.getDate()) + "/" + blogEntry.getId();
      indexEntries.add(indexEntry);
    }

    writeIndex();
  }

  public synchronized void index(BlogEntry blogEntry) {
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    String indexEntry = format.format(blogEntry.getDate()) + "/" + blogEntry.getId();
    indexEntries.add(indexEntry);

    DailyBlog dailyBlog = blog.getBlogForDay(blogEntry.getDate());
    dailyBlog.addBlogEntry(blogEntry.getId());

    writeIndex();
  }

  public synchronized void unindex(BlogEntry blogEntry) {
    DailyBlog dailyBlog = blog.getBlogForDay(blogEntry.getDate());
    dailyBlog.removeBlogEntry(blogEntry.getId());

    DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    String indexEntry = format.format(blogEntry.getDate()) + "/" + blogEntry.getId();
    indexEntries.remove(indexEntry);

    writeIndex();
  }

  private void loadIndex() {
    File index = new File(blog.getIndexesDirectory(), "blogentries.index");
    if (index.exists()) {
      try {
        File indexFile = new File(blog.getIndexesDirectory(), "blogentries.index");
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String indexEntry = reader.readLine();
        while (indexEntry != null) {
          indexEntries.add(indexEntry);

          // split up the yyyy/MM/dd/blogentryid into it's parts
          String parts[] = indexEntry.split("/");
          int year = Integer.parseInt(parts[0]);
          int month = Integer.parseInt(parts[1]);
          int day = Integer.parseInt(parts[2]);
          String blogEntryId = parts[3];

          // and add it to the internal memory structures
          DailyBlog dailyBlog = blog.getBlogForDay(year, month, day);
          dailyBlog.addBlogEntry(blogEntryId);

          indexEntry = reader.readLine();
        }

        reader.close();
      } catch (Exception e) {
        log.error("Error while reading index", e);
      }
    }
  }

  private void writeIndex() {
    try {
      File indexFile = new File(blog.getIndexesDirectory(), "blogentries.index");
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (String indexEntry : indexEntries) {
        writer.write(indexEntry);
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error("Error while writing index", e);
    }
  }

}