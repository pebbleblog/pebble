package net.sourceforge.pebble.security;

import java.util.Collection;
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
   * Looks up and returns a collection of all users.
   *
   * @return a Collection of PebbleUserDetails objects
   */
  public Collection<PebbleUserDetails> getUsers() {
    return users.values();
  }

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
   * @param pud   a PebbleUserDetails instance
   * @return a populated PebbleUserDetails instance representing the new user
   */
  public synchronized PebbleUserDetails putUser(PebbleUserDetails pud) {
    users.put(pud.getUsername(), pud);

    return pud;
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
