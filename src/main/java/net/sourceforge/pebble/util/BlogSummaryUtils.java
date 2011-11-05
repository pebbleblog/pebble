package net.sourceforge.pebble.util;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.domain.Year;

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

}
