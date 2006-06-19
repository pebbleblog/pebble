package net.sourceforge.pebble.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;

/**
 * Extension of the Acegi User class that adds additional information
 * such as the user's e-mail address.
 *
 * @author    Simon Brown
 */
public class PebbleUserDetails extends User {

  /** the name */
  private String name;

  /** the e-mail address */
  private String emailAddress;

  /** the website */
  private String website;

  public PebbleUserDetails(String username, String password, String name, String emailAddress, String website, GrantedAuthority[] authorities) {
    super(username, password, true, true, true, true, authorities);
    this.name = name;
    this.emailAddress = emailAddress;
    this.website = website;
  }

  /**
   * Gets the name.
   *
   * @return  a String
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name  a String
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the e-mail address.
   *
   * @return  a String
   */
  public String getEmailAddress() {
    return emailAddress;
  }

  /**
   * Sets the e-mail address.
   *
   * @param emailAddress  a String
   */
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  /**
   * Gets the website.
   *
   * @return  a String
   */
  public String getWebsite() {
    return website;
  }

  /**
   * Sets the name.
   *
   * @param website  a String
   */
  public void setWebsite(String website) {
    this.website = website;
  }

}
