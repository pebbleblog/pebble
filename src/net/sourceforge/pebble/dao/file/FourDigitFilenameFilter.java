package net.sourceforge.pebble.dao.file;

import java.io.FilenameFilter;
import java.io.File;

/**
 * Filters out files (directories) that aren't named yyyy.
 *
 * @author Simon Brown
 */
public class FourDigitFilenameFilter implements FilenameFilter {

  /**
   * Tests if a specified file should be included in a file list.
   *
   * @param dir  the directory in which the file was found.
   * @param name the name of the file.
   * @return <code>true</code> if and only if the name should be
   *         included in the file list; <code>false</code> otherwise.
   */
  public boolean accept(File dir, String name) {
    return name.matches("\\d\\d\\d\\d\\z");
  }

}
