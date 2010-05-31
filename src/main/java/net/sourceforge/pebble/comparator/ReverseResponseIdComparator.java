package net.sourceforge.pebble.comparator;

import java.util.Comparator;

/**
 * A comparator used to order response ids, in reverse order.
 *
 * @author    Simon Brown
 */
public class ReverseResponseIdComparator implements Comparator {

  /**
   * Compares two objects.
   *
   * @param o1  object 1
   * @param o2  object 2
   * @return  -n, 0 or +n if the date represented by the second blog entry is less than,
   *          the same as or greater than the first, respectively
   */
  public int compare(Object o1, Object o2) {
    String responseId1 = (String)o1;
    String responseId2 = (String)o2;

    int start = responseId1.lastIndexOf("/");
    return responseId2.substring(start).compareTo(responseId1.substring(start));
  }

}
