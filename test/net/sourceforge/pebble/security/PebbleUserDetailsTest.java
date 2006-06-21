package net.sourceforge.pebble.security;

import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.Constants;

import java.util.Date;
import java.util.Calendar;

import junit.framework.TestCase;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.GrantedAuthority;

/**
 * Tests for the PebbleUserDetails class.
 *
 * @author    Simon Brown
 */
public class PebbleUserDetailsTest extends TestCase {

  private PebbleUserDetails user;

  protected void setUp() throws Exception {
    user = new PebbleUserDetails("username", "password", "A user", "emailAddress", "website",
        new GrantedAuthority[]{new GrantedAuthorityImpl(Constants.BLOG_OWNER_ROLE)});
  }

  public void testConstruction() {
    assertEquals("username", user.getUsername());
    assertEquals("password", user.getPassword());
    assertEquals("A user", user.getName());
    assertEquals("emailAddress", user.getEmailAddress());
    assertEquals("website", user.getWebsite());

    GrantedAuthority authorities[] = user.getAuthorities();
    assertEquals(1, authorities.length);
    assertEquals(Constants.BLOG_OWNER_ROLE, authorities[0].getAuthority());
  }

}