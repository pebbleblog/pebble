package net.sourceforge.pebble.comparator;

import java.util.Comparator;

/**
 * A comparator used to order blog entry id instances, in reverse order..
 *
 * @author    Simon Brown
 */
public class ReverseBlogEntryIdComparator implements Comparator {

  /**
   * Compares two objects.
   *
   * @param o1  object 1
   * @param o2  object 2
   * @return  -n, 0 or +n if the date represented by the second blog entry is less than,
   *          the same as or greater than the first, respectively
   */
  public int compare(Object o1, Object o2) {
    String blogEntryId1 = (String)o1;
    String blogEntryId2 = (String)o2;
    Long l1 = Long.parseLong(blogEntryId1);
    Long l2 = Long.parseLong(blogEntryId2);

    return l2.compareTo(l1);
  }

}
