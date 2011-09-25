package net.sourceforge.pebble.dao.orient.model;

import net.sourceforge.pebble.domain.Response;

import java.util.Date;

/**
 * An OrientDB response
 */
public class OrientResponse {
  protected String title;
  protected String ipAddress;
  protected Date date;

  protected OrientResponse() {
  }

  OrientResponse(Response response) {
    title = response.getTitle();
    ipAddress = response.getIpAddress();
    date = response.getDate();
  }
}
