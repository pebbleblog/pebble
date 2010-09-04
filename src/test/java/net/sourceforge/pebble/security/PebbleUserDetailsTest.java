package net.sourceforge.pebble.security;

import junit.framework.TestCase;
import net.sourceforge.pebble.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import java.util.Collection;
import java.util.HashMap;

/**
 * Tests for the PebbleUserDetails class.
 *
 * @author    Simon Brown
 */
public class PebbleUserDetailsTest extends TestCase {

  private PebbleUserDetails user;

  protected void setUp() throws Exception {
    user = new PebbleUserDetails("username", "password", "A user", "emailAddress", "website", "profile", new String[] {Constants.BLOG_OWNER_ROLE}, new HashMap<String,String>(), true);
  }

  public void testConstruction() {
    assertEquals("username", user.getUsername());
    assertEquals("password", user.getPassword());
    assertEquals("A user", user.getName());
    assertEquals("emailAddress", user.getEmailAddress());
    assertEquals("website", user.getWebsite());

    Collection<GrantedAuthority> authorities = user.getAuthorities();
    assertEquals(2, authorities.size());
    assertTrue(authorities.contains(new GrantedAuthorityImpl(Constants.BLOG_OWNER_ROLE)));
    assertTrue(authorities.contains(new GrantedAuthorityImpl(Constants.BLOG_READER_ROLE)));
  }

  public void testConstructionWithNoExplicitRoles() {
    user = new PebbleUserDetails("username", "password", "A user", "emailAddress", "website", "profile", null, new HashMap<String,String>(), true);

    assertEquals("username", user.getUsername());
    assertEquals("password", user.getPassword());
    assertEquals("A user", user.getName());
    assertEquals("emailAddress", user.getEmailAddress());
    assertEquals("website", user.getWebsite());

    Collection<GrantedAuthority> authorities = user.getAuthorities();
    assertEquals(1, authorities.size());
    assertTrue(authorities.contains(new GrantedAuthorityImpl(Constants.BLOG_READER_ROLE)));
  }

}