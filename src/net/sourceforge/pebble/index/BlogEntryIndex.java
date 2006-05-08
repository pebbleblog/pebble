package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.DailyBlog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FilenameFilter;
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

  public BlogEntryIndex(Blog blog) {
    this.blog = blog;

    loadIndex();
  }

  public void clear() {
  }

  public void index(List<BlogEntry> blogEntries) {
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    for (BlogEntry blogEntry : blogEntries) {
      log.info("Indexing " + format.format(blogEntry.getDate()) + "/" + blogEntry.getId());

      DailyBlog day = blog.getBlogForDay(blogEntry.getDate());
      day.addBlogEntry(blogEntry.getId());
    }
  }

  public List<String> getBlogEntries(int year, int month, int day) {
    DecimalFormat doubleZero = new DecimalFormat("00");

    StringBuffer buf = new StringBuffer();
    buf.append(blog.getRoot());
    buf.append(File.separator);
    buf.append(year);
    buf.append(File.separator);
    buf.append(doubleZero.format(month));
    buf.append(File.separator);
    buf.append(doubleZero.format(day));

    File dayDirectory = new File(buf.toString());
    String blogEntryFiles[] = dayDirectory.list(new BlogEntryFilenameFilter());

    List<String> blogEntryIds = new ArrayList<String>();
    if (blogEntryFiles != null) {
      for (String blogEntryFile : blogEntryFiles) {
        blogEntryIds.add(blogEntryFile.substring(0, blogEntryFile.length()-4));
      }
    }

    return blogEntryIds;
  }

  public List<String> getBlogEntries(int year, int month) {
    List<String> blogEntryIds = new ArrayList<String>();

    // this is a hack, but hey :-)
    for (int day = 1; day <= 31; day++) {
      blogEntryIds.addAll(getBlogEntries(year, month, day));
    }

    return blogEntryIds;
  }

  private void loadIndex() {
    File index = new File(blog.getIndexesDirectory(), "blogentries.index");
    if (index.exists()) {

    }
  }

  /**
   * Filters out any files that aren't blog entries.
   *
   * @author Simon Brown
   */
  class BlogEntryFilenameFilter implements FilenameFilter {

    /**
     * Tests if a specified file should be included in a file list.
     *
     * @param dir  the directory in which the file was found.
     * @param name the name of the file.
     * @return <code>true</code> if and only if the name should be
     *         included in the file list; <code>false</code> otherwise.
     */
    public boolean accept(File dir, String name) {
      return name.matches("\\d+.xml\\z");
    }
  }

}