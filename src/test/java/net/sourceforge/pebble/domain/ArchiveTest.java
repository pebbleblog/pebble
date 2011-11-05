package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.domain.Archive;
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.Year;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArchiveTest extends SingleBlogTestCase {
  private Archive archive;

  protected void setUp() throws Exception {
    super.setUp();
    archive = Archive.builder(blog).build();
  }
  /**
   * Tests that we can access the previous month.
   */
  public void testGetPreviousMonth() {
    Month month = Month.emptyMonth(blog, 2003, 7);
    Month previousMonth = archive.getPreviousMonth(month);
    assertEquals(2003, previousMonth.getYear());
    assertEquals(6, previousMonth.getMonth());

    month = Month.emptyMonth(blog, 2003, 1);
    previousMonth = archive.getPreviousMonth(month);
    assertEquals(2002, previousMonth.getYear());
    assertEquals(12, previousMonth.getMonth());
  }

  /**
   * Tests that we can access the next month.
   */
  public void testGetNextMonth() {
    Month month = Month.emptyMonth(blog, 2003, 7);
    Month nextMonth = archive.getNextMonth(month);
    assertEquals(2003, nextMonth.getYear());
    assertEquals(8, nextMonth.getMonth());

    month = Month.emptyMonth(blog, 2002, 12);
    nextMonth = archive.getNextMonth(month);
    assertEquals(2003, nextMonth.getYear());
    assertEquals(1, nextMonth.getMonth());
  }


}
