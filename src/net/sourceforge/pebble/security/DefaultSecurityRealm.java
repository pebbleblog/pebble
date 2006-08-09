package net.sourceforge.pebble.security;

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.Constants;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Implementation of the UserDetailsService that gets authentication
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

  private Configuration configuration;

  private PasswordEncoder passwordEncoder;

  private SaltSource saltSource;

  /**
   * Looks up and returns a collection of all users.
   *
   * @return  a Collection of PebbleUserDetails objects
   */
  public synchronized Collection<PebbleUserDetails> getUsers() {
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

    return users;
  }

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username the username to find details for
   * @return a PebbleUserDetails instance
   *
   */
  public synchronized PebbleUserDetails getUser(String username) {
    // create the realm, if it doesn't exist
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

      return new PebbleUserDetails(username, password, name, emailAddress, website, roles);
    } catch (IOException ioe) {
      return null;
    }
  }

  /**
   * Stores a user with the given properties.
   *
   * @param pud   a PebbleUserDetails instance
   * @return a populated PebbleUserDetails instance representing the new user
   */
  public synchronized PebbleUserDetails putUser(PebbleUserDetails pud) {
    File user = getFileForUser(pud.getUsername());

    Properties props = new Properties();
    props.setProperty(DefaultSecurityRealm.PASSWORD, passwordEncoder.encodePassword(pud.getPassword(), saltSource.getSalt(pud)));
    props.setProperty(DefaultSecurityRealm.ROLES, pud.getRolesAsString());
    props.setProperty(DefaultSecurityRealm.NAME, pud.getName());
    props.setProperty(DefaultSecurityRealm.EMAIL_ADDRESS, pud.getEmailAddress());
    props.setProperty(DefaultSecurityRealm.WEBSITE, pud.getWebsite());

    try {
      FileOutputStream out = new FileOutputStream(user);
      props.store(out, "User : " + pud.getUsername());
      out.flush();
      out.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return getUser(pud.getUsername());
  }

  /**
   * Removes user details for the given username.
   *
   * @param username    the username of the user to remove
   */
  public synchronized void removeUser(String username) {
    File user = getFileForUser(username);
    if (user.exists()) {
      user.delete();
    }
  }

  protected File getFileForRealm() {
    // find the directory and file corresponding to the user, of the form
    // ${pebbleContext.dataDirectory}/realm/${username}.properties
    File realm = new File(configuration.getDataDirectory(), DefaultSecurityRealm.REALM_DIRECTORY_NAME);

    if (!realm.exists()) {
      realm.mkdir();
      log.warn("*** Creating default user (username/password)");
      log.warn("*** Don't forget to delete this user in a production deployment!");
      PebbleUserDetails defaultUser = new PebbleUserDetails("username", "password", "Default User", "username@domain.com", "http://www.domain.com", new String[] {Constants.BLOG_OWNER_ROLE, Constants.BLOG_PUBLISHER_ROLE, Constants.BLOG_CONTRIBUTOR_ROLE, Constants.BLOG_ADMIN_ROLE});
      putUser(defaultUser);
    }

    return realm;
  }

  protected File getFileForUser(String username) {
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