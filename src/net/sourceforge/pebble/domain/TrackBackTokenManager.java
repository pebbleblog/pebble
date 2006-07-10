package net.sourceforge.pebble.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Represents a MovableType TrackBack - see
 * http://www.movabletype.org/docs/mttrackback.html for more information.
 *
 * @author    Simon Brown
 */
public class TrackBackTokenManager {

  private static final Log log = LogFactory.getLog(TrackBackTokenManager.class);

  private static final TrackBackTokenManager instance = new TrackBackTokenManager();
  private static final long TIME_TO_LIVE = 1000 * 60 * 10; // 10 minutes

  private Random random = new Random();
  private Map<String,Date> tokens = new HashMap<String,Date>();

  private TrackBackTokenManager() {
    TimerTask task = new TimerTask() {
      public void run() {
        synchronized (TrackBackTokenManager.this) {
          for (String token : tokens.keySet()) {
            if (!isValid(token)) {
              expire(token);
            }
          }
        }
      }
    };

    Timer timer = new Timer();
    timer.schedule(task, 2 * TIME_TO_LIVE);
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
      return (date != null) && (new Date().getTime() - date.getTime() <= TIME_TO_LIVE);
    }
  }

  public synchronized void expire(String token) {
    tokens.remove(token);
  }

}
