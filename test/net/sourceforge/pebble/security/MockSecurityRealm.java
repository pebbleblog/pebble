package net.sourceforge.pebble.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * A mock SecurityRealm for unit testing.
 *
 * @author    Simon Brown
 */
public class MockSecurityRealm implements SecurityRealm {

  private Map<String,PebbleUserDetails> users = new HashMap<String,PebbleUserDetails>();

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username the username to find details for
   * @return a PebbleUserDetails instance
   */
  public PebbleUserDetails getUser(String username) {
    return users.get(username);
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
    GrantedAuthority[] authorities = new GrantedAuthority[roles.length];
    for (int i = 0; i < roles.length; i++) {
      authorities[i] = new GrantedAuthorityImpl(roles[i]);
    }
    PebbleUserDetails user = new PebbleUserDetails(username, password, name, emailAddress, website, authorities);
    users.put(username, user);

    return user;
  }

  /**
   * Removes user details for the given username.
   *
   * @param username the username of the user to remove
   */
  public void removeUser(String username) {
    users.remove(username);
  }
}
