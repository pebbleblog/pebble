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
public class OSAnalyser {

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
      } else if (agent.matches(".*Linux.*")) {
        agent = "Linux";
      } else if (agent.matches(".*WinNT.*")) {
        agent = "Windows";
      } else if (agent.matches(".*Windows NT.*")) {
        agent = "Windows";
      } else if (agent.matches(".*Windows.*")) {
        agent = "Windows";
      } else if (agent.matches(".*Mac OS X.*")) {
        agent = "Mac OS X";
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
