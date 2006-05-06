package net.sourceforge.pebble.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;

/**
 * Extension of the standard User class that adds additional information
 * such as the user's e-mail address.
 *
 * @author    Simon Brown
 */
public class PebbleUserDetails extends User {

  /** the e-mail address */
  private String emailAddress;

  public PebbleUserDetails(String username, String password, String emailAddress, GrantedAuthority[] authorities) {
    super(username, password, true, true, true, true, authorities);
    this.emailAddress = emailAddress;
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


}
