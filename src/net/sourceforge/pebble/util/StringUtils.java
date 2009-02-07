/*
 * Copyright (c) 2003-2006, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sourceforge.pebble.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A collection of utility methods for manipulating strings.
 *
 * @author    Simon Brown
 */
public final class StringUtils {

  private static final Pattern OPENING_B_TAG_PATTERN = Pattern.compile("&lt;b&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_B_TAG_PATTERN = Pattern.compile("&lt;/b&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_STRONG_TAG_PATTERN = Pattern.compile("&lt;strong&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_STRONG_TAG_PATTERN = Pattern.compile("&lt;/strong&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_I_TAG_PATTERN = Pattern.compile("&lt;i&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_I_TAG_PATTERN = Pattern.compile("&lt;/i&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_EM_TAG_PATTERN = Pattern.compile("&lt;em&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_EM_TAG_PATTERN = Pattern.compile("&lt;/em&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_BLOCKQUOTE_TAG_PATTERN = Pattern.compile("&lt;blockquote&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_BLOCKQUOTE_TAG_PATTERN = Pattern.compile("&lt;/blockquote&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern BR_TAG_PATTERN = Pattern.compile("&lt;br */*&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_P_TAG_PATTERN = Pattern.compile("&lt;p&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_P_TAG_PATTERN = Pattern.compile("&lt;/p&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_PRE_TAG_PATTERN = Pattern.compile("&lt;pre&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_PRE_TAG_PATTERN = Pattern.compile("&lt;/pre&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_UL_TAG_PATTERN = Pattern.compile("&lt;ul&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_UL_TAG_PATTERN = Pattern.compile("&lt;/ul&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_OL_TAG_PATTERN = Pattern.compile("&lt;ol&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_OL_TAG_PATTERN = Pattern.compile("&lt;/ol&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_LI_TAG_PATTERN = Pattern.compile("&lt;li&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_LI_TAG_PATTERN = Pattern.compile("&lt;/li&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_A_TAG_PATTERN = Pattern.compile("&lt;/a&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_A_TAG_PATTERN = Pattern.compile("&lt;a href=.*?&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_SUP_TAG_PATTERN = Pattern.compile("&lt;sup&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_SUP_TAG_PATTERN = Pattern.compile("&lt;/sup&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern OPENING_SUB_TAG_PATTERN = Pattern.compile("&lt;sub&gt;", Pattern.CASE_INSENSITIVE);
  private static final Pattern CLOSING_SUB_TAG_PATTERN = Pattern.compile("&lt;/sub&gt;", Pattern.CASE_INSENSITIVE);

  public static final int MAX_CONTENT_LENGTH = 255;
  public static final int MAX_WORD_LENGTH = 20;

  /**
   * Filters out characters that have meaning within JSP and HTML, and
   * replaces them with "escaped" versions.
   *
   * @param s   the String to filter
   * @return  the filtered String
   */
  public static String transformHTML(String s) {

    if (s == null) {
      return null;
    }

    StringBuffer buf = new StringBuffer(s.length());

    // loop through every character and replace if necessary
    int length = s.length();
    for (int i = 0; i < length; i++) {
      switch (s.charAt(i)) {
        case '<':
          buf.append("&lt;");
          break;
        case '>':
          buf.append("&gt;");
          break;
        case '&':
          buf.append("&amp;");
          break;
        case '\"':
            buf.append("&quot;");
            break;
        default :
          buf.append(s.charAt(i));
      }
    }

    return buf.toString();
  }

  /**
   * Transforms the given String into a subset of HTML displayable on a web
   * page. The subset includes &lt;b&gt;, &lt;i&gt;, &lt;p&gt;, &lt;br&gt;,
   * &lt;pre&gt; and &lt;a href&gt; (and their corresponding end tags).
   *
   * @param s   the String to transform
   * @return    the transformed String
   */
  public static String transformToHTMLSubset(String s) {

    if (s == null) {
      return null;
    }

    s = replace(s, OPENING_B_TAG_PATTERN, "<b>");
    s = replace(s, CLOSING_B_TAG_PATTERN, "</b>");
    s = replace(s, OPENING_STRONG_TAG_PATTERN, "<strong>");
    s = replace(s, CLOSING_STRONG_TAG_PATTERN, "</strong>");
    s = replace(s, OPENING_I_TAG_PATTERN, "<i>");
    s = replace(s, CLOSING_I_TAG_PATTERN, "</i>");
    s = replace(s, OPENING_EM_TAG_PATTERN, "<em>");
    s = replace(s, CLOSING_EM_TAG_PATTERN, "</em>");
    s = replace(s, OPENING_BLOCKQUOTE_TAG_PATTERN, "<blockquote>");
    s = replace(s, CLOSING_BLOCKQUOTE_TAG_PATTERN, "</blockquote>");
    s = replace(s, BR_TAG_PATTERN, "<br />");
    s = replace(s, OPENING_P_TAG_PATTERN, "<p>");
    s = replace(s, CLOSING_P_TAG_PATTERN, "</p>");
    s = replace(s, OPENING_PRE_TAG_PATTERN, "<pre>");
    s = replace(s, CLOSING_PRE_TAG_PATTERN, "</pre>");
    s = replace(s, OPENING_UL_TAG_PATTERN, "<ul>");
    s = replace(s, CLOSING_UL_TAG_PATTERN, "</ul>");
    s = replace(s, OPENING_OL_TAG_PATTERN, "<ol>");
    s = replace(s, CLOSING_OL_TAG_PATTERN, "</ol>");
    s = replace(s, OPENING_LI_TAG_PATTERN, "<li>");
    s = replace(s, CLOSING_LI_TAG_PATTERN, "</li>");
    s = replace(s, OPENING_SUP_TAG_PATTERN, "<sup>");
    s = replace(s, CLOSING_SUP_TAG_PATTERN, "</sup>");
    s = replace(s, OPENING_SUB_TAG_PATTERN, "<sub>");
    s = replace(s, CLOSING_SUB_TAG_PATTERN, "</sub>");

    // HTTP links - remove all attributes other than href
    s = replace(s, CLOSING_A_TAG_PATTERN, "</a>");
    Matcher m = OPENING_A_TAG_PATTERN.matcher(s);
    while (m.find()) {
      int start = m.start();
      int end = m.end();
      String link = s.substring(start, end);
      String hrefAttribute = "";
      int startOfHrefIndex = link.indexOf("href=\"");
      if (startOfHrefIndex > -1) {
        int endOfHrefIndex = link.indexOf("\"", startOfHrefIndex+"href=\"".length()); // 6 = href="
        hrefAttribute = link.substring(startOfHrefIndex, endOfHrefIndex+1);
      } else {
        startOfHrefIndex = link.indexOf("href='");
        if (startOfHrefIndex > -1) {
          int endOfHrefIndex = link.indexOf("'", startOfHrefIndex+"href='".length()); // 6 = href='
          hrefAttribute = link.substring(startOfHrefIndex, endOfHrefIndex+1);
        }
      }
      link = "<a " + hrefAttribute + ">";
      s = s.substring(0, start) + link + s.substring(end, s.length());
      m = OPENING_A_TAG_PATTERN.matcher(s);
    }

    // escaped angle brackets and other allowed entities
    s = s.replaceAll("&amp;lt;", "&lt;");
    s = s.replaceAll("&amp;gt;", "&gt;");
    s = s.replaceAll("&amp;([#a-zA-Z0-9]{1,}?);", "&$1;");
    
    return s;
  }

  private static String replace(String string, Pattern pattern, String replacement) {
    Matcher m = pattern.matcher(string);
    return m.replaceAll(replacement);
  }

  /**
   * Filters out newline characters.
   *
   * @param s   the String to filter
   * @return  the filtered String
   */
  public static String filterNewlines(String s) {

    if (s == null) {
      return null;
    }

    StringBuffer buf = new StringBuffer(s.length());

    // loop through every character and replace if necessary
    int length = s.length();
    for (int i = 0; i < length; i++) {
      switch (s.charAt(i)) {
        case '\r':
          break;
        default :
          buf.append(s.charAt(i));
      }
    }

    return buf.toString();
  }

  /**
   * Filters out all HTML tags.
   *
   * @param s   the String to filter
   * @return    the filtered String
   */
  public static String filterHTML(String s) {
    if (s == null) {
      return null;
    }

    s = s.replaceAll("&lt;", "");
    s = s.replaceAll("&gt;", "");
    s = s.replaceAll("&nbsp;", "");
    s = s.replaceAll("(?s)<!--.*?-->", "");
    return s.replaceAll("(?s)<.*?>", "");
  }

  public static String truncate(String s) {
    return truncate(s, MAX_CONTENT_LENGTH);
  }

  public static String truncate(String s, int maxLength) {
    String content = StringUtils.filterHTML(s);

    // then truncate, if necessary
    if (content == null) {
      return "";
    } else {
      StringBuffer buf = new StringBuffer();

      String words[] = content.split("\\s");
      for (int i = 0; i < words.length; i++) {
        if (buf.length() + words[i].length() > maxLength) {
          // truncate here
          buf.append("...");
          return buf.toString();
        } else if (words[i].length() > MAX_WORD_LENGTH) {
          // truncate here
          buf.append(words[i].substring(0, MAX_WORD_LENGTH));
          buf.append("...");
          return buf.toString();
        } else {
          buf.append(words[i]);
          if ((i+1) < words.length) {
            buf.append(" ");
          }
        }
      }

      return buf.toString();
    }
  }

  public static String stripScriptTags(String html) {
    if (html == null) {
      return html;
    }

    html = html.replaceAll("<script.*?>.*?</script.*?>", "");
    html = html.replaceAll("<script.*?/>", "");
    return html;
  }

}