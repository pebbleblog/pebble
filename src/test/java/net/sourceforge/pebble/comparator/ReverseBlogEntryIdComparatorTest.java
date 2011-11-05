package net.sourceforge.pebble.comparator;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.*;
import static net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator.INSTANCE;
/**
 */
public class ReverseBlogEntryIdComparatorTest {
  @Test
  public void firstIdLongerShouldBeSmaller() {
    assertThat(INSTANCE.compare("1111", "999"), lessThan(0));
    assertThat(INSTANCE.compare("9999", "111"), lessThan(0));
  }
  @Test
  public void secondIdLongerShouldBeSmaller() {
    assertThat(INSTANCE.compare("111", "9999"), greaterThan(0));
    assertThat(INSTANCE.compare("999", "1111"), greaterThan(0));
  }
  @Test
  public void idsWithSameLengthShouldBeBasedOnNumber() {
    assertThat(INSTANCE.compare("1111", "9999"), greaterThan(0));
    assertThat(INSTANCE.compare("9999", "1111"), lessThan(0));
  }
  @Test
  public void equalIdsShouldBeEqual() {
    assertThat(INSTANCE.compare("9999", "9999"), equalTo(0));
  }

}
