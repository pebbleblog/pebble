package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;

import java.util.*;

/**
 * Represents a MovableType TrackBack - see
 * http://www.movabletype.org/docs/mttrackback.html for more information.
 *
 * @author    Simon Brown
 */
public class TrackBackTokenManager {

  private static final TrackBackTokenManager instance = new TrackBackTokenManager();
  private static final long TEN_MINUTES = 1000 * 60 * 10;

  private Random random = new Random();
  private Map<String,Date> tokens = new HashMap<String,Date>();

  private TrackBackTokenManager() {
  }

  public static TrackBackTokenManager getInstance() {
    return instance;
  }

  public synchronized String generateToken(BlogEntry blogEntry) {
    String token = "" + random.nextLong();
    tokens.put(token, new Date());
    return token;
  }

  public synchronized boolean isValid(String token) {
    if (token == null || token.length() == 0) {
      return false;
    } else {
      Date date = tokens.get(token);
      return (date != null) && (new Date().getTime() - date.getTime() <= TEN_MINUTES);
    }
  }

  public synchronized void expire(String token) {
    tokens.remove(token);
  }

}
