package net.sourceforge.pebble.security;

import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.providers.dao.SaltSource;
import org.springframework.dao.DataAccessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Implementation of the UserDetailsService that gets authentication
 * credentials from the blog directory.
 *
 * @author    Simon Brown
 */
public class PebbleUserDetailsService implements UserDetailsService {

  private static final Log log = LogFactory.getLog(PebbleUserDetailsService.class);

  private static final String REALM_DIRECTORY_NAME = "realm";

  private static final String PASSWORD = "password";
  private static final String ROLES = "roles";
  private static final String NAME = "name";
  private static final String EMAIL_ADDRESS = "emailAddress";
  private static final String WEBSITE = "website";

  private Configuration configuration;

  private PasswordEncoder passwordEncoder;

  private SaltSource saltSource;

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

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username    the username to find details for
   * @return  a PebbleUserDetails instance
   * @throws org.acegisecurity.userdetails.UsernameNotFoundException
   * @throws org.springframework.dao.DataAccessException
   */
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

    // create the realm, if it doesn't exist
    File realm = getFileForRealm();
    if (!realm.exists()) {
      realm.mkdir();
      log.info("*** Creating default user (username/password)");
      log.info("*** Don't forget to delete this user in a production deployment!");
      createUser("username", "password", "Default User", "username@domain.com", "http://www.domain.com", new String[] {Constants.BLOG_OWNER_ROLE, Constants.BLOG_CONTRIBUTOR_ROLE, Constants.PEBBLE_ADMIN_ROLE});
    }

    File user = getFileForUser(username);
    if (!user.exists()) {
      throw new UsernameNotFoundException("A user with username " + username + " does not exist");
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
      throw new UsernameNotFoundException("A user with username " + username + " does not exist", ioe);
    }
  }

  public void createUser(String username, String password, String name, String emailAddress, String website, String[] roles) {
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
    props.setProperty(PASSWORD, passwordEncoder.encodePassword(password, saltSource.getSalt(pud)));
    props.setProperty(ROLES, rolesAsString);
    props.setProperty(NAME, name);
    props.setProperty(EMAIL_ADDRESS, emailAddress);
    props.setProperty(WEBSITE, website);

    try {
      FileOutputStream out = new FileOutputStream(user);
      props.store(out, "User : " + username);
      out.flush();
      out.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  private File getFileForRealm() {
    // find the directory and file corresponding to the user, of the form
    // ${pebbleContext.dataDirectory}/realm/${username}.properties
    return new File(configuration.getDataDirectory(), REALM_DIRECTORY_NAME);
  }

  private File getFileForUser(String username) {
    // find the directory and file corresponding to the user, of the form
    // ${pebbleContext.dataDirectory}/realm/${username}.properties
    return new File(getFileForRealm(), username + ".properties");
  }

}
