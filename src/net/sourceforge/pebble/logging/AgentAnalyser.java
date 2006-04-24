package net.sourceforge.pebble.logging;

import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.file.FileDAOFactory;
import net.sourceforge.pebble.domain.Blog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ...
 *
 * @author Simon Brown
 */
public class AgentAnalyser {

  public static void main(String[] args) {
    DAOFactory.setConfiguredFactory(new FileDAOFactory());
    Blog blog = new Blog(args[0]);
    Log log = null;
    if (args.length == 3) {
      log = blog.getLogger().getLog(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    } else if (args.length == 4) {
      log = blog.getLogger().getLog(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
    }
    Map agents = new HashMap();
    Iterator it = log.getLogEntries().iterator();
    while (it.hasNext()) {
      LogEntry entry = (LogEntry)it.next();
      String agent = entry.getAgent();
      if (agent == null) {
        agent = "Not specified";
      } else if (agent.matches(".*Bloglines.*")) {
        agent = "Bloglines";
      } else if (agent.matches(".*Firefox.*")) {
        agent = "Firefox";
      } else if (agent.matches(".*Safari.*")) {
        agent = "Safari";
      } else if (agent.matches(".*Omea.*")) {
        agent = "Omea Reader";
      } else if (agent.matches(".*SharpReader.*")) {
        agent = "SharpReader";
      } else if (agent.matches(".*NetNewsWire.*")) {
        agent = "NetNewsWire";
      } else if (agent.matches(".*msnbot.*")) {
        agent = "msnbot";
      } else if (agent.matches(".*Googlebot.*")) {
        agent = "Googlebot";
      } else if (agent.matches(".*TrillianPRO.*")) {
        agent = "TrillianPRO";
      } else if (agent.matches(".*FeedDemon.*")) {
        agent = "FeedDemon";
      } else if (agent.matches(".*Drupal.*")) {
        agent = "Drupal";
      } else if (agent.matches(".*NewsGatorOnline.*")) {
        agent = "NewsGatorOnline";
      } else if (agent.matches(".*RssReader.*")) {
        agent = "RssReader";
      } else if (agent.matches(".*Thunderbird.*")) {
        agent = "Thunderbird";
      } else if (agent.matches(".*Newsfire.*")) {
        agent = "Newsfire";
      } else if (agent.matches(".*Artima NewsMan.*")) {
        agent = "Artima NewsMan";
      } else if (agent.matches(".*NewzCrawler.*")) {
        agent = "NewzCrawler";
      } else if (agent.matches(".*RssBandit.*")) {
        agent = "RssBandit";
      } else if (agent.matches(".*Yahoo! Slurp.*")) {
        agent = "Yahoo! Slurp";
      } else if (agent.matches(".*Jakarta Commons-HttpClient.*")) {
        agent = "Jakarta Commons-HttpClient";
      } else if (agent.matches(".*NewsGator.*")) {
        agent = "NewsGator";
      } else if (agent.matches(".*Netscape.*")) {
        agent = "Netscape";
      } else if (agent.matches(".*Gecko.*")) {
        agent = "Gecko";
      } else if (agent.matches(".*Konqueror.*")) {
        agent = "Konqueror";
      } else if (agent.matches(".*Firebird.*")) {
        agent = "Firebird";
      } else if (agent.matches(".*Opera.*")) {
        agent = "Opera";
      } else if (agent.matches(".*BecomeBot.*")) {
        agent = "BecomeBot";
      } else if (agent.matches(".*liferea.*")) {
        agent = "liferea";
      } else if (agent.matches(".*MSIE.*")) {
        agent = "MSIE";
      }

      Integer count = (Integer)agents.get(agent);
      if (count == null) {
        count = new Integer(0);
      }

      agents.put(agent, new Integer(count.intValue()+1));
    }

    it = agents.keySet().iterator();
    while (it.hasNext()) {
      Object key = it.next();
      System.out.println(key + "\t" + agents.get(key));
    }
  }

}
