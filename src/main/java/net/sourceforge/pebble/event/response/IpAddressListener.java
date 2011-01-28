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
package net.sourceforge.pebble.event.response;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.domain.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Checks comment and TrackBack IP address against a whitelist and a blacklist.
 * If in the whitelist, the response is left as-is. If in the blacklist,
 * the response is set to pending and the spam score incremented by 1 point.
 * If in neither, the response is set to pending but the spam score isn't
 * increased. This allows responses from new IP addresses to be manually
 * verified before publication.
 *
 * @author Simon Brown
 */
public class IpAddressListener extends BlogEntryResponseListenerSupport {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(IpAddressListener.class);

  /** the name of the whitelist property */
  public static final String WHITELIST_KEY = "IpAddressListener.whitelist";

  /** the name of the blacklist property */
  public static final String BLACKLIST_KEY = "IpAddressListener.blacklist";

  /**
   * Called when a comment or TrackBack has been added.
   *
   * @param response a Response
   */
  protected void blogEntryResponseAdded(Response response) {
    PluginProperties props = response.getBlogEntry().getBlog().getPluginProperties();

    if (isListed(response, props.getProperty(BLACKLIST_KEY))) {
      log.info(response.getTitle() + " marked as pending : IP address " + response.getIpAddress() + " is on blacklist");
      response.setPending();
      response.incrementSpamScore();
    } else if (isListed(response, props.getProperty(WHITELIST_KEY))) {
      // do nothing
    } else {
      log.info(response.getTitle() + " marked as pending : IP address " + response.getIpAddress() + " not on blacklist or whitelist");
      response.setPending();
    }
  }

  /**
   * Called when a comment or TrackBack has been approved.
   *
   * @param response a Response
   */
  protected void blogEntryResponseApproved(Response response) {
    PluginProperties props = response.getBlogEntry().getBlog().getPluginProperties();

    if (response.getIpAddress() == null || response.getIpAddress().trim().length() == 0) {
      return;
    }

    synchronized (props) {
      String whitelist = props.getProperty(WHITELIST_KEY);
      String blacklist = props.getProperty(BLACKLIST_KEY);
      whitelist = addIpAddress(response, whitelist);
      blacklist = removeIpAddress(response, blacklist);
      props.setProperty(WHITELIST_KEY, whitelist);
      props.setProperty(BLACKLIST_KEY, blacklist);
      props.store();
    }
  }

  /**
   * Called when a comment or TrackBack has been rejected.
   *
   * @param response a Response
   */
  protected void blogEntryResponseRejected(Response response) {
    PluginProperties props = response.getBlogEntry().getBlog().getPluginProperties();

    if (response.getIpAddress() == null || response.getIpAddress().trim().length() == 0) {
      return;
    }

    synchronized (props) {
      String blacklist = props.getProperty(BLACKLIST_KEY);
      String whitelist = props.getProperty(WHITELIST_KEY);
      blacklist = addIpAddress(response, blacklist);
      whitelist = removeIpAddress(response, whitelist);
      props.setProperty(BLACKLIST_KEY, blacklist);
      props.setProperty(WHITELIST_KEY, whitelist);
      props.store();
    }
  }

  /**
   * Determines whether the IP address of the specified response is contained
   * within a given list of IP addresses.
   *
   * @param response    a Response instance
   * @param list        a list of IP addresses, comma separated
   * @return    true if the IP address is contained within the list,
   *            false otherwise
   */
  private boolean isListed(Response response, String list) {
    if (response.getIpAddress() == null) {
      return false;
    }

    String ipAddresses[] = null;
    if (list != null) {
      ipAddresses = list.split(",");
    } else {
      ipAddresses = new String[0];
    }

    for (int i = 0; i < ipAddresses.length; i++) {
      if (response.getIpAddress().equals(ipAddresses[i])) {
        return true;
      }
    }

    return false;
  }

  /**
   * Adds the IP address of the specified response to the given list.
   *
   * @param response    a Response instance
   * @param list        a list of IP addresses, comma separated
   * @return  an updated list of IP addresses
   */
  private String addIpAddress(Response response, String list) {
    if (list == null || list.trim().length() == 0) {
      return response.getIpAddress();
    } else if (!isListed(response, list)) {
      return list + "," + response.getIpAddress();
    } else {
      return list;
    }
  }

  /**
   * Removes the IP address of the specified response to the given list.
   *
   * @param response    a Response instance
   * @param list        a list of IP addresses, comma separated
   * @return  an updated list of IP addresses
   */
  private String removeIpAddress(Response response, String list) {
    if (response.getIpAddress() == null) {
      return list;
    }

    String ipAddresses[] = null;
    if (list != null) {
      ipAddresses = list.split(",");
    } else {
      ipAddresses = new String[0];
    }

    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < ipAddresses.length; i++) {
      if (!response.getIpAddress().equals(ipAddresses[i])) {
        if (buf.length() > 0) {
          buf.append(",");
        }
        buf.append(ipAddresses[i]);
      }
    }
    return buf.toString();
  }

}
