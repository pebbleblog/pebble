package net.sourceforge.pebble.security;

import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.springframework.dao.DataAccessException;
import net.sourceforge.pebble.PebbleContext;

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

  private static final String PASSWORD = "password";
  private static final String ROLES = "roles";
  private static final String EMAIL_ADDRESS = "emailAddress";

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

      String password = props.getProperty(PebbleUserDetailsService.PASSWORD);
      String emailAddress = props.getProperty(PebbleUserDetailsService.EMAIL_ADDRESS);
      String[] roles = props.getProperty(PebbleUserDetailsService.ROLES).split(",");
      GrantedAuthority[] authorities = new GrantedAuthority[roles.length];
      for (int i = 0; i < roles.length; i++) {
        authorities[i] = new GrantedAuthorityImpl(roles[i].trim());
      }

      return new PebbleUserDetails(username, password, emailAddress, authorities);
    } catch (IOException ioe) {
      throw new UsernameNotFoundException("A user with username " + username + " does not exist", ioe);
    }
  }

  public void createRealm() {
    File realm = getFileForRealm();
    realm.mkdir();
  }

  public void createUser(String username, String password, String emailAddress, String roles) {
    File user = getFileForUser(username);

    Properties props = new Properties();
    props.setProperty(PASSWORD, password);
    props.setProperty(EMAIL_ADDRESS, emailAddress);
    props.setProperty(ROLES, roles);

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
    // ${pebble.data.directory}/authentication/${username}.properties
    return new File(pebbleContext.getDataDirectory(), "authentication");
  }

  private File getFileForUser(String username) {
    // find the directory and file corresponding to the user, of the form
    // ${pebble.data.directory}/authentication/${username}.properties
    return new File(getFileForRealm(), username + ".properties");
  }

}
