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
   * Looks up and returns user details for the given username.
   *
   * @param openId    the openId to find details for
   * @return  a PebbleUserDetails instance
   */
  public PebbleUserDetails getUserForOpenId(String openId) throws SecurityRealmException;

  /**
   * Adds an OpenID to a user
   *
   * @param pud The user details of the user
   * @param openId The openId to add to them
   * @throws SecurityRealmException If an error occurred
   */
  public void addOpenIdToUser(PebbleUserDetails pud, String openId) throws SecurityRealmException;

  /**
   * Removes an OpenID from a user
   *
   * @param pud The user details of the user
   * @param openId The openId to remove from them
   * @throws SecurityRealmException If an error occurred
   */
  public void removeOpenIdFromUser(PebbleUserDetails pud, String openId) throws SecurityRealmException;

  /**
   * Creates a new user.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public void createUser(PebbleUserDetails pud) throws SecurityRealmException;

  /**
   * Updates user details, except for the password
   *
   * @param pud   a PebbleUserDetails instance
   */
  public void updateUser(PebbleUserDetails pud) throws SecurityRealmException;

  /**
   * Changes a user's password.
   *
   * @param username    the username of the user
   * @param password    the new password
   * @throws SecurityRealmException
   */
  public void changePassword(String username, String password) throws SecurityRealmException;

  /**
   * Removes user details for the given username.
   *
   * @param username    the username of the user to remove
   */
  public void removeUser(String username) throws SecurityRealmException;

}