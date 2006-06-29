package net.sourceforge.pebble.security;

import junit.framework.TestCase;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.util.FileUtils;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.encoding.PlaintextPasswordEncoder;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.providers.dao.salt.ReflectionSaltSource;
import org.acegisecurity.providers.dao.SaltSource;

import java.io.File;

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
    realm.getUser("username");
  }

  public void testConfigured() {
    assertSame(passwordEncoder, realm.getPasswordEncoder());
    assertSame(saltSource, realm.getSaltSource());
  }

  public void testSecurityRealmCreatedIfItDoesntExist() {
    FileUtils.deleteFile(new File(realm.getConfiguration().getDataDirectory(), "realm"));
    realm.getUser("username");
    assertNotNull(realm.getUser("username"));
  }

  public void testGetUser() throws Exception {
    realm.putUser("username", "password", "name", "emailAddress", "website", new String[]{Constants.BLOG_OWNER_ROLE});
    PebbleUserDetails user = realm.getUser("username");

    assertNotNull(user);
    assertEquals("username", user.getUsername());
    assertEquals("password{username}", user.getPassword());
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

  public void testRemoveUser() {
    realm.putUser("username", "password", "name", "emailAddress", "website", new String[]{Constants.BLOG_OWNER_ROLE});
    PebbleUserDetails user = realm.getUser("username");
    assertNotNull(user);

    realm.removeUser("username");
    user = realm.getUser("username");
    assertNull(user);
  }

  public void testRemoveUserThatDoesntExists() {
    PebbleUserDetails user = realm.getUser("someotherusername");
    assertNull(user);

    realm.removeUser("someotherusername");
    user = realm.getUser("someotherusername");
    assertNull(user);
  }

}
