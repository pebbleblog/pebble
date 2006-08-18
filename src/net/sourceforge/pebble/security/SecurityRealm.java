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
  public Collection<PebbleUserDetails> getUsers() throws SecurityRealmException;

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username    the username to find details for
   * @return  a PebbleUserDetails instance
   */
  public PebbleUserDetails getUser(String username) throws SecurityRealmException;

  /**
   * Creates a new user.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public void createUser(PebbleUserDetails pud) throws SecurityRealmException;

  /**
   * Updates user details.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public void updateUser(PebbleUserDetails pud) throws SecurityRealmException;

  /**
   * Removes user details for the given username.
   *
   * @param username    the username of the user to remove
   */
  public void removeUser(String username) throws SecurityRealmException;

}