package net.sourceforge.pebble.index.file;

import com.google.common.collect.ImmutableList;
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.domain.Year;

import java.util.LinkedList;
import java.util.List;

/**
 * A cache of blog entries for the year
 */
public class YearCache implements Comparable<YearCache> {
  private final Year year;
  private final List<MonthCache> months;
  private final int firstMonth;

  private YearCache(Year year, List<MonthCache> months, int firstMonth) {
    this.year = year;
    this.months = months;
    this.firstMonth = firstMonth;
  }

  public Year getYear() {
    return year;
  }

  public MonthCache getMonthCache(int month) {
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Invalid month of " + month + " specified, should be between 1 and 12");
    }

    // Offset month by first month to get the index in the array
    int index = month - firstMonth;

    // Check if we have this month or not
    if (index < 0 || index >= months.size()) {
      return MonthCache.builder(year.getBlogForMonth(month)).build();
    }

    return months.get(index);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    YearCache yearCache = (YearCache) o;

    if (year != null ? !year.equals(yearCache.year) : yearCache.year != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return year != null ? year.hashCode() : 0;
  }

  public int compareTo(YearCache o) {
    return year.compareTo(o.year);
  }

  public static Builder builder(Year year) {
    return new Builder(year);
  }

  public static Builder builder(YearCache like) {
    return new Builder(like);
  }

  public static class Builder {
    private final Year year;
    private final LinkedList<MonthCache> months;
    private int firstMonth;

    private Builder(Year year) {
      this.year = year;
      months = new LinkedList<MonthCache>();
      firstMonth = 13;
    }

    private Builder(YearCache yearCache) {
      this.year = yearCache.year;
      this.months = new LinkedList<MonthCache>(yearCache.months);
      this.firstMonth = yearCache.firstMonth;
    }

    public YearCache build() {
      return new YearCache(year, ImmutableList.copyOf(months), firstMonth);
    }

    public Builder putMonth(MonthCache month) {
      if (month.getMonth().getYear() != year.getYear()) {
        throw new IllegalArgumentException("Cannot add month from year " + month.getMonth().getYear() + " to year " + year);
      }
      // First insert needed months
      while (firstMonth > month.getMonth().getMonth()) {
        firstMonth--;
        months.addFirst(MonthCache.builder(Month.emptyMonth(month.getMonth().getBlog(), year.getYear(), firstMonth)).build());
      }
      months.set(month.getMonth().getMonth() - firstMonth, month);
      return this;
    }

  }

}
