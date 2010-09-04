/*
 * Copyright (c) 2003-2006, Simon Brown
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
package net.sourceforge.pebble.web.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A factory class from which to look up and retrieve an instance
 * of an Action class to process a specific request.
 *
 * @author    Simon Brown
 */
public class ActionFactory {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(ActionFactory.class);

  /** the collection of actions that we know about */
  private Map actions = new HashMap();

  /** the name of the action mapping file */
  private String actionMappingFileName;

  /**
   * Creates a new instance, using the given configuration file.
   */
  public ActionFactory(String actionMappingFileName) {
    this.actionMappingFileName = actionMappingFileName;
    init();
  }

  /**
   * Initialises this component, reading in and creating the map
   * of action names to action classes.
   */
  private void init() {
    try {
      // load the properties file containing the name -> class name mapping
      InputStream in = getClass().getClassLoader().getResourceAsStream(actionMappingFileName);
      Properties props = new Properties();
      props.load(in);

      // and store them in a HashMap
      Enumeration e = props.propertyNames();
      String actionName;
      String className;

      while (e.hasMoreElements()) {
        actionName = (String)e.nextElement();
        className = props.getProperty(actionName);

        actions.put(actionName, className);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
  }

  /**
   * Given the name/type of request, this method returns the Action
   * instance appropriate to process the request.
   *
   * @param name    the name (type) of the request
   * @return  an instance of Action (could be null if no mapping has been defined)
   */
  public Action getAction(String name) throws ActionNotFoundException {
    try {
      // instantiate the appropriate class to handle the request
      if (actions.containsKey(name)) {
        Class c = getClass().getClassLoader().loadClass((String)actions.get(name));
        Action action = (Action)c.newInstance();

        return action;
      } else {
        throw new ActionNotFoundException("An action called " + name + " could not be found");
      }
    } catch (ClassNotFoundException cnfe) {
      log.error(cnfe.getMessage(), cnfe);
      throw new ActionNotFoundException("An action called " + name + " could not be loaded : " + cnfe.getMessage());
    } catch (IllegalAccessException iae) {
      log.error(iae.getMessage(), iae);
      throw new ActionNotFoundException("An action called " + name + " could not be created : " + iae.getMessage());
    } catch (InstantiationException ie) {
      log.error(ie.getMessage(), ie);
      throw new ActionNotFoundException("An action called " + name + " could not be created : " + ie.getMessage());
    }
  }

}