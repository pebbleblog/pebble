/*
 * Copyright (c) 2003-2011, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.sourceforge.pebble.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implementation of the UserDetailsService that gets authentication
 * credentials for OpenID identifiers from a SecurityRealm implementation.
 *
 * @author    Simon Brown
 */
public class OpenIdUserDetailsService implements UserDetailsService {

  private static final Log log = LogFactory.getLog(OpenIdUserDetailsService.class);

  private SecurityRealm securityRealm;

  /**
   * Looks up and returns user details for the given OpenID identifier.
   *
   * @param username    the OpenID identifier to find credentials for
   * @return  a PebbleUserDetails instance
   * @throws UsernameNotFoundException if no user can be found that's mapped to the given OpenID credentials
   */
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      PebbleUserDetails user = securityRealm.getUserForOpenId(username);
      if (user == null) {
        throw new UsernameNotFoundException("No OpenId for " + username + " found");
      } else {
        return user;
      }
    } catch (SecurityRealmException e) {
      throw new UsernameNotFoundException("User details not found", e);
    }
  }

  public SecurityRealm getSecurityRealm() {
    return securityRealm;
  }

  public void setSecurityRealm(SecurityRealm securityRealm) {
    this.securityRealm = securityRealm;
  }

}