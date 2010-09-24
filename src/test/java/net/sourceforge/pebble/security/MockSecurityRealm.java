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
  private Map<String,String> openIdMap = new HashMap<String, String>();

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

  public PebbleUserDetails getUserForOpenId(String openId) throws SecurityRealmException {
    return users.get(openIdMap.get(openId));
  }

  public void addOpenIdToUser(PebbleUserDetails pud, String openId) throws SecurityRealmException {
    openIdMap.put(openId, pud.getUsername());
  }

  public void removeOpenIdFromUser(PebbleUserDetails pud, String openId) throws SecurityRealmException {
    openIdMap.remove(openId);
  }

  /**
   * Creates a new user.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public void createUser(PebbleUserDetails pud) throws SecurityRealmException {
    users.put(pud.getUsername(), pud);
  }

  /**
   * Updates user details.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public void updateUser(PebbleUserDetails pud) throws SecurityRealmException {
    users.put(pud.getUsername(), pud);
  }

  /**
   * Changes a user's password.
   *
   * @param username    the username of the user
   * @param password    the new password
   * @throws SecurityRealmException
   */
  public void changePassword(String username, String password) throws SecurityRealmException {
    users.get(username).setPassword(password);
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
