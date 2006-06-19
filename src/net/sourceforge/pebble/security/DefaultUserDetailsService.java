package net.sourceforge.pebble.security;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;

/**
 * Implementation of the UserDetailsService that gets authentication
 * credentials from the blog directory.
 *
 * @author    Simon Brown
 */
public class DefaultUserDetailsService implements UserDetailsService {

  private SecurityRealm securityRealm;

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username    the username to find details for
   * @return  a PebbleUserDetails instance
   * @throws org.acegisecurity.userdetails.UsernameNotFoundException
   */
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    PebbleUserDetails user = securityRealm.getUser(username);
    if (user == null) {
      throw new UsernameNotFoundException("A user with username " + username + " does not exist");
    } else {
      return user;
    }
  }

  public SecurityRealm getSecurityRealm() {
    return securityRealm;
  }

  public void setSecurityRealm(SecurityRealm securityRealm) {
    this.securityRealm = securityRealm;
  }

}
