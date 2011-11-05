package net.sourceforge.pebble.util;

import com.google.common.collect.Lists;
import net.sourceforge.pebble.domain.*;

/**
 * Utilities for working with the blog summaries (ie, months, years days etc)
 */
public class BlogSummaryUtils {
  public static Day getPreviousDay(Iterable<Year> years, Day day) {
    Month month = getMonthForDay(years, day);
    if (day.getDay() <= 1) {
      return getPreviousMonth(years, month).getBlogForLastDay();
    } else {
      return month.getBlogForDay(day.getDay() - 1);
    }
  }

  public static Day getNextDay(Iterable<Year> years, Day day) {
    Month month = getMonthForDay(years, day);
    if (day.getDay() >= month.getLastDayInMonth()) {
      return getNextMonth(years, month).getBlogForFirstDay();
    } else {
      return month.getBlogForDay(day.getDay() + 1);
    }
  }

  public static Month getMonthForDay(Iterable<Year> years, Day day) {
    return getYear(day.getBlog(), years, day.getYear()).getBlogForMonth(day.getMonth());
  }

  public static Month getPreviousMonth(Iterable<Year> years, Month month) {
    if (month.getMonth() <= 1) {
      return getYear(month.getBlog(), years, month.getYear() - 1).getBlogForMonth(12);
    } else {
      return getYear(month.getBlog(), years, month.getYear()).getBlogForMonth(month.getMonth() - 1);
    }
  }

  public static Month getNextMonth(Iterable<Year> years, Month month) {
    if (month.getMonth() >= 12) {
      return getYear(month.getBlog(), years, month.getYear() + 1).getBlogForFirstMonth();
    } else {
      return getYear(month.getBlog(), years, month.getYear()).getBlogForMonth(month.getMonth() + 1);
    }
  }

  public static Year getYear(Blog blog, Iterable<Year> years, int yearNo) {
    for (Year year : years) {
      if (year.getYear() == yearNo) {
        return year;
      }
    }
    return Year.emptyYear(blog, yearNo);
  }

  /**
   * Gets the Month instance representing the first month that
   * contains blog entries.
   *
   * @param blog A blog
   * @param years The available years
   * @return  a Month instance
   */
  public static Month getFirstMonth(Blog blog, Iterable<Year> years) {
    for (Year year : years) {
      for (Month month : Lists.reverse(year.getArchives())) {
        if (month.hasBlogEntries()) {
          return month;
        }
      }
    }
    SimpleDate now = blog.getToday();
    return Month.emptyMonth(blog, now.getYear(), now.getMonth());
  }

  public static Day getBlogForDay(Blog blog, Iterable<Year> years, SimpleDate date) {
    return getYear(blog, years, date.getYear()).getBlogForMonth(date.getMonth()).getBlogForDay(date.getDay());
  }
}
