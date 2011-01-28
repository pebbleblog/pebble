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

package net.sourceforge.pebble.trackback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Manages tokens for generating TrackBack links.
 *
 * @author    Simon Brown
 */
public class TrackBackTokenManager {

  private static final Log log = LogFactory.getLog(TrackBackTokenManager.class);

  private static final TrackBackTokenManager instance = new TrackBackTokenManager();

  /** the time to live for new tokens */
  private static final long TIME_TO_LIVE = 1000 * 60 * 10; // 10 minutes

  private Random random = new Random();

  /** the map of tokens */
  private Map<String,Date> tokens = new HashMap<String,Date>();

  /**
   * Private constructor for the singleton pattern.
   */
  private TrackBackTokenManager() {
    // create a new TimerTask that will purge invalid tokens
    TimerTask task = new TimerTask() {
      public void run() {
        synchronized (TrackBackTokenManager.this) {
          log.debug("Purging expired tokens");
          Iterator it = tokens.keySet().iterator();
          while (it.hasNext()) {
            String token = (String)it.next();
            if (!isValid(token)) {
              it.remove();
            }
          }
        }
      }
    };

    Timer timer = new Timer();
    timer.schedule(task, 2 * TIME_TO_LIVE);
  }

  /**
   * Gets the singleton instance of this class.
   *
   * @return    a TrackBackTokenManager instance
   */
  public static TrackBackTokenManager getInstance() {
    return instance;
  }

  /**
   * Generates a new token with a fixed time to live.
   *
   * @return  a new token
   */
  public synchronized String generateToken() {
    String token = "" + random.nextLong();
    tokens.put(token, new Date());
    return token;
  }

  /**
   * Determines whether a given token is valid.
   *
   * @param token   the token to test
   * @return  true if the token is valid and hasn't expired, false otherwise
   */
  public synchronized boolean isValid(String token) {
    if (token == null || token.length() == 0) {
      return false;
    } else {
      Date date = tokens.get(token);
      return (date != null) && (new Date().getTime() - date.getTime() <= TIME_TO_LIVE);
    }
  }

  /**
   * Expires a given token.
   *
   * @param token   the token to be expired
   */
  public synchronized void expire(String token) {
    tokens.remove(token);
  }

}