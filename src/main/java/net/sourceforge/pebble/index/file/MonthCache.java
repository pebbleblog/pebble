package net.sourceforge.pebble.index.file;

import com.google.common.collect.ImmutableList;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;

import java.util.*;

/**
 * A cache of blog entries for the month
 */
public class MonthCache {

  private final Month month;
  private final List<DayCache> days;
  private final int firstDay;

  public MonthCache(Month month, List<DayCache> days, int firstDay) {
    this.month = month;
    this.days = days;
    this.firstDay = firstDay;
  }

  public Month getMonth() {
    return month;
  }

  public DayCache getDayCache(int day) {
    // some bounds checking
    if (day < 1 || day > month.getLastDayInMonth()) {
      throw new IllegalArgumentException("Invalid day of " + day + " specified, should be between 1 and " + month.getLastDayInMonth());
    }

    // Calculate index
    int index = day - firstDay;
    if (index < 0 || index >= days.size()) {
      return DayCache.builder(month.getBlogForDay(day)).build();
    }

    return days.get(index);
  }

  /**
   * Gets all blog entries for this month.
   *
   * @return  a List of BlogEntry instances, reverse ordered by date
   */
  public List<String> getBlogEntries() {
    List<String> blogEntries = new ArrayList<String>();
    for (DayCache day : days) {
      if (day != null) {
        blogEntries.addAll(day.getBlogEntries());
      }
    }
    return blogEntries;
  }

  public static Builder builder(Month month) {
    return new Builder(month);
  }

  public static Builder builder(MonthCache like) {
    return new Builder(like);
  }

  public static class Builder {
    private final Month month;
    private final LinkedList<DayCache> days;
    private int firstDay;

    private Builder(Month month) {
      this.month = month;
      this.days = new LinkedList<DayCache>();
      this.firstDay = month.getLastDayInMonth() + 1;
    }

    private Builder(MonthCache monthCache) {
      this.month = monthCache.month;
      this.days = new LinkedList<DayCache>(monthCache.days);
      this.firstDay = monthCache.firstDay;
    }

    public MonthCache build() {
      return new MonthCache(month, ImmutableList.copyOf(days), firstDay);
    }

    public Builder putDay(DayCache day) {
      if (day.getDay().getMonth() != month.getMonth() || day.getDay().getYear() != month.getYear()) {
        throw new IllegalArgumentException("Cannot add day from year " + day.getDay().getYear()  + " and month " + day.getDay().getMonth() + " to month " + month.getMonth() + "in year " + month.getYear());
      }
      if (day.getDay().getDay() < 1 || day.getDay().getDay() > month.getLastDayInMonth()) {
        throw new IllegalArgumentException("Day must be between 1 and " + month.getLastDayInMonth());
      }
      // First insert needed days
      while (firstDay > day.getDay().getDay()) {
        firstDay--;
        days.addFirst(DayCache.builder(Day.emptyDay(month.getBlog(), month.getYear(), month.getMonth(), firstDay)).build());
      }
      days.set(day.getDay().getDay() - firstDay, day);
      return this;
    }

  }


}
