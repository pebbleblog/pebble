package net.sourceforge.pebble.security;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.dao.salt.ReflectionSaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.providers.encoding.PlaintextPasswordEncoder;

/**
 * Tests for the DefaultSecurityRealm class.
 *
 * @author    Simon Brown
 */
public class DefaultSecurityRealmTest extends SingleBlogTestCase {

  private DefaultSecurityRealm realm;
  private PasswordEncoder passwordEncoder;
  private ReflectionSaltSource saltSource;

  protected void setUp() throws Exception {
    super.setUp();

    realm = new DefaultSecurityRealm();
    realm.setConfiguration(PebbleContext.getInstance().getConfiguration());

    passwordEncoder = new PlaintextPasswordEncoder();
    realm.setPasswordEncoder(passwordEncoder);
    saltSource = new ReflectionSaltSource();
    saltSource.setUserPropertyToUse("getUsername");
    realm.setSaltSource(saltSource);

    realm.init();
  }

  protected void tearDown() throws Exception {
    super.tearDown();

    realm.removeUser("username");
  }

  public void testConfigured() {
    assertSame(passwordEncoder, realm.getPasswordEncoder());
    assertSame(saltSource, realm.getSaltSource());
  }

  public void testGetUser() throws Exception {
    PebbleUserDetails pud = new PebbleUserDetails("testuser", "password", "name", "emailAddress", "website", new String[]{Constants.BLOG_OWNER_ROLE});
    realm.createUser(pud);
    PebbleUserDetails user = realm.getUser("testuser");

    assertNotNull(user);
    assertEquals("testuser", user.getUsername());
    assertEquals("password{testuser}", user.getPassword());
    assertEquals("name", user.getName());
    assertEquals("emailAddress", user.getEmailAddress());
    assertEquals("website", user.getWebsite());

    GrantedAuthority authorities[] = user.getAuthorities();
    assertEquals(1, authorities.length);
    assertEquals(Constants.BLOG_OWNER_ROLE, authorities[0].getAuthority());
  }

  public void testGetUserWhenUserDoesntExist() throws Exception {
    PebbleUserDetails user = realm.getUser("someotherusername");
    assertNull(user);
  }

  public void testRemoveUser() throws Exception {
    PebbleUserDetails pud = new PebbleUserDetails("testuser", "password", "name", "emailAddress", "website", new String[]{Constants.BLOG_OWNER_ROLE});
    realm.createUser(pud);

    PebbleUserDetails user = realm.getUser("testuser");
    assertNotNull(user);

    realm.removeUser("testuser");
    user = realm.getUser("testuser");
    assertNull(user);
  }

  public void testRemoveUserThatDoesntExists() throws Exception {
    PebbleUserDetails user = realm.getUser("someotherusername");
    assertNull(user);

    realm.removeUser("someotherusername");
    user = realm.getUser("someotherusername");
    assertNull(user);
  }

}
