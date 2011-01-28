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
package net.sourceforge.pebble.domain;


/**
 * Represents a blog entry attachment (such as that used within an RSS
 * enclosure).
 *
 * @author Simon Brown
 */
public class Attachment implements Cloneable {

  /** the URL of the attachment */
  private String url;

  /** the content size (length in bytes) */
  private long size;

  /** the content type */
  private String type;

  /**
   * Default, no args constructor.
   */
  public Attachment() {
  }

  /**
   * Creates an instance with the specified properties.
   *
   * @param url   a URL as a String
   * @param size    the size in bytes
   * @param type    a content type as a String
   */
  public Attachment(String url, long size, String type) {
    setUrl(url);
    setSize(size);
    setType(type);
  }

  /**
   * Gets the URL.
   *
   * @return  the URL as a String
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the URL.
   *
   * @param url   a URL as a String
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Gets the size in bytes.
   *
   * @return  the size of the attachement in bytes
   */
  public long getSize() {
    return size;
  }

  /**
   * Sets the size of the attachement in bytes.
   *
   * @param size    the size in bytes
   */
  public void setSize(long size) {
    this.size = size;
  }

  /**
   * Gets the content type.
   *
   * @return  a MIME type as a String
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the content type.
   *
   * @param type    a content type as a String
   */
  public void setType(String type) {
    this.type = type;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Attachment)) {
      return false;
    }

    final Attachment attachment = (Attachment) o;
    if (url == null || url.length() == 0) {
      return attachment.url == null || attachment.url.length() == 0;
    } else {
      return url.equals(attachment.url);
    }
  }

  public int hashCode() {
    return url.hashCode();
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance.
   * @see Cloneable
   */
  public Object clone() {
    Attachment attachment = new Attachment();
    attachment.setUrl(url);
    attachment.setSize(size);
    attachment.setType(type);

    return attachment;
  }

}
