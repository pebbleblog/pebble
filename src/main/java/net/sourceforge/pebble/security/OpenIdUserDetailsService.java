package net.sourceforge.pebble.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implementation of the UserDetailsService that gets authentication
 * credentials for OpenID identifiers from a SecurityRealm implementation.
 *
 * @author    Simon Brown
 */
public class OpenIdUserDetailsService implements UserDetailsService {

  private static final Log log = LogFactory.getLog(OpenIdUserDetailsService.class);

  private SecurityRealm securityRealm;

  /**
   * Looks up and returns user details for the given OpenID identifier.
   *
   * @param username    the OpenID identifier to find credentials for
   * @return  a PebbleUserDetails instance
   * @throws UsernameNotFoundException if no user can be found that's mapped to the given OpenID credentials
   */
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      PebbleUserDetails user = securityRealm.getUserForOpenId(username);
      if (user == null) {
        throw new UsernameNotFoundException("No OpenId for " + username + " found");
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