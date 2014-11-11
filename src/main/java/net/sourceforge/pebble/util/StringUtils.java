/*
 * Copyright (c) 2003-2011, Simon Brown
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
  public static final int MAX_NUM_OF_POSTS = 5;

  
  //HTML4 248 named entities
  private final static Map<String,String> htmlEntities = new HashMap<String,String>();
  private final static Collection<String> allowedSchemes = new ArrayList<String>();

  static {
	htmlEntities.put("&nbsp;", "\u00A0");
	htmlEntities.put("&iexcl;", "\u00A1");
	htmlEntities.put("&cent;", "\u00A2");
	htmlEntities.put("&pound;", "\u00A3");
	htmlEntities.put("&curren;", "\u00A4");
	htmlEntities.put("&yen;", "\u00A5");
	htmlEntities.put("&brvbar;", "\u00A6");
	htmlEntities.put("&sect;", "\u00A7");
	htmlEntities.put("&uml;", "\u00A8");
	htmlEntities.put("&copy;", "\u00A9");
	htmlEntities.put("&ordf;", "\u00AA");
	htmlEntities.put("&laquo;", "\u00AB");
	htmlEntities.put("&not;", "\u00AC");
	htmlEntities.put("&shy;", "\u00AD");
	htmlEntities.put("&reg;", "\u00AE");
	htmlEntities.put("&macr;", "\u00AF");
	htmlEntities.put("&deg;", "\u00B0");
	htmlEntities.put("&plusmn;", "\u00B1");
	htmlEntities.put("&sup2;", "\u00B2");
	htmlEntities.put("&sup3;", "\u00B3");
	htmlEntities.put("&acute;", "\u00B4");
	htmlEntities.put("&micro;", "\u00B5");
	htmlEntities.put("&para;", "\u00B6");
	htmlEntities.put("&middot;", "\u00B7");
	htmlEntities.put("&cedil;", "\u00B8");
	htmlEntities.put("&sup1;", "\u00B9");
	htmlEntities.put("&ordm;", "\u00BA");
	htmlEntities.put("&raquo;", "\u00BB");
	htmlEntities.put("&frac14;", "\u00BC");
	htmlEntities.put("&frac12;", "\u00BD");
	htmlEntities.put("&frac34;", "\u00BE");
	htmlEntities.put("&iquest;", "\u00BF");
	htmlEntities.put("&Agrave;", "\u00C0");
	htmlEntities.put("&Aacute;", "\u00C1");
	htmlEntities.put("&Acirc;", "\u00C2");
	htmlEntities.put("&Atilde;", "\u00C3");
	htmlEntities.put("&Auml;", "\u00C4");
	htmlEntities.put("&Aring;", "\u00C5");
	htmlEntities.put("&AElig;", "\u00C6");
	htmlEntities.put("&Ccedil;", "\u00C7");
	htmlEntities.put("&Egrave;", "\u00C8");
	htmlEntities.put("&Eacute;", "\u00C9");
	htmlEntities.put("&Ecirc;", "\u00CA");
	htmlEntities.put("&Euml;", "\u00CB");
	htmlEntities.put("&Igrave;", "\u00CC");
	htmlEntities.put("&Iacute;", "\u00CD");
	htmlEntities.put("&Icirc;", "\u00CE");
	htmlEntities.put("&Iuml;", "\u00CF");
	htmlEntities.put("&ETH;", "\u00D0");
	htmlEntities.put("&Ntilde;", "\u00D1");
	htmlEntities.put("&Ograve;", "\u00D2");
	htmlEntities.put("&Oacute;", "\u00D3");
	htmlEntities.put("&Ocirc;", "\u00D4");
	htmlEntities.put("&Otilde;", "\u00D5");
	htmlEntities.put("&Ouml;", "\u00D6");
	htmlEntities.put("&times;", "\u00D7");
	htmlEntities.put("&Oslash;", "\u00D8");
	htmlEntities.put("&Ugrave;", "\u00D9");
	htmlEntities.put("&Uacute;", "\u00DA");
	htmlEntities.put("&Ucirc;", "\u00DB");
	htmlEntities.put("&Uuml;", "\u00DC");
	htmlEntities.put("&Yacute;", "\u00DD");
	htmlEntities.put("&THORN;", "\u00DE");
	htmlEntities.put("&szlig;", "\u00DF");
	htmlEntities.put("&agrave;", "\u00E0");
	htmlEntities.put("&aacute;", "\u00E1");
	htmlEntities.put("&acirc;", "\u00E2");
	htmlEntities.put("&atilde;", "\u00E3");
	htmlEntities.put("&auml;", "\u00E4");
	htmlEntities.put("&aring;", "\u00E5");
	htmlEntities.put("&aelig;", "\u00E6");
	htmlEntities.put("&ccedil;", "\u00E7");
	htmlEntities.put("&egrave;", "\u00E8");
	htmlEntities.put("&eacute;", "\u00E9");
	htmlEntities.put("&ecirc;", "\u00EA");
	htmlEntities.put("&euml;", "\u00EB");
	htmlEntities.put("&igrave;", "\u00EC");
	htmlEntities.put("&iacute;", "\u00ED");
	htmlEntities.put("&icirc;", "\u00EE");
	htmlEntities.put("&iuml;", "\u00EF");
	htmlEntities.put("&eth;", "\u00F0");
	htmlEntities.put("&ntilde;", "\u00F1");
	htmlEntities.put("&ograve;", "\u00F2");
	htmlEntities.put("&oacute;", "\u00F3");
	htmlEntities.put("&ocirc;", "\u00F4");
	htmlEntities.put("&otilde;", "\u00F5");
	htmlEntities.put("&ouml;", "\u00F6");
	htmlEntities.put("&divide;", "\u00F7");
	htmlEntities.put("&oslash;", "\u00F8");
	htmlEntities.put("&ugrave;", "\u00F9");
	htmlEntities.put("&uacute;", "\u00FA");
	htmlEntities.put("&ucirc;", "\u00FB");
	htmlEntities.put("&uuml;", "\u00FC");
	htmlEntities.put("&yacute;", "\u00FD");
	htmlEntities.put("&thorn;", "\u00FE");
	htmlEntities.put("&yuml;", "\u00FF");
	htmlEntities.put("&OElig;", "\u0152");
	htmlEntities.put("&oelig;", "\u0153");
	htmlEntities.put("&Scaron;", "\u0160");
	htmlEntities.put("&scaron;", "\u0161");
	htmlEntities.put("&Yuml;", "\u0178");
	htmlEntities.put("&fnof;", "\u0192");
	htmlEntities.put("&circ;", "\u02C6");
	htmlEntities.put("&tilde;", "\u02DC");
	htmlEntities.put("&Alpha;", "\u0391");
	htmlEntities.put("&Beta;", "\u0392");
	htmlEntities.put("&Gamma;", "\u0393");
	htmlEntities.put("&Delta;", "\u0394");
	htmlEntities.put("&Epsilon;", "\u0395");
	htmlEntities.put("&Zeta;", "\u0396");
	htmlEntities.put("&Eta;", "\u0397");
	htmlEntities.put("&Theta;", "\u0398");
	htmlEntities.put("&Iota;", "\u0399");
	htmlEntities.put("&Kappa;", "\u039A");
	htmlEntities.put("&Lambda;", "\u039B");
	htmlEntities.put("&Mu;", "\u039C");
	htmlEntities.put("&Nu;", "\u039D");
	htmlEntities.put("&Xi;", "\u039E");
	htmlEntities.put("&Omicron;", "\u039F");
	htmlEntities.put("&Pi;", "\u03A0");
	htmlEntities.put("&Rho;", "\u03A1");
	htmlEntities.put("&Sigma;", "\u03A3");
	htmlEntities.put("&Tau;", "\u03A4");
	htmlEntities.put("&Upsilon;", "\u03A5");
	htmlEntities.put("&Phi;", "\u03A6");
	htmlEntities.put("&Chi;", "\u03A7");
	htmlEntities.put("&Psi;", "\u03A8");
	htmlEntities.put("&Omega;", "\u03A9");
	htmlEntities.put("&alpha;", "\u03B1");
	htmlEntities.put("&beta;", "\u03B2");
	htmlEntities.put("&gamma;", "\u03B3");
	htmlEntities.put("&delta;", "\u03B4");
	htmlEntities.put("&epsilon;", "\u03B5");
	htmlEntities.put("&zeta;", "\u03B6");
	htmlEntities.put("&eta;", "\u03B7");
	htmlEntities.put("&theta;", "\u03B8");
	htmlEntities.put("&iota;", "\u03B9");
	htmlEntities.put("&kappa;", "\u03BA");
	htmlEntities.put("&lambda;", "\u03BB");
	htmlEntities.put("&mu;", "\u03BC");
	htmlEntities.put("&nu;", "\u03BD");
	htmlEntities.put("&xi;", "\u03BE");
	htmlEntities.put("&omicron;", "\u03BF");
	htmlEntities.put("&pi;", "\u03C0");
	htmlEntities.put("&rho;", "\u03C1");
	htmlEntities.put("&sigmaf;", "\u03C2");
	htmlEntities.put("&sigma;", "\u03C3");
	htmlEntities.put("&tau;", "\u03C4");
	htmlEntities.put("&upsilon;", "\u03C5");
	htmlEntities.put("&phi;", "\u03C6");
	htmlEntities.put("&chi;", "\u03C7");
	htmlEntities.put("&psi;", "\u03C8");
	htmlEntities.put("&omega;", "\u03C9");
	htmlEntities.put("&thetasym;", "\u03D1");
	htmlEntities.put("&upsih;", "\u03D2");
	htmlEntities.put("&piv;", "\u03D6");
	htmlEntities.put("&ensp;", "\u2002");
	htmlEntities.put("&emsp;", "\u2003");
	htmlEntities.put("&thinsp;", "\u2009");
	htmlEntities.put("&zwnj;", "\u200C");
	htmlEntities.put("&zwj;", "\u200D");
	htmlEntities.put("&lrm;", "\u200E");
	htmlEntities.put("&rlm;", "\u200F");
	htmlEntities.put("&ndash;", "\u2013");
	htmlEntities.put("&mdash;", "\u2014");
	htmlEntities.put("&lsquo;", "\u2018");
	htmlEntities.put("&rsquo;", "\u2019");
	htmlEntities.put("&sbquo;", "\u201A");
	htmlEntities.put("&ldquo;", "\u201C");
	htmlEntities.put("&rdquo;", "\u201D");
	htmlEntities.put("&bdquo;", "\u201E");
	htmlEntities.put("&dagger;", "\u2020");
	htmlEntities.put("&Dagger;", "\u2021");
	htmlEntities.put("&bull;", "\u2022");
	htmlEntities.put("&hellip;", "\u2026");
	htmlEntities.put("&permil;", "\u2030");
	htmlEntities.put("&prime;", "\u2032");
	htmlEntities.put("&Prime;", "\u2033");
	htmlEntities.put("&lsaquo;", "\u2039");
	htmlEntities.put("&rsaquo;", "\u203A");
	htmlEntities.put("&oline;", "\u203E");
	htmlEntities.put("&frasl;", "\u2044");
	htmlEntities.put("&euro;", "\u20AC");
	htmlEntities.put("&image;", "\u2111");
	htmlEntities.put("&weierp;", "\u2118");
	htmlEntities.put("&real;", "\u211C");
	htmlEntities.put("&trade;", "\u2122");
	htmlEntities.put("&alefsym;", "\u2135");
	htmlEntities.put("&larr;", "\u2190");
	htmlEntities.put("&uarr;", "\u2191");
	htmlEntities.put("&rarr;", "\u2192");
	htmlEntities.put("&darr;", "\u2193");
	htmlEntities.put("&harr;", "\u2194");
	htmlEntities.put("&crarr;", "\u21B5");
	htmlEntities.put("&lArr;", "\u21D0");
	htmlEntities.put("&uArr;", "\u21D1");
	htmlEntities.put("&rArr;", "\u21D2");
	htmlEntities.put("&dArr;", "\u21D3");
	htmlEntities.put("&hArr;", "\u21D4");
	htmlEntities.put("&forall;", "\u2200");
	htmlEntities.put("&part;", "\u2202");
	htmlEntities.put("&exist;", "\u2203");
	htmlEntities.put("&empty;", "\u2205");
	htmlEntities.put("&nabla;", "\u2207");
	htmlEntities.put("&isin;", "\u2208");
	htmlEntities.put("&notin;", "\u2209");
	htmlEntities.put("&ni;", "\u220B");
	htmlEntities.put("&prod;", "\u220F");
	htmlEntities.put("&sum;", "\u2211");
	htmlEntities.put("&minus;", "\u2212");
	htmlEntities.put("&lowast;", "\u2217");
	htmlEntities.put("&radic;", "\u221A");
	htmlEntities.put("&prop;", "\u221D");
	htmlEntities.put("&infin;", "\u221E");
	htmlEntities.put("&ang;", "\u2220");
	htmlEntities.put("&and;", "\u2227");
	htmlEntities.put("&or;", "\u2228");
	htmlEntities.put("&cap;", "\u2229");
	htmlEntities.put("&cup;", "\u222A");
	htmlEntities.put("&int;", "\u222B");
	htmlEntities.put("&there4;", "\u2234");
	htmlEntities.put("&sim;", "\u223C");
	htmlEntities.put("&cong;", "\u2245");
	htmlEntities.put("&asymp;", "\u2248");
	htmlEntities.put("&ne;", "\u2260");
	htmlEntities.put("&equiv;", "\u2261");
	htmlEntities.put("&le;", "\u2264");
	htmlEntities.put("&ge;", "\u2265");
	htmlEntities.put("&sub;", "\u2282");
	htmlEntities.put("&sup;", "\u2283");
	htmlEntities.put("&nsub;", "\u2284");
	htmlEntities.put("&sube;", "\u2286");
	htmlEntities.put("&supe;", "\u2287");
	htmlEntities.put("&oplus;", "\u2295");
	htmlEntities.put("&otimes;", "\u2297");
	htmlEntities.put("&perp;", "\u22A5");
	htmlEntities.put("&sdot;", "\u22C5");
	htmlEntities.put("&lceil;", "\u2308");
	htmlEntities.put("&rceil;", "\u2309");
	htmlEntities.put("&lfloor;", "\u230A");
	htmlEntities.put("&rfloor;", "\u230B");
	htmlEntities.put("&lang;", "\u2329");
	htmlEntities.put("&rang;", "\u232A");
	htmlEntities.put("&loz;", "\u25CA");
	htmlEntities.put("&spades;", "\u2660");
	htmlEntities.put("&clubs;", "\u2663");
	htmlEntities.put("&hearts;", "\u2665");
	htmlEntities.put("&diams;", "\u2666");

  allowedSchemes.add("https://");
  allowedSchemes.add("http://");
  allowedSchemes.add("ftp://");
  allowedSchemes.add("mailto:");
  }
	

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
    // Use a single buffer for efficiency
    StringBuffer buffer = new StringBuffer();
    // The position in the original string that we are up to
    int position = 0;
    while (m.find()) {
      int start = m.start();
      int end = m.end();
      buffer.append(s.subSequence(position, start)).append("<a href=");
      String link = s.substring(start, end);
      int startOfHrefIndex = link.indexOf("href=&quot;");
      if (startOfHrefIndex > -1) {
        int startOfHrefValue = startOfHrefIndex + "href=&quot;".length();
        int endOfHrefIndex = link.indexOf("&quot;", startOfHrefValue);
        buffer.append("\"").append(validateUrl(link.substring(startOfHrefValue, endOfHrefIndex))).append("\"");
      } else {
        startOfHrefIndex = link.indexOf("href='");
        if (startOfHrefIndex > -1) {
          int startOfHrefValue = startOfHrefIndex + "href='".length();
          int endOfHrefIndex = link.indexOf("'", startOfHrefIndex+"href='".length());
          buffer.append("'").append(validateUrl(link.substring(startOfHrefValue, endOfHrefIndex))).append("'");
        }
      }
      buffer.append(">");
      position = end;
    }
    // If position is still 0 there were no matches, so don't do anything
    if (position > 0) {
      buffer.append(s.subSequence(position, s.length()));
      s = buffer.toString();
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
    s = s.replaceAll("(?s)<[Ss][Cc][Rr][Ii][Pp][Tt].*?>.*?</[Ss][Cc][Rr][Ii][Pp][Tt]>", "");
    s = s.replaceAll("(?s)<[Ss][Tt][Yy][Ll][Ee].*?>.*?</[Ss][Tt][Yy][Ll][Ee]>", "");
    s = s.replaceAll("(?s)<!--.*?-->", "");
    s = s.replaceAll("(?s)<.*?>", "");
    return s;
  }

  private static String extractFromRegexp(Pattern pattern, String content) {
    Matcher m = pattern.matcher(content);
    if (m.find()) {
      return m.group(1);
    }
    return null;
  }
  
  public static String findThumbnailUrl(String articleBody) {
    //thumbnail
    for (Pattern p : imagesPatterns) {
      String url = extractFromRegexp(p, articleBody);
      if (url != null && (url.indexOf("http://")==0 || url.indexOf("https://")==0)) 
          return url;
    }
    return null;
  }
  
  public static String truncate(String s) {
    return truncate(s, MAX_CONTENT_LENGTH);
  }

  
  /**
   * Removes all HTML tags and then truncate the string.
   * @param s the string to truncate
   * @param maxLength the maximum length of the returned string
   * @return the processed string
   */
  public static String truncate(String s, int maxLength) {
    String content = filterHTML(s);

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


  public static String unescapeHTMLEntities(String source) {
     Iterator<String> it = htmlEntities.keySet().iterator(); 
	 
	 while(it.hasNext()) { 
		 
		 String key = it.next(); 
		 String val = htmlEntities.get(key); 
		 source = source.replaceAll(key, val);
	 } 
     return source;
  }

  public static String validateUrl(String url) {
    if (url == null || url.length() == 0) {
      return null;
    }
    // whitelist, don't blacklist.
    for (String scheme : allowedSchemes) {
      if (url.startsWith(scheme)) {
        return url;
      }
    }
    return "http://" + url;
  }

  /**
   * An implementation of indexOf that returns the string's length instead of -1 in case the pattern is not found.
   * @param orig String to look into
   * @param pattern pattern to look for
   * @param from index of the beginning of the search
   * @return orig.indexOf(pattern,from). If -1, returns orig.length()
   */
  public static int indexOfOrLength(String orig, String pattern, int from) {
    int res = orig.indexOf(pattern,from);
    if (res == -1) res = orig.length();
    return res;
  }
  
  private static List<Pattern> imagesPatterns = new ArrayList<Pattern>();
  
  static {
    imagesPatterns.add(Pattern.compile("<[iI][mM][gG][^>]*? [sS][rR][cC]=[\"']([^\"']*?)[\"'][^>]*? [iI][tT][eE][mM][pP][rR][oO][pP]=[\"']thumbnailURL[\"'][^>]*?>"));
    imagesPatterns.add(Pattern.compile("<[iI][mM][gG][^>]*? [iI][tT][eE][mM][pP][rR][oO][pP]=[\"']thumbnailURL[\"'][^>]*? [sS][rR][cC]=[\"']([^\"'>]*?)[\"'][^>]*?>"));
    imagesPatterns.add(Pattern.compile("<[iI][mM][gG][^>]*? [sS][rR][cC]=[\"']([^\"']*?)[\"'][^>]*?>"));
  }
}
