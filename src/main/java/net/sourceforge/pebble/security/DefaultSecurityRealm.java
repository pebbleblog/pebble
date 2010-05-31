package net.sourceforge.pebble.security;

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.comparator.PebbleUserDetailsComparator;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * Implementation of the SecurityRealm that gets authentication
 * credentials from the blog directory.
 *
 * @author    Simon Brown
 */
public class DefaultSecurityRealm implements SecurityRealm {

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

  /**
   * Creates the underlying security realm upon creation, if necessary.
   */
  public void init() {
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