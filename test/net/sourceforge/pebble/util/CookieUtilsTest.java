package net.sourceforge.pebble.util;

import junit.framework.TestCase;

import javax.servlet.http.Cookie;

/**
 * Tests for the utilities in the CookieUtils class.
 *
 * @author    Simon Brown
 */
public class CookieUtilsTest extends TestCase {

  public void testGetCookie() {
    Cookie cookies[] = new Cookie[3];
    cookies[0] = new Cookie("name1", "value1");
    cookies[1] = new Cookie("name2", "value2");
    cookies[2] = new Cookie("name3", "value3");

    assertSame(cookies[1], CookieUtils.getCookie(cookies, "name2"));
  }

  public void testGetCookieFromNullArray() {
    assertNull(CookieUtils.getCookie(null, "name"));
  }

}
