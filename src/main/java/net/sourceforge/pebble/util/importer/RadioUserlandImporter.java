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

import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.file.FileDAOFactory;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple utility to import posts Radio Userland into Pebble.
 *
 * @author    Simon Brown
 */
public class RadioUserlandImporter {

  /**
   * Starts the importer.
   */
  public static void main(String[] args) throws Exception {
    File root = new File(args[0]);
    File sources[] = root.listFiles();
    DAOFactory.setConfiguredFactory(new FileDAOFactory());
    Blog blog = new Blog(args[1]);
    blog.setProperty(Blog.TIMEZONE_KEY, args[2]);

    for (int i = 0; i < sources.length; i++) {
      importFile(blog, sources[i]);
    }
  }

  private static void importFile(Blog blog, File source) throws Exception {
    System.out.println("Importing " + source.getName());
    // create a factory and builder - an abstraction for an XML parser
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(false);
    factory.setNamespaceAware(true);
    factory.setIgnoringElementContentWhitespace(true);
    factory.setIgnoringComments(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    builder.setErrorHandler(new ErrorHandler() {
      public void warning(SAXParseException e) throws SAXException {
        System.out.println("Warning : " + e.getMessage());
        throw e;
      }

      public void error(SAXParseException e) throws SAXException {
        System.out.println("Error : " + e.getMessage());
        throw e;
      }

      public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("Fatal : " + e.getMessage());
        throw e;
      }
    });

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    String title = "No title";
    String body = "";
    Date date = null;
    Document doc = builder.parse(source);
    Node root = doc.getDocumentElement();
    NodeList nodes = root.getChildNodes();
    System.out.println(nodes.getLength());
    for (int i = 0; i < nodes.getLength(); i++) {
      Node n = nodes.item(i);
      System.out.println(n.getNodeName());

      if (n.getNodeName().equals("string")) {
        System.out.println(n.getAttributes().getNamedItem("name"));
        System.out.println(n.getAttributes().getNamedItem("name").getNodeValue());
        if (n.getAttributes().getNamedItem("name").getNodeValue().equals("title")) {
          title = n.getAttributes().getNamedItem("value").getNodeValue();
          System.out.println("Title : " + title);
        } else if (n.getAttributes().getNamedItem("name").getNodeValue().equals("text")) {
          body = n.getAttributes().getNamedItem("value").getNodeValue();
          System.out.println("Body : " + body);
        }
      }

      if (n.getNodeName().equals("date") && n.getAttributes().getNamedItem("name").getNodeValue().equals("when")) {
        date = sdf.parse((n.getAttributes().getNamedItem("value").getNodeValue()).substring(4));
        System.out.println("Date : " + date);
      }
    }

    BlogEntry entry = new BlogEntry(blog);
    entry.setTitle(title);
    entry.setBody(body);
    entry.setDate(date);

    BlogService service = new BlogService();
    service.putBlogEntry(entry);
  }

}
