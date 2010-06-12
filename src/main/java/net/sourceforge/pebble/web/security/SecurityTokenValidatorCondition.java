package net.sourceforge.pebble.web.security;

import javax.servlet.http.HttpServletRequest;

/**
 * A condition for when a security token should be validated
 *
 * @author James Roper
 */
public interface SecurityTokenValidatorCondition {

  boolean shouldValidate(HttpServletRequest request);
}
