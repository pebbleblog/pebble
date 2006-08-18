package net.sourceforge.pebble.security;

import junit.framework.TestCase;
import net.sourceforge.pebble.Constants;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;

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
    PebbleUserDetails pud = new PebbleUserDetails("username", "password", "name", "emailAddress", "website", new String[]{Constants.BLOG_OWNER_ROLE});
    securityRealm.createUser(pud);
    UserDetails user = service.loadUserByUsername("username");

    assertNotNull(user);
    assertEquals("username", user.getUsername());
    assertEquals("password", user.getPassword());

    GrantedAuthority authorities[] = user.getAuthorities();
    assertEquals(1, authorities.length);
    assertEquals(Constants.BLOG_OWNER_ROLE, authorities[0].getAuthority());
  }

  public void testLoadByUsernameThrowsExceptionWhenUserDoesntExist() throws Exception {
    try {
      PebbleUserDetails pud = new PebbleUserDetails("username", "password", "name", "emailAddress", "website", new String[]{Constants.BLOG_OWNER_ROLE});
      securityRealm.createUser(pud);
      UserDetails user = service.loadUserByUsername("someotherusername");
      fail();
    } catch (UsernameNotFoundException e) {
    }
  }

}
