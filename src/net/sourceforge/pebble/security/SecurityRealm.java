package net.sourceforge.pebble.security;

/**
 * Represents a security realm with some basic operations.
 *
 * @author    Simon Brown
 */
public interface SecurityRealm {

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
   * @param username      the username
   * @param password      the password
   * @param name          the user's name
   * @param emailAddress  the user's e-mail address
   * @param website       the user's website
   * @param roles         an array containing the user's roles
   * @return  a populated PebbleUserDetails instance representing the new user
   */
  public PebbleUserDetails putUser(String username, String password, String name, String emailAddress, String website, String[] roles);

  /**
   * Removes user details for the given username.
   *
   * @param username    the username of the user to remove
   */
  public void removeUser(String username);

}