package net.sourceforge.pebble.security;

import net.sourceforge.pebble.Constants;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;

/**
 * Extension of the Acegi User class that adds additional information
 * such as the user's e-mail address.
 *
 * @author    Simon Brown
 */
public class PebbleUserDetails implements UserDetails {

  private String username;
  private String password;

  /** the name */
  private String name;

  /** the e-mail address */
  private String emailAddress;

  /** the website */
  private String website;

  private GrantedAuthority grantedAuthories[];

  public PebbleUserDetails() {
  }

  public PebbleUserDetails(String username, String name, String emailAddress, String website, String roles[]) {
    this.username = username;
    this.name = name;
    this.emailAddress = emailAddress;
    this.website = website;
    this.grantedAuthories = createGrantedAuthorities(roles);
  }

  public PebbleUserDetails(String username, String password, String name, String emailAddress, String website, String roles[]) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.emailAddress = emailAddress;
    this.website = website;
    this.grantedAuthories = createGrantedAuthorities(roles);
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public boolean isAccountNonExpired() {
    return true;
  }

  public boolean isAccountNonLocked() {
    return true;
  }

  public boolean isCredentialsNonExpired() {
    return true;
  }

  public boolean isEnabled() {
    return true;
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
   * Gets the e-mail address.
   *
   * @return  a String
   */
  public String getEmailAddress() {
    return emailAddress;
  }

  /**
   * Gets the website.
   *
   * @return  a String
   */
  public String getWebsite() {
    return website;
  }

  public GrantedAuthority[] getAuthorities() {
    return this.grantedAuthories;
  }

  public String[] getRoles() {
    GrantedAuthority[] authorities = getAuthorities();
    String[] roles = new String[authorities.length];
    for (int i = 0; i < authorities.length; i++) {
      roles[i] = authorities[i].getAuthority();
    }

    return roles;
  }

  public String getRolesAsString() {
    StringBuffer buf = new StringBuffer();

    GrantedAuthority[] authorities = getAuthorities();
    for (int i = 0; i < authorities.length; i++) {
      buf.append(authorities[i].getAuthority());
      if (i < authorities.length) {
        buf.append(",");
      }
    }

    return buf.toString();
  }

  public boolean isUserInRole(String role) {
    GrantedAuthority[] authorities = getAuthorities();
    if (authorities != null) {
      for (GrantedAuthority authority : authorities) {
        if (authority.getAuthority().equals(role)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isBlogAdmin() {
    return isUserInRole(Constants.BLOG_ADMIN_ROLE);
  }

  public boolean isBlogOwner() {
    return isUserInRole(Constants.BLOG_OWNER_ROLE);
  }

  public boolean isBlogPublisher() {
    return isUserInRole(Constants.BLOG_PUBLISHER_ROLE);
  }

  public boolean isBlogContributor() {
    return isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
  }

  private static GrantedAuthority[] createGrantedAuthorities(String roles[]) {
    if (roles != null) {
      GrantedAuthority[] authorities = new GrantedAuthority[roles.length];
      for (int i = 0; i < roles.length; i++) {
        authorities[i] = new GrantedAuthorityImpl(roles[i].trim());
      }

      return authorities;
    } else {
      return new GrantedAuthority[]{};
    }
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public void setGrantedAuthories(GrantedAuthority[] grantedAuthories) {
    this.grantedAuthories = grantedAuthories;
  }

}
