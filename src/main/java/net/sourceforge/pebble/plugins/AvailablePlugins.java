package net.sourceforge.pebble.plugins;

import java.util.*;

/**
 * @author James Roper
 */
public class AvailablePlugins {
  public static final String PERMALINK_PROVIDER = "permalink-provider";
  public static final String CONTENT_DECORATOR = "content-decorator";
  public static final String BLOG_LISTENER = "blog-listener";
  public static final String BLOG_ENTRY_LISTENER = "blog-entry-listener";
  public static final String COMMENT_LISTENER = "comment-listener";
  public static final String COMMENT_CONFIRMATION_STRATEGY = "comment-confirmation-strategy";
  public static final String TRACKBACK_LISTENER = "trackback-listener";
  public static final String TRACKBACK_CONFIRMATION_STRATEGY = "trackback-confirmation-strategy";
  public static final String LUCENCE_ANALYZER = "lucene-analyzer";
  public static final String LOGGER = "logger";

  private final Map<String, List<Plugin>> plugins;

  public Map<String, List<Plugin>> copyMap()
  {
    Map<String, List<Plugin>> map = new HashMap<String, List<Plugin>>();
    for (Map.Entry<String, List<Plugin>> entry : plugins.entrySet())
    {
      map.put(entry.getKey(), new ArrayList<Plugin>(entry.getValue()));
    }
    return map;
  }

  public AvailablePlugins(Map<String, List<Plugin>> plugins) {
    this.plugins = plugins;
  }

  public Collection<Plugin> getPermalinkProviders() {
    return getEmptyIfNull(PERMALINK_PROVIDER);
  }

  public Collection<Plugin> getContentDecorators() {
    return getEmptyIfNull(CONTENT_DECORATOR);
  }

  public Collection<Plugin> getBlogListeners() {
    return getEmptyIfNull(BLOG_LISTENER);
  }

  public Collection<Plugin> getBlogEntryListeners() {
    return getEmptyIfNull(BLOG_ENTRY_LISTENER);
  }

  public Collection<Plugin> getCommentListeners() {
    return getEmptyIfNull(COMMENT_LISTENER);
  }

  public Collection<Plugin> getCommentConfirmationStrategies() {
    return getEmptyIfNull(COMMENT_CONFIRMATION_STRATEGY);
  }

  public Collection<Plugin> getTrackbackListeners() {
    return getEmptyIfNull(TRACKBACK_LISTENER);
  }

  public Collection<Plugin> getTrackbackConfirmationStrategies() {
    return getEmptyIfNull(TRACKBACK_CONFIRMATION_STRATEGY);
  }

  public Collection<Plugin> getLuceneAnalyzers() {
    return getEmptyIfNull(LUCENCE_ANALYZER);
  }

  public Collection<Plugin> getLoggers() {
    return getEmptyIfNull(LOGGER);
  }

  private Collection<Plugin> getEmptyIfNull(String key) {
    Collection<Plugin> list = plugins.get(key);
    if (list == null) {
      return Collections.emptyList();
    }
    else {
      return Collections.unmodifiableCollection(list);
    }
  }

}
