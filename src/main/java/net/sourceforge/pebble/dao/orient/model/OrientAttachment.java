package net.sourceforge.pebble.dao.orient.model;

import net.sourceforge.pebble.domain.Attachment;

/**
 * An OrientDB attachment
 */
public class OrientAttachment {
  private String url;
  private long size;
  private String type;

  private OrientAttachment() {
  }

  OrientAttachment(Attachment attachment) {
    url = attachment.getUrl();
    size = attachment.getSize();
    type = attachment.getType();
  }

  Attachment toAttachment() {
    return new Attachment(url, size, type);
  }
}
