package net.sourceforge.pebble.web.security;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author James Roper
 */
public class SecurityTokenValidatorTest extends TestCase {

  private SecurityTokenValidator validator;

  protected void setUp() {
    validator = new SecurityTokenValidator();
  }

  public void testGenerateHash() {
    Map<String, String[]> map1 = new HashMap<String, String[]>();
    map1.put("key", new String[] {"value"});
    map1.put("zed", new String[] {"value2"});
    String hash1 = validator.hashRequest("path", map1, "salt");
    Map<String, String[]> map2 = new HashMap<String, String[]>();
    map2.put("zed", new String[] {"value2"});
    map2.put("key", new String[] {"value"});
    String hash2 = validator.hashRequest("path", map2, "salt");
    assertEquals(hash1, hash2);
    String hash3 = validator.hashRequest("path", map1, "badsalt");
    assertFalse(hash1.equals(hash3));
  }

}
