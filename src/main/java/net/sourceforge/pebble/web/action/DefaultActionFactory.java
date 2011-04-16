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
package net.sourceforge.pebble.web.action;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
public class DefaultActionFactory implements ActionFactory, ApplicationContextAware {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(DefaultActionFactory.class);

  /** the collection of actions that we know about */
  private final Map<String, String> actions = new HashMap<String, String>();

  /** the name of the action mapping file */
  private String actionMappingFileName;

  /** the ApplicationContext to instantiate actions with */
  private AutowireCapableBeanFactory beanFactory;

  /**
   * Initialises this component, reading in and creating the map
   * of action names to action classes.
   *
   * @throws IOException If an error occurs looking up all the actions
   */
  @PostConstruct
  public void init() throws IOException {
    // Load all resources by the given name, allows for plugins to provide their own actions
    for (Enumeration<URL> e = getClass().getClassLoader().getResources(actionMappingFileName); e.hasMoreElements();) {
      URL url = e.nextElement();
      // load the properties file containing the name -> class name mapping
      InputStream in = null;
      try {
        in = url.openStream();
        Properties props = new Properties();
        props.load(in);
        actions.putAll((Map) props);
      } catch (IOException ioe) {
        log.error("Error reading actions for class: " + url, ioe);
      } finally {
        IOUtils.closeQuietly(in);
      }
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
        Class<?> c = getClass().getClassLoader().loadClass((String)actions.get(name));
        Class<? extends Action> actionClass = c.asSubclass(Action.class);
        return (Action) beanFactory.createBean(actionClass, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
      } else {
        throw new ActionNotFoundException("An action called " + name + " could not be found");
      }
    } catch (ClassNotFoundException cnfe) {
      log.error(cnfe.getMessage(), cnfe);
      throw new ActionNotFoundException("An action called " + name + " could not be loaded", cnfe);
    } catch (BeansException be) {
      log.error(be.getMessage(), be);
      throw new ActionNotFoundException("An action called " + name + " could not be instantiated", be);
    }
  }

  public void setActionMappingFileName(String actionMappingFileName) {
    this.actionMappingFileName = actionMappingFileName;
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
  }
}