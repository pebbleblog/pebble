package net.sourceforge.pebble.security;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import org.acegisecurity.providers.dao.salt.ReflectionSaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.providers.encoding.PlaintextPasswordEncoder;
import org.acegisecurity.GrantedAuthorityImpl;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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
    Map<String,String> preferences = new HashMap<String,String>();
    preferences.put("testPreference", "true");
    PebbleUserDetails pud = new PebbleUserDetails("testuser", "password", "name", "emailAddress", "website", "profile", new String[]{Constants.BLOG_OWNER_ROLE}, preferences, true);
    realm.createUser(pud);
    PebbleUserDetails user = realm.getUser("testuser");

    assertNotNull(user);
    assertEquals("testuser", user.getUsername());
    assertEquals("password{testuser}", user.getPassword());
    assertEquals("name", user.getName());
    assertEquals("emailAddress", user.getEmailAddress());
    assertEquals("website", user.getWebsite());
    assertEquals("profile", user.getProfile());
    assertEquals("true", user.getPreference("testPreference"));

    List authorities = Arrays.asList(user.getAuthorities());
    assertEquals(2, authorities.size());
    assertTrue(authorities.contains(new GrantedAuthorityImpl(Constants.BLOG_OWNER_ROLE)));
    assertTrue(authorities.contains(new GrantedAuthorityImpl(Constants.BLOG_READER_ROLE)));
  }

  public void testGetUserWhenUserDoesntExist() throws Exception {
    PebbleUserDetails user = realm.getUser("someotherusername");
    assertNull(user);
  }

  public void testRemoveUser() throws Exception {
    PebbleUserDetails pud = new PebbleUserDetails("testuser", "password", "name", "emailAddress", "website", "profile", new String[]{Constants.BLOG_OWNER_ROLE}, new HashMap<String,String>(), true);
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
