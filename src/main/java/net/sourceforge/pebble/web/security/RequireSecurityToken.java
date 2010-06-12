package net.sourceforge.pebble.web.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this action requires a security token for XSRF prevention
 * @author James Roper
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequireSecurityToken {
  Class<? extends SecurityTokenValidatorCondition> value() default NullSecurityTokenValidatorCondition.class;
}
