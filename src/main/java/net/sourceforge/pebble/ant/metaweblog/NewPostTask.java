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
package net.sourceforge.pebble.ant.metaweblog;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.xmlrpc.XmlRpcClient;

/**
 * Ant task to post to a blog via the MetaWeblog API.
 *
 * @author    Simon Brown
 */
public class NewPostTask extends Task {

  private String blogid;
  private String username;
  private String password;
  private String title;
  private String content;
  private String category;
  private String url;
  private String handler = "metaWeblog";

  /**
   * Performs the work of this task.
   *
   * @throws org.apache.tools.ant.BuildException   if something goes wrong
   */
  public void execute() throws BuildException {

    try {
      System.out.println("Calling " + handler + ".newPost at " + url);
      System.out.println(" blogid=" + blogid);
      System.out.println(" username=" + username);
      System.out.println(" password=********");
      System.out.println(" title=" + title);
      System.out.println(" content=" + content);
      System.out.println(" category=" + category);
      System.out.println(" publish=true");

      XmlRpcClient xmlrpc = new XmlRpcClient(url);
      Vector params = new Vector();
      params.add(blogid);
      params.add(username);
      params.add(password);

      Hashtable struct = new Hashtable();
      struct.put("title", title);
      struct.put("description", content);
      Vector categories = new Vector();
      if (category != null) {
        StringTokenizer tok = new StringTokenizer(category, ",");
        while (tok.hasMoreTokens()) {
          categories.add(tok.nextToken().trim());
        }
      }
      struct.put("categories", categories);

      params.add(struct);
      params.add(Boolean.TRUE);
      Object postId = xmlrpc.execute(handler + ".newPost", params);

      System.out.println("New post id is " + postId);

    } catch (Exception e) {
      throw new BuildException(e);
    }
  }

  /**
   * Sets the blog id.
   *
   * @param blogid    a String
   */
  public void setBlogid(String blogid) {
    this.blogid = blogid;
  }

  /**
   * Sets the XML-RPC username.
   *
   * @param username    a String
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Sets the XML-RPC password.
   *
   * @param password    a String
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Sets the title of the blog entry.
   *
   * @param title   the blog entry title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Sets the blog entry content.
   *
   * @param content   the blog entry content
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Sets the category to which the blog entry should be added to.
   *
   * @param category    the category ID as a String
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Sets the URL of the XML-RPC call.
   *
   * @param url   the URL as a String
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Sets the name of the XML-RPC Blogger API handler (e.g. "blogger").
   *
   * @param handler   the name of the handler as a String
   */
  public void setHandler(String handler) {
    this.handler = handler;
  }

}