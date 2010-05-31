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