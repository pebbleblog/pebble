package net.sourceforge.pebble.security;

import java.util.Collection;

/**
 * Represents a security realm with some basic operations.
 *
 * @author    Simon Brown
 */
public interface SecurityRealm {

  /**
   * Looks up and returns a collection of all users.
   *
   * @return  a Collection of PebbleUserDetails objects
   */
  public Collection<PebbleUserDetails> getUsers();

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username    the username to find details for
   * @return  a PebbleUserDetails instance
   */
  public PebbleUserDetails getUser(String username);

  /**
   * Stores a user with the given properties.
   *
   * @param pud   a PebbleUserDetails instance
   * @return a populated PebbleUserDetails instance representing the new user
   */
  public PebbleUserDetails putUser(PebbleUserDetails pud);

  /**
   * Removes user details for the given username.
   *
   * @param username    the username of the user to remove
   */
  public void removeUser(String username);

}