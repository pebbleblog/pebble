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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A mock SecurityRealm for unit testing.
 *
 * @author    Simon Brown
 */
public class MockSecurityRealm implements SecurityRealm {

  private Map<String,PebbleUserDetails> users = new HashMap<String,PebbleUserDetails>();
  private Map<String,String> openIdMap = new HashMap<String, String>();

  /**
   * Looks up and returns a collection of all users.
   *
   * @return a Collection of PebbleUserDetails objects
   */
  public Collection<PebbleUserDetails> getUsers() {
    return users.values();
  }

  /**
   * Looks up and returns user details for the given username.
   *
   * @param username the username to find details for
   * @return a PebbleUserDetails instance
   */
  public PebbleUserDetails getUser(String username) {
    return users.get(username);
  }

  public PebbleUserDetails getUserForOpenId(String openId) throws SecurityRealmException {
    return users.get(openIdMap.get(openId));
  }

  public void addOpenIdToUser(PebbleUserDetails pud, String openId) throws SecurityRealmException {
    openIdMap.put(openId, pud.getUsername());
  }

  public void removeOpenIdFromUser(PebbleUserDetails pud, String openId) throws SecurityRealmException {
    openIdMap.remove(openId);
  }

  /**
   * Creates a new user.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public void createUser(PebbleUserDetails pud) throws SecurityRealmException {
    users.put(pud.getUsername(), pud);
  }

  /**
   * Updates user details.
   *
   * @param pud   a PebbleUserDetails instance
   */
  public void updateUser(PebbleUserDetails pud) throws SecurityRealmException {
    users.put(pud.getUsername(), pud);
  }

  /**
   * Changes a user's password.
   *
   * @param username    the username of the user
   * @param password    the new password
   * @throws SecurityRealmException
   */
  public void changePassword(String username, String password) throws SecurityRealmException {
    users.get(username).setPassword(password);
  }

  /**
   * Removes user details for the given username.
   *
   * @param username the username of the user to remove
   */
  public void removeUser(String username) {
    users.remove(username);
  }
}
