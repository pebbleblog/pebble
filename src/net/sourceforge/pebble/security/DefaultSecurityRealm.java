package net.sourceforge.pebble.security;

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.Constants;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
   * Looks up and returns user details for the given username.
   *
   * @param username the username to find details for
   * @return a PebbleUserDetails instance
   *
   */
  public PebbleUserDetails getUser(String username) {
    // create the realm, if it doesn't exist
    File realm = getFileForRealm();
    if (!realm.exists()) {
      realm.mkdir();
      log.info("*** Creating default user (username/password)");
      log.info("*** Don't forget to delete this user in a production deployment!");
      putUser("username", "password", "Default User", "username@domain.com", "http://www.domain.com", new String[] {Constants.BLOG_OWNER_ROLE, Constants.BLOG_PUBLISHER_ROLE, Constants.BLOG_CONTRIBUTOR_ROLE, Constants.PEBBLE_ADMIN_ROLE});
    }

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
      GrantedAuthority[] authorities = new GrantedAuthority[roles.length];
      for (int i = 0; i < roles.length; i++) {
        authorities[i] = new GrantedAuthorityImpl(roles[i].trim());
      }

      return new PebbleUserDetails(username, password, name, emailAddress, website, authorities);
    } catch (IOException ioe) {
      return null;
    }
  }

  /**
   * Stores a user with the given properties.
   *
   * @param username     the username
   * @param password     the password
   * @param name         the user's name
   * @param emailAddress the user's e-mail address
   * @param website      the user's website
   * @param roles        an array containing the user's roles
   * @return a populated PebbleUserDetails instance representing the new user
   */
  public PebbleUserDetails putUser(String username, String password, String name, String emailAddress, String website, String[] roles) {
    File user = getFileForUser(username);

    String rolesAsString = "";
    GrantedAuthority[] authorities = new GrantedAuthority[roles.length];
    for (int i = 0; i < roles.length; i++) {
      rolesAsString += roles[i];
      if (i < (roles.length-1)) {
        rolesAsString += ",";
      }
      authorities[i] = new GrantedAuthorityImpl(roles[i]);
    }

    PebbleUserDetails pud = new PebbleUserDetails(username, password, name, emailAddress, website, authorities);

    Properties props = new Properties();
    props.setProperty(DefaultSecurityRealm.PASSWORD, passwordEncoder.encodePassword(password, saltSource.getSalt(pud)));
    props.setProperty(DefaultSecurityRealm.ROLES, rolesAsString);
    props.setProperty(DefaultSecurityRealm.NAME, name);
    props.setProperty(DefaultSecurityRealm.EMAIL_ADDRESS, emailAddress);
    props.setProperty(DefaultSecurityRealm.WEBSITE, website);

    try {
      FileOutputStream out = new FileOutputStream(user);
      props.store(out, "User : " + username);
      out.flush();
      out.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    return getUser(username);
  }

  /**
   * Removes user details for the given username.
   *
   * @param username    the username of the user to remove
   */
  public void removeUser(String username) {
    File user = getFileForUser(username);
    if (user.exists()) {
      user.delete();
    }
  }

  protected File getFileForRealm() {
    // find the directory and file corresponding to the user, of the form
    // ${pebbleContext.dataDirectory}/realm/${username}.properties
    return new File(configuration.getDataDirectory(), DefaultSecurityRealm.REALM_DIRECTORY_NAME);
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