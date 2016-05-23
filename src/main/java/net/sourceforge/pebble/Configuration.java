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

package net.sourceforge.pebble;

import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.file.FileDAOFactory;
import net.sourceforge.pebble.security.SecurityRealm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A bean representing configurable properties for Pebble.
 *
 * @author    Simon Brown
 */
public class Configuration {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(Configuration.class);

  private String dataDirectory = evaluateDirectory("${user.home}/pebble");
  private String url;
  private String secureUrl;
  private boolean multiBlog = false;
  private boolean multiBlogHttps = false;
  private boolean virtualHostingEnabled = false;
  private boolean virtualHostingSubdomain = true;
  private boolean userThemesEnabled = true;
  private String smtpHost = "java:comp/env/mail/Session";
  private String smtpPort = "25";
  private long fileUploadSize = 2048;
  private long fileUploadQuota = -1;
  private DAOFactory daoFactory = new FileDAOFactory();
  private SecurityRealm securityRealm;

  public Configuration() {
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String s) {
    this.url = s;

    if (url != null && !(url.length() == 0) && !url.endsWith("/")) {
      url += "/";
    }
  }

  public String getDomainName() {
    // and set the domain name
    String url = PebbleContext.getInstance().getConfiguration().getUrl();
    int index = url.indexOf("://");
    String domainName = url.substring(index+3);
    index = domainName.indexOf("/");
    domainName = domainName.substring(0, index);

    if (domainName.indexOf(":") > -1) {
      // the domain name still has a port number so remove it
      domainName = domainName.substring(0, domainName.indexOf(":"));
    }

    return domainName;
  }

  public String getSecureUrl() {
    if (secureUrl != null && secureUrl.length() > 0) {
      return secureUrl;
    } else {
      return url;
    }
  }

  public void setSecureUrl(String s) {
    this.secureUrl = s;

    if (secureUrl != null && !(secureUrl.length() == 0) && !secureUrl.endsWith("/")) {
      secureUrl += "/";
    }
  }

  public String getSmtpHost() {
    return smtpHost;
  }

  public void setSmtpHost(String smtpHost) {
    this.smtpHost = smtpHost;
  }

  public String getSmtpPort() {
    return smtpPort;
  }

  public void setSmtpPort(String smtpPort) {
    this.smtpPort = smtpPort;
  }

  public long getFileUploadSize() {
    return fileUploadSize;
  }

  public void setFileUploadSize(long fileUploadSize) {
    this.fileUploadSize = fileUploadSize;
  }

  public long getFileUploadQuota() {
    return fileUploadQuota;
  }

  public void setFileUploadQuota(long fileUploadQuota) {
    this.fileUploadQuota = fileUploadQuota;
  }

  public DAOFactory getDaoFactory() {
    return daoFactory;
  }

  public void setDaoFactory(DAOFactory daoFactory) {
    this.daoFactory = daoFactory;
  }

  public String getDataDirectory() {
    return dataDirectory;
  }

  public void setDataDirectory(String dataDirectory) {
    this.dataDirectory = evaluateDirectory(dataDirectory);
  }

  public boolean isMultiBlog() {
    return multiBlog;
  }

  public void setMultiBlog(boolean multiBlog) {
    this.multiBlog = multiBlog;
  }


  public boolean isVirtualHostingEnabled() {
    return virtualHostingEnabled;
  }

  public void setVirtualHostingEnabled(boolean virtualHostingEnabled) {
    this.virtualHostingEnabled = virtualHostingEnabled;
  }

  public SecurityRealm getSecurityRealm() {
    return securityRealm;
  }

  public void setSecurityRealm(SecurityRealm securityRealm) {
    this.securityRealm = securityRealm;
  }

  /**
   * Replaces ${some.property} at the start of the string with the value
   * from System.getProperty(some.property).
   *
   * @param s   the String to transform
   * @return  a new String, or the same String if it doesn't start with a
   *          property name delimited by ${...}
   */
  private String evaluateDirectory(String s) {
    log.debug("Raw string is " + s);
    if (s.startsWith("${")) {
      int index = s.indexOf("}");
      String propertyName = s.substring(2, index);
      String propertyValue = System.getProperty(propertyName);
      log.debug(propertyName + " = " + propertyValue);
      return propertyValue + s.substring(index+1);
    } else {
      return s;
    }
  }

  /**
   * Determines whether user themes are enabled.
   *
   * @return    true if user themes are enabled, false otherwise
   */
  public boolean isUserThemesEnabled() {
    return userThemesEnabled;
  }

  /**
   * Sets whether user themes are enabled.
   *
   * @param userThemesEnabled   true if user themes are enabled,
   *                            false otherwise
   */
  public void setUserThemesEnabled(boolean userThemesEnabled) {
    this.userThemesEnabled = userThemesEnabled;
  }

  public void setVirtualHostingSubdomain(boolean virtualHostingSubdomain) {
    this.virtualHostingSubdomain = virtualHostingSubdomain;
  }

  public boolean isVirtualHostingSubdomain() {
    return virtualHostingSubdomain;
  }

  public void setMultiBlogHttps(boolean multiBlogHttps) {
    this.multiBlogHttps = multiBlogHttps;
  }

  public boolean isMultiBlogHttps() {
    return multiBlogHttps;
  }
}