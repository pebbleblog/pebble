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
package net.sourceforge.pebble.index;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.domain.BlogEntry;

import java.util.*;
import java.io.*;

/**
 * Represents the tag index for a blog.
 *
 * @author    Simon Brown
 */
public class EmailSubscriptionList {

  private static final Log log = LogFactory.getLog(EmailSubscriptionList.class);

  private Blog blog;

  /** the list of e-mail addresses */
  private List<String> emailAddresses = new LinkedList<String>();

  public EmailSubscriptionList(Blog blog) {
    this.blog = blog;

    readIndex();
  }

  /**
   * Clears the index.
   */
  public void clear() {
    emailAddresses = new LinkedList<String>();
    writeIndex();
  }

  /**
   * Adds an e-mail address.
   *
   * @param emailAddress    an e-mail address
   */
  public synchronized void addEmailAddress(String emailAddress) {
    if (!emailAddresses.contains(emailAddress)) {
      emailAddresses.add(emailAddress);
      writeIndex();
    }
  }

  /**
   * Removes an e-mail address.
   *
   * @param emailAddress    an e-mail address
   */
  public synchronized void removeEmailAddress(String emailAddress) {
    emailAddresses.remove(emailAddress);
    writeIndex();
  }

  /**
   * Helper method to load the index.
   */
  private void readIndex() {
    File indexFile = new File(blog.getIndexesDirectory(), "email-subscriptions.index");
    if (indexFile.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String emailAddress = reader.readLine();
        while (emailAddress != null) {
          emailAddresses.add(emailAddress);
          emailAddress = reader.readLine();
        }

        reader.close();
      } catch (Exception e) {
        log.error("Error while reading index", e);
      }
    }
  }

  /**
   * Helper method to write out the index to disk.
   */
  private void writeIndex() {
    try {
      File indexFile = new File(blog.getIndexesDirectory(), "email-subscriptions.index");
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (String emailAddress : emailAddresses) {
        writer.write(emailAddress);
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error("Error while writing index", e);
    }
  }

  /**
   * Gets the list of e-mail addresses.
   */
  public List<String> getEmailAddresses() {
    List list = new ArrayList<String>(emailAddresses);
    Collections.sort(list);
    return list;
  }

}
