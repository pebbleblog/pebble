package net.sourceforge.pebble.dao.file;

import java.io.FilenameFilter;
import java.io.File;

/**
 * Filters out any files that aren't blog entries.
 *
 * @author Simon Brown
 */
public class BlogEntryFilenameFilter implements FilenameFilter {

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
