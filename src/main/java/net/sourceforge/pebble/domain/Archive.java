package net.sourceforge.pebble.domain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * The blog archive
 */
public class Archive {
  private final Blog blog;
  private final List<Year> years;

  private Archive(Blog blog, List<Year> years) {
    this.blog = blog;
    this.years = years;
  }

  /**
   * Get the blog this archive is for
   *
   * @return The blog the archive is for
   */
  public Blog getBlog() {
    return blog;
  }

  /**
   * Get the years for this archive
   *
   * @return The years for this archive
   */
  public List<Year> getYears() {
    return years;
  }

  /**
   * Gets the Month instance representing the first month that
   * contains blog entries.
   *
   * @return  a Month instance
   */
  public Month getFirstMonth() {
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

  /**
   * Get the month previous to the given month
   *
   * @param month The given month
   * @return The month before that
   */
  public Month getPreviousMonth(Month month) {
    if (month.getMonth() <= 1) {
      return getYear(month.getYear() - 1).getBlogForMonth(12);
    } else {
      return getYear(month.getYear()).getBlogForMonth(month.getMonth() - 1);
    }
  }

  /**
   * Get the next month from the given month
   *
   * @param month The given month
   * @return The month after that
   */
  public Month getNextMonth(Month month) {
    if (month.getMonth() >= 12) {
      return getYear(month.getYear() + 1).getBlogForFirstMonth();
    } else {
      return getYear(month.getYear()).getBlogForMonth(month.getMonth() + 1);
    }
  }

  /**
   * Get the day previous to the given day
   *
   * @param day The given day
   * @return The day before that
   */
  public Day getPreviousDay(Day day) {
    Month month = getYear(day.getYear()).getBlogForMonth(day.getMonth());
    if (day.getDay() <= 1) {
      return getPreviousMonth(month).getBlogForLastDay();
    } else {
      return month.getBlogForDay(day.getDay() - 1);
    }
  }

  /**
   * Get the day after the given day
   *
   * @param day The given day
   * @return The day after that
   */
  public Day getNextDay(Day day) {
    Month month = getYear(day.getYear()).getBlogForMonth(day.getMonth());
    if (day.getDay() >= month.getLastDayInMonth()) {
      return getNextMonth(month).getBlogForFirstDay();
    } else {
      return month.getBlogForDay(day.getDay() + 1);
    }
  }

  /**
   * Get the day for the given date
   *
   * @param date The date
   * @return The day for that date
   */
  public Day getDay(SimpleDate date) {
    return getYear(date.getYear()).getBlogForMonth(date.getMonth()).getBlogForDay(date.getDay());
  }

  /**
   * Get the day for the given date
   *
   * @param year The year
   * @param month The month
   * @param day The day
   * @return The day for that date
   */
  public Day getDay(int year, int month, int day) {
    return getYear(year).getBlogForMonth(month).getBlogForDay(day);
  }

  /**
   * Get the day for today
   *
   * @return The day for today
   */
  public Day getToday() {
    return getDay(blog.getToday());
  }

  /**
   * Get the this month
   *
   * @return This month
   */
  public Month getThisMonth() {
    SimpleDate now = blog.getToday();
    return getYear(now.getYear()).getBlogForMonth(now.getMonth());
  }

  /**
   * Get the this year
   *
   * @return This year
   */
  public Year getThisYear() {
    SimpleDate now = blog.getToday();
    return getYear(now.getYear());
  }

  /**
   * Get the given year
   *
   * @param yearNo The year to get
   * @return The given year
   */
  public Year getYear(int yearNo) {
    for (Year year : years) {
      if (year.getYear() == yearNo) {
        return year;
      }
    }
    return Year.emptyYear(blog, yearNo);
  }


  public static Builder builder(Blog blog) {
    return new Builder(blog);
  }

  public static Builder builder(Archive like) {
    return new Builder(like);
  }

  public static class Builder {
    private final Blog blog;
    private List<Year> years = Collections.emptyList();

    private Builder(Blog blog) {
      this.blog = blog;
    }

    private Builder(Archive like) {
      this.blog = like.blog;
      this.years = like.years;
    }

    public Archive build() {
      return new Archive(blog, ImmutableList.copyOf(years));
    }

    public Builder setYears(List<Year> years) {
      this.years = years;
      return this;
    }
  }
}
