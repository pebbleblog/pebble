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

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.comparator.PebbleUserDetailsComparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import java.io.*;
import java.util.*;

/**
 * Implementation of the SecurityRealm that gets authentication
 * credentials from the blog directory.
 *
 * @author    Simon Brown
 */
public class DefaultSecurityRealm implements SecurityRealm, ApplicationListener {

  private static final Log log = LogFactory.getLog(DefaultSecurityRealm.class);

  private static final String REALM_DIRECTORY_NAME = "realm";

  protected static final String PASSWORD = "password";
  protected static final String ROLES = "roles";
  protected static final String NAME = "name";
  protected static final String EMAIL_ADDRESS = "emailAddress";
  protected static final String WEBSITE = "website";
  protected static final String PROFILE = "profile";
  protected static final String DETAILS_UPDATEABLE = "detailsUpdateable";
  protected static final String PREFERENCE = "preference.";

  private Configuration configuration;

  private PasswordEncoder passwordEncoder;

  private SaltSource saltSource;

  /** Map of open ids to users, cached in a copy on write map */
  private volatile Map<String, String> openIdMap;

  /**
   * Creates the underlying security realm upon creation, if necessary, and initialises the openIdMap.
   *
   * Note, this used to be an init method for the bean, however, due to a circular dependency between this bean and the
   * Pebble configuration bean, it was possible for the Pebble configuration to have been injected and the init method
   * called before the configuration had the home directory set.  This bug exhibited itself when we upgraded to Spring
   * 3.  So, we initialise on framework start.
   */
  public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ContextRefreshedEvent) {
      try {
        File realm = getFileForRealm();
        if (!realm.exists()) {
          realm.mkdirs();
          log.warn("*** Creating default user (username/password)");
          log.warn("*** Don't forget to delete this user in a production deployment!");
          PebbleUserDetails defaultUser = new PebbleUserDetails("username", "password", "Default User", "username@domain.com", "http://www.domain.com", "Default User...", new String[] {Constants.BLOG_OWNER_ROLE, Constants.BLOG_PUBLISHER_ROLE, Constants.BLOG_CONTRIBUTOR_ROLE, Constants.BLOG_ADMIN_ROLE}, new HashMap<String,String>(), true);
          createUser(defaultUser);
        }
      } catch (SecurityRealmException e) {
        log.error("Error while creating security realm", e);
      }

      try {
        // Initialise open id map
        openIdMap = new HashMap<String, String>();
        for (PebbleUserDetails user : getUsers()) {
          for (String openId : user.getOpenIds()) {
            openIdMap.put(openId, user.getUsername());
          }
        }
      } catch (SecurityRealmException e) {
        log.error("Error initialising open ids map", e);
      }
    }
  }

  /**
   * Looks up and returns a collection of all users.
   *
   * @return  a Collection of PebbleUserDetails objects
   */
  public synchronized Collection<PebbleUserDetails> getUsers() throws SecurityRealmException {
    LinkedList<PebbleUserDetails> users = new LinkedList<PebbleUserDetails>();
    File realm = getFileForRealm();
    File files[] = realm.listFiles(new FilenameFilter() {
      /**
       * Tests if a specified file should be included in a file list.
       *
       * @param dir  the directory in which the file was found.
       * @param name the name of the file.
       * @return <code>true</code> if and only if the name should be
       *         included in the file list; <code>false</code> otherwise.
       */
      public boolean accept(File dir, String name) {
        return name.endsWith(".properties");
      }
    });

    for (File file : files) {
      PebbleUserDetails pud = getUser(file.getName().substring(0, file.getName().lastIndexOf(".")));
      if (pud != null) {
        users.add(pud);
      }
    }

    Collections.sort(users, new PebbleUserDetailsComparator());

    return users;
  }

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username the username to find details for
   * @return a PebbleUserDetails instance
   *
   */
  public synchronized PebbleUserDetails getUser(String username) throws SecurityRealmException {
    File user = getFileForUser(username);
    if (!user.exists()) {
      return null;
    }

    try {
      FileInputStream in = new FileInputStream(user);
      Properties props = new Properties();
      props.load(in);
      in.close();

      String password = props.getProperty(PASSWORD);
      String[] roles = props.getProperty(ROLES).split(",");
      String name = props.getProperty(NAME);
      String emailAddress = props.getProperty(EMAIL_ADDRESS);
      String website = props.getProperty(WEBSITE);
      String profile = props.getProperty(PROFILE);
      String detailsUpdateableAsString = props.getProperty(DETAILS_UPDATEABLE);
      boolean detailsUpdateable = true;
      if (detailsUpdateableAsString != null) {
        detailsUpdateable = detailsUpdateableAsString.equalsIgnoreCase("true");
      }

      Map<String,String> preferences = new HashMap<String,String>();
      for (Object key : props.keySet()) {
        String propertyName = (String)key;
        if (propertyName.startsWith(PREFERENCE)) {
          preferences.put(propertyName.substring(PREFERENCE.length()), props.getProperty(propertyName));          
        }
      }

      return new PebbleUserDetails(username, password, name, emailAddress, website, profile, roles, preferences, detailsUpdateable);
    } catch (IOException ioe) {
      throw new SecurityRealmException(ioe);
    }
  }

  public PebbleUserDetails getUserForOpenId(String openId) throws SecurityRealmException {
    String username = openIdMap.get(openId);
    if (username == null) {
      return null;
    } else {
      return getUser(username);
    }
  }

  public synchronized void addOpenIdToUser(PebbleUserDetails pud, String openId) throws SecurityRealmException {
    Collection<String> openIds = new ArrayList<String>(pud.getOpenIds());
    openIds.add(openId);
    pud.setOpenIds(openIds);
    updateUser(pud);
    // Update open id map
    HashMap<String, String> newOpenIdMap = new HashMap<String, String>(openIdMap);
    newOpenIdMap.put(openId, pud.getUsername());
    openIdMap = newOpenIdMap;
  }

  public synchronized void removeOpenIdFromUser(PebbleUserDetails pud, String openId) throws SecurityRealmException {
    // Update open id map
    HashMap<String, String> newOpenIdMap = new HashMap<String, String>(openIdMap);
    newOpenIdMap.remove(openId);
    openIdMap = newOpenIdMap;
    
    Collection<String> openIds = new ArrayList<String>(pud.getOpenIds());
    openIds.remove(openId);
    pud.setOpenIds(openIds);
    updateUser(pud);
  }

  /**
   * Creates a new user.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public synchronized void createUser(PebbleUserDetails pud) throws SecurityRealmException {
    if (getUser(pud.getUsername()) == null) {
      updateUser(pud, true);
    } else {
      throw new SecurityRealmException("User " + pud.getUsername() + " already exists");
    }
  }

  /**
   * Updates user details.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public synchronized void updateUser(PebbleUserDetails pud) throws SecurityRealmException {
    updateUser(pud, false);
  }

  /**
   * Updates user details, except for the password
   *
   * @param pud   a PebbleUserDetails instance
   */
  private void updateUser(PebbleUserDetails pud, boolean updatePassword) throws SecurityRealmException {
    File user = getFileForUser(pud.getUsername());
    PebbleUserDetails currentDetails = getUser(pud.getUsername());

    Properties props = new Properties();
    if (updatePassword) {
      props.setProperty(DefaultSecurityRealm.PASSWORD, passwordEncoder.encodePassword(pud.getPassword(), saltSource.getSalt(pud)));
    } else {
      props.setProperty(DefaultSecurityRealm.PASSWORD, currentDetails.getPassword());
    }
    props.setProperty(DefaultSecurityRealm.ROLES, pud.getRolesAsString());
    props.setProperty(DefaultSecurityRealm.NAME, pud.getName());
    props.setProperty(DefaultSecurityRealm.EMAIL_ADDRESS, pud.getEmailAddress());
    props.setProperty(DefaultSecurityRealm.WEBSITE, pud.getWebsite());
    props.setProperty(DefaultSecurityRealm.PROFILE, pud.getProfile());
    props.setProperty(DefaultSecurityRealm.DETAILS_UPDATEABLE, "" + pud.isDetailsUpdateable());

    Map<String,String> preferences = pud.getPreferences();
    for (String preference : preferences.keySet()) {
      props.setProperty(DefaultSecurityRealm.PREFERENCE + preference, preferences.get(preference));
    }

    try {
      FileOutputStream out = new FileOutputStream(user);
      props.store(out, "User : " + pud.getUsername());
      out.flush();
      out.close();
    } catch (IOException ioe) {
      throw new SecurityRealmException(ioe);
    }
  }

  /**
   * Changes a user's password.
   *
   * @param username    the username of the user
   * @param password    the new password
   * @throws SecurityRealmException
   */
  public synchronized void changePassword(String username, String password) throws SecurityRealmException {
    PebbleUserDetails pud = getUser(username);
    if (pud != null) {
      pud.setPassword(password);
      updateUser(pud, true);
    }
  }

  /**
   * Removes user details for the given username.
   *
   * @param username    the username of the user to remove
   */
  public synchronized void removeUser(String username) throws SecurityRealmException {
    File user = getFileForUser(username);
    if (user.exists()) {
      user.delete();
    }

    if (user.exists()) {
      throw new SecurityRealmException("User " + username + " could not be deleted");
    }
  }

  protected File getFileForRealm() throws SecurityRealmException {
    // find the directory and file corresponding to the user, of the form
    // ${pebbleContext.dataDirectory}/realm/${username}.properties
    return new File(configuration.getDataDirectory(), DefaultSecurityRealm.REALM_DIRECTORY_NAME);
  }

  protected File getFileForUser(String username) throws SecurityRealmException {
    // find the directory and file corresponding to the user, of the form
    // ${pebbleContext.dataDirectory}/realm/${username}.properties
    return new File(getFileForRealm(), username + ".properties");
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  public PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public SaltSource getSaltSource() {
    return saltSource;
  }

  public void setSaltSource(SaltSource saltSource) {
    this.saltSource = saltSource;
  }

}