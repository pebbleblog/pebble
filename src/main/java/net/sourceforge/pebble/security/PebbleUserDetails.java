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

package net.sourceforge.pebble.security;

import net.sourceforge.pebble.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Extension of the Acegi User class that adds additional information
 * such as the user's e-mail address.
 *
 * @author    Simon Brown
 */
public class PebbleUserDetails implements UserDetails {

  public static final String OPEN_IDS_PREFERENCE = "openids";
  // Reserved unwise character from RFC-2396
  private static final String OPEN_IDS_SEPARATOR = "|";

  private String username;
  private String password;

  /** the name */
  private String name;

  /** the e-mail address */
  private String emailAddress;

  /** the website */
  private String website;

  /** the profile */
  private String profile;

  /** the user's preferences */
  private Map<String,String> preferences = new HashMap<String,String>();

  private Collection<GrantedAuthority> grantedAuthories;

  private boolean detailsUpdateable = true;

  private Collection<String> openIds;

  public PebbleUserDetails() {
  }

  public PebbleUserDetails(String username, String name, String emailAddress, String website, String profile, String roles[], Map<String,String> preferences, boolean detailsUpdateable) {
    this.username = username;
    this.name = name;
    this.emailAddress = emailAddress;
    this.website = website;
    this.profile = profile;
    this.grantedAuthories = createGrantedAuthorities(roles);
    this.preferences = preferences;
    this.detailsUpdateable = detailsUpdateable;
  }

  public PebbleUserDetails(String username, String password, String name, String emailAddress, String website, String profile, String roles[], Map<String,String> preferences, boolean detailsUpdateable) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.emailAddress = emailAddress;
    this.website = website;
    this.profile = profile;
    this.grantedAuthories = createGrantedAuthorities(roles);
    this.preferences = preferences;
    this.detailsUpdateable = detailsUpdateable;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public boolean isAccountNonExpired() {
    return true;
  }

  public boolean isAccountNonLocked() {
    return true;
  }

  public boolean isCredentialsNonExpired() {
    return true;
  }

  public boolean isEnabled() {
    return true;
  }

  /**
   * Gets the name.
   *
   * @return  a String
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the e-mail address.
   *
   * @return  a String
   */
  public String getEmailAddress() {
    return emailAddress;
  }

  /**
   * Gets the website.
   *
   * @return  a String
   */
  public String getWebsite() {
    return website;
  }

  /**
   * Gets the user's profile.
   *
   * @return  a String
   */
  public String getProfile() {
    return this.profile;
  }

  public Collection<GrantedAuthority> getAuthorities() {
    return this.grantedAuthories;
  }

  public String[] getRoles() {
    Collection<GrantedAuthority> authorities = getAuthorities();
    String[] roles = new String[authorities.size()];
    int i = 0;
    for (GrantedAuthority authority: authorities) {
      roles[i++] = authority.getAuthority();
    }

    return roles;
  }

  public String getRolesAsString() {
    StringBuffer buf = new StringBuffer();

    String sep = "";
    for (GrantedAuthority authority: getAuthorities()) {
      buf.append(sep);
      sep = ",";
      buf.append(authority.getAuthority());
    }

    return buf.toString();
  }

  public boolean isUserInRole(String role) {
    Collection<GrantedAuthority> authorities = getAuthorities();
    if (authorities != null) {
      for (GrantedAuthority authority : authorities) {
        if (authority.getAuthority().equals(role)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isBlogAdmin() {
    return isUserInRole(Constants.BLOG_ADMIN_ROLE);
  }

  public boolean isBlogOwner() {
    return isUserInRole(Constants.BLOG_OWNER_ROLE);
  }

  public boolean isBlogPublisher() {
    return isUserInRole(Constants.BLOG_PUBLISHER_ROLE);
  }

  public boolean isBlogContributor() {
    return isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
  }

  private static Collection<GrantedAuthority> createGrantedAuthorities(String roles[]) {
    Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
    if (roles != null) {
      for (String role : roles) {
        authorities.add(new GrantedAuthorityImpl(role.trim()));
      }
    }
    authorities.add(new GrantedAuthorityImpl(Constants.BLOG_READER_ROLE));

    return authorities;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public void setGrantedAuthories(Collection<GrantedAuthority> grantedAuthories) {
    this.grantedAuthories = grantedAuthories;
  }

  public boolean isDetailsUpdateable() {
    return detailsUpdateable;
  }

  public void setDetailsUpdateable(boolean detailsUpdateable) {
    this.detailsUpdateable = detailsUpdateable;
  }

  public Map<String,String> getPreferences() {
    return new HashMap<String,String>(preferences);
  }

  public String getPreference(String key) {
    return preferences.get(key);
  }

  public void setPreferences(Map<String,String> preferences) {
    this.preferences = new HashMap<String,String>(preferences);
  }

  public Collection<String> getOpenIds()
  {
    String openIds = getPreference(OPEN_IDS_PREFERENCE);
    if (openIds == null || openIds.trim().length() == 0) {
      return Collections.emptyList();
    } else {
      // Use a regular expression that will automatically trim whitespace
      return Arrays.asList(openIds.split("\\s*\\" + OPEN_IDS_SEPARATOR + "\\s*"));
    }
  }

  public void setOpenIds(Collection<String> openIds)
  {
    if (openIds.isEmpty()) {
      preferences.remove(OPEN_IDS_PREFERENCE);
    } else {
      StringBuilder builder = new StringBuilder();
      String sep = "";
      for (String openId : openIds) {
        builder.append(sep);
        sep = OPEN_IDS_SEPARATOR;
        builder.append(openId);
      }
      preferences.put(OPEN_IDS_PREFERENCE, builder.toString());
    }
  }
}