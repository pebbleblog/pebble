package net.sourceforge.pebble.security;

import junit.framework.TestCase;
import net.sourceforge.pebble.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashMap;

/**
 * Tests for the DefaultUserDetails class.
 *
 * @author    Simon Brown
 */
public class DefaultUserDetailsServiceTest extends TestCase {

  private DefaultUserDetailsService service;
  private SecurityRealm securityRealm;

  protected void setUp() {
    service = new DefaultUserDetailsService();
    securityRealm = new MockSecurityRealm();
    service.setSecurityRealm(securityRealm);
  }

  public void testSecurityRealm() {
    assertSame(securityRealm, service.getSecurityRealm());
  }

  public void testLoadByUsername() throws Exception {
    PebbleUserDetails pud = new PebbleUserDetails("username", "password", "name", "emailAddress", "website", "profile", new String[]{Constants.BLOG_OWNER_ROLE}, new HashMap<String,String>(), true);
    securityRealm.createUser(pud);
    UserDetails user = service.loadUserByUsername("username");

    assertNotNull(user);
    assertEquals("username", user.getUsername());
    assertEquals("password", user.getPassword());

    Collection<GrantedAuthority> authorities = user.getAuthorities();
    assertEquals(2, authorities.size());
    assertTrue(authorities.contains(new GrantedAuthorityImpl(Constants.BLOG_OWNER_ROLE)));
    assertTrue(authorities.contains(new GrantedAuthorityImpl(Constants.BLOG_READER_ROLE)));
  }

  public void testLoadByUsernameThrowsExceptionWhenUserDoesntExist() throws Exception {
    try {
      PebbleUserDetails pud = new PebbleUserDetails("username", "password", "name", "emailAddress", "website", "profile", new String[]{Constants.BLOG_OWNER_ROLE}, new HashMap<String,String>(), true);
      securityRealm.createUser(pud);
      UserDetails user = service.loadUserByUsername("someotherusername");
      fail();
    } catch (UsernameNotFoundException e) {
    }
  }

}
