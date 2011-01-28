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
package net.sourceforge.pebble.util.importer;

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.dao.CategoryDAO;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.file.FileDAOFactory;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.State;
import net.sourceforge.pebble.domain.TrackBack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Simple utility to import posts Movable Type into Pebble.
 *
 * @author    Simon Brown
 */
public class MovableTypeImporter {

  /**
   * Starts the importer.
   */
  public static void main(String[] args) throws Exception {
    if (args.length != 3) {
      System.out.println("Usage : net.sourceforge.pebble.util.importer.MovableTypeImporter %1 %2 %3");
      System.out.println("   %1 : location of MT export file");
      System.out.println("   %2 : location of Pebble blog");
      System.out.println("   %3 : time zone (e.g. Europe/London)");

      return;
    }

    File file = new File(args[0]);
    if(null == PebbleContext.getInstance().getConfiguration()){
      //to prevent NullPointerException upon UpdateNotificationPingsClient.sendUpdateNotificationPing()
      Configuration config = new Configuration();
      config.setDataDirectory(args[1]);
      config.setUrl("http://www.yourdomain.com/blog/");
      PebbleContext.getInstance().setConfiguration(config);
    }

    DAOFactory.setConfiguredFactory(new FileDAOFactory());
    Blog blog = new Blog(args[1]);
    blog.setProperty(Blog.TIMEZONE_KEY, args[2]);

    importBlog(blog, file);
  }

  /**
   * Imports a Movable Type blog from an export file.
   *
   * @param blog    the SimpleBlo to import to
   * @param file    the Movable Type export file
   * @throws Exception  if something goes wrong
   */
  private static void importBlog(Blog blog, File file) throws Exception {
    System.out.println("Importing " + file.getName());

    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"));
    BlogEntry blogEntry = null;
    do {
      blogEntry = readBlogEntry(blog, reader);
    } while (blogEntry != null);

    System.out.println(" " + blog.getNumberOfBlogEntries());
  }

  private static BlogEntry readBlogEntry(Blog blog, BufferedReader reader) throws Exception {
    String line = reader.readLine();
    if (line == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
    String author = line.substring("AUTHOR: ".length());
//    System.out.println("Author:" + author);
    String title = reader.readLine().substring("TITLE: ".length());
//    System.out.println("Title:" + title);
    String status = reader.readLine().substring("STATUS: ".length());
//    System.out.println("Status:" + status);
    String allowComments = reader.readLine().substring("ALLOW COMMENTS: ".length());
//    System.out.println("Allow comments:" + allowComments);
    String convertBreaks = reader.readLine().substring("CONVERT BREAKS: ".length());
//    System.out.println("Convert breaks:" + convertBreaks);
    String allowPings = reader.readLine().substring("ALLOW PINGS: ".length());
//    System.out.println("Allow pings:" + allowPings);
//    String primaryCategory = "";
    List<String> categories = new ArrayList<String>(1);
    line = reader.readLine();
    //    System.out.println("Primary category:" + primaryCategory);
    if(line.length() != 0){
      // the entry is categorized
//      primaryCategory = line.substring("PRIMARY CATEGORY: ".length());
      //    System.out.println("Primary category:" + primaryCategory);
//      if (primaryCategory.trim().length() > 0) {
        line = reader.readLine();
        while(line.length() > 0){
          if(line.indexOf("CATEGORY: ") != -1){
            String category = line.substring(line.indexOf("CATEGORY: ")+10);
            if(category.trim().length() > 0){
              categories.add(category);
            }
//            categories.add(line.substring(line.indexOf("CATEGORY: ")+10));
          }
          line = reader.readLine();
        }
//      System.out.println("Category:" + category);
//      }else{
//        reader.readLine(); // blank line
//      }
    }
    Date date = sdf.parse(reader.readLine().substring("DATE: ".length()));
//    System.out.println("Date:" + date);

    reader.readLine();
    reader.readLine();
    StringBuffer body = new StringBuffer();
    String bodyLine = reader.readLine();
    while (!bodyLine.equals("-----")) {
      body.append(bodyLine);
      bodyLine = reader.readLine();
      if (!bodyLine.equals("-----")) {
        body.append("<br />");
      }
    }
//    System.out.println("Body:" + body);

    reader.readLine();
    StringBuffer extendedBody = new StringBuffer();
    String extendedBodyLine = reader.readLine();
    while (!extendedBodyLine.equals("-----")) {
      extendedBody.append(extendedBodyLine);
      extendedBodyLine = reader.readLine();
      if (!extendedBodyLine.equals("-----")) {
        extendedBody.append("<br />");
      }
    }
//    System.out.println("Extended body:" + extendedBody);

    reader.readLine();
    StringBuffer excerpt = new StringBuffer();
    String excerptLine = reader.readLine();
    while (!excerptLine.equals("-----")) {
      excerpt.append(excerptLine);
      excerptLine = reader.readLine();
      if (!excerptLine.equals("-----")) {
        excerpt.append("<br />");
      }
    }
//    System.out.println("Excerpt:" + excerpt);

    reader.readLine();
    StringBuffer keywords = new StringBuffer();
    String keywordsLine = reader.readLine();
    while (!keywordsLine.equals("-----")) {
      keywords.append(keywordsLine);
      keywordsLine = reader.readLine();
      if (!keywordsLine.equals("-----")) {
        keywords.append("<br />");
      }
    }
//    System.out.println("Keywords:" + keywords);

    // create a new Pebble entry, add and store
    BlogEntry entry = new BlogEntry(blog);
    entry.setTitle(title);
    if (extendedBody.length() != 0) {
      entry.setBody(body+"<br />"+extendedBody);
      entry.setExcerpt(body.toString());
    }else{
      entry.setBody(body.toString());
    }
    entry.setDate(date);
    if (excerpt.length() != 0) {
      entry.setExcerpt(excerpt.toString());
    }else if(extendedBody.length() != 0){
      entry.setExcerpt(body.toString());
    }
    entry.setAuthor(author);
    entry.setCommentsEnabled(allowComments.equals("1"));
    entry.setTrackBacksEnabled(allowPings.equals("1"));

    for (String categoryStr : categories) {
      if(categoryStr != null && categoryStr.trim().length() > 0) {
        Category category = new Category(categoryStr.trim(), categoryStr.trim());
        DAOFactory factory = DAOFactory.getConfiguredFactory();
        CategoryDAO dao = factory.getCategoryDAO();
        dao.addCategory(category, blog);
        blog.addCategory(category);
        entry.addCategory(category);
      }
    }
    entry.setPublished("Publish".equals(status));

    BlogService service = new BlogService();
    service.putBlogEntry(entry);

    line = reader.readLine();
    while (!line.equals("--------")) {
      if (line.equals("COMMENT:")) {
        String commentAuthor = reader.readLine().substring("AUTHOR: ".length());
        String commentEmail = reader.readLine().substring("EMAIL: ".length());
        String commentIpAddress = reader.readLine().substring("IP: ".length());
        String commentUrl = reader.readLine().substring("URL: ".length());
        Date commentDate = sdf.parse(reader.readLine().substring("DATE: ".length()));

        StringBuffer commentBody = new StringBuffer();
        String commentBodyLine = reader.readLine();
        while (!commentBodyLine.equals("-----")) {
          commentBody.append(commentBodyLine);
          commentBodyLine = reader.readLine();
          if (!commentBodyLine.equals("-----")) {
            commentBody.append("<br />");
          }
        }

        Comment comment = entry.createComment(null, commentBody.toString(), commentAuthor, commentEmail, commentUrl, "", commentIpAddress, commentDate, State.APPROVED);
        entry.addComment(comment);
      } else if (line.equals("PING:")) {
        String pingTitle = reader.readLine().substring("TITLE: ".length());
        String pingUrl = reader.readLine().substring("URL: ".length());
        String pingIpAddress = reader.readLine().substring("IP: ".length());
        String pingBlogName = reader.readLine().substring("BLOG NAME: ".length());
        Date pingDate = sdf.parse(reader.readLine().substring("DATE: ".length()));

        StringBuffer pingBody = new StringBuffer();
        String pingBodyLing = reader.readLine();
        while (!pingBodyLing.equals("-----")) {
          pingBody.append(pingBodyLing);
          pingBodyLing = reader.readLine();
          if (!pingBodyLing.equals("-----")) {
            pingBody.append("<br />");
          }
        }

        TrackBack trackBack = entry.createTrackBack(pingTitle, pingBody.toString(), pingUrl, pingBlogName, pingIpAddress, pingDate, State.APPROVED);
        entry.addTrackBack(trackBack);
      }
      line = reader.readLine();
    }

    service.putBlogEntry(entry);

//    System.out.println("--------------------------------------------------");
    System.out.print(".");

    return entry;
  }

}
