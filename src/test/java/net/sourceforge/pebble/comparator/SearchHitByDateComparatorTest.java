package net.sourceforge.pebble.comparator;

import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.search.SearchHit;

import java.util.Comparator;
import java.util.Date;

/**
 * Tests for the SearchHitByDateComparator class.
 *
 * @author    Simon Brown
 */
public class SearchHitByDateComparatorTest extends SingleBlogTestCase {

  public void testCompare() {
    Comparator comp = new SearchHitByDateComparator();

    SearchHit h1 = new SearchHit(null, "", "", "", "", new Date(321), 1.0F);
    SearchHit h2 = new SearchHit(null, "", "", "", "", new Date(123), 1.0F);

    assertTrue(comp.compare(h1, h1) == 0);
    assertTrue(comp.compare(h1, h2) != 0);
    assertTrue(comp.compare(h1, h2) < 0);
    assertTrue(comp.compare(h2, h1) > 0);
  }

}
