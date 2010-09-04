package net.sourceforge.pebble.security;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the UserDetailsService that gets authentication
 * credentials from a SecurityRealm implementation.
 *
 * @author    Simon Brown
 */
public class DefaultUserDetailsService implements UserDetailsService {

  private static final Log log = LogFactory.getLog(DefaultUserDetailsService.class);

  private SecurityRealm securityRealm;

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username    the username to find details for
   * @return  a PebbleUserDetails instance
   * @throws org.acegisecurity.userdetails.UsernameNotFoundException
   */
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      PebbleUserDetails user = securityRealm.getUser(username);
      if (user == null) {
        throw new UsernameNotFoundException("A user with username " + username + " does not exist");
      } else {
        return user;
      }
    } catch (SecurityRealmException e) {
      throw new UsernameNotFoundException("User details not found", e);
    }
  }

  public SecurityRealm getSecurityRealm() {
    return securityRealm;
  }

  public void setSecurityRealm(SecurityRealm securityRealm) {
    this.securityRealm = securityRealm;
  }

}