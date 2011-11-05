package net.sourceforge.pebble.domain;

import com.maxmind.geoip.timeZone;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple date
 */
public class SimpleDate {
  private final int year;
  private final int month;
  private final int day;

  public SimpleDate(Calendar cal) {
    this.year = cal.get(Calendar.YEAR);
    this.month = cal.get(Calendar.MONTH) + 1;
    this.day = cal.get(Calendar.DAY_OF_MONTH);
  }

  public SimpleDate(TimeZone timeZone, Locale locale, Date date) {
    Calendar cal = Calendar.getInstance(timeZone, locale);
    cal.setTime(date);
    this.year = cal.get(Calendar.YEAR);
    this.month = cal.get(Calendar.MONTH) + 1;
    this.day = cal.get(Calendar.DAY_OF_MONTH);
  }

  public SimpleDate(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  public int getDay() {
    return day;
  }
}
