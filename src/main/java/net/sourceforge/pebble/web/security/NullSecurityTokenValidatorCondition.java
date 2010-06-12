package net.sourceforge.pebble.web.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Condition that means there is no condition, all requests should be validated
 * 
 * @author James Roper
 */
public class NullSecurityTokenValidatorCondition implements SecurityTokenValidatorCondition {
  public boolean shouldValidate(HttpServletRequest request) {
    return true;
  }
}
