package net.sourceforge.pebble.security;

import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.springframework.dao.DataAccessException;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.Constants;

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

  private static final String REALM_DIRECTORY_NAME = "realm";

  private static final String PASSWORD = "password";
  private static final String ROLES = "roles";
  private static final String NAME = "name";
  private static final String EMAIL_ADDRESS = "emailAddress";
  private static final String WEBSITE = "website";

  private PebbleContext pebbleContext;

  /**
   * Getter the for the pebbleContext property.
   *
   * @return  a PebbleContext instance
   */
  public PebbleContext getPebbleContext() {
    return pebbleContext;
  }

  /**
   * Setter the for the pebbleContext property.
   *
   * @param pebbleContext   a PebbleContext instance
   */
  public void setPebbleContext(PebbleContext pebbleContext) {
    this.pebbleContext = pebbleContext;
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

  public void createUser(String username, String password, String name, String emailAddress, String website, String roles) {
    File user = getFileForUser(username);

    Properties props = new Properties();
    props.setProperty(PASSWORD, password);
    props.setProperty(ROLES, roles);
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
    File realm = new File(pebbleContext.getDataDirectory(), REALM_DIRECTORY_NAME);
    if (!realm.exists()) {
      realm.mkdir();
      createUser("username", "password", "Default User", "", "", Constants.BLOG_OWNER_ROLE + "," + Constants.BLOG_CONTRIBUTOR_ROLE + "," + Constants.PEBBLE_ADMIN_ROLE);
    }

    return realm;
  }

  private File getFileForUser(String username) {
    // find the directory and file corresponding to the user, of the form
    // ${pebbleContext.dataDirectory}/realm/${username}.properties
    return new File(getFileForRealm(), username + ".properties");
  }

}
