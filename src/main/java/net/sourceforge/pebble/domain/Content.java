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
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.api.event.PebbleEvent;
import net.sourceforge.pebble.util.StringUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Superclass for blog entries, comments and TrackBacks.
 *
 * @author    Simon Brown
 */
public abstract class Content implements Permalinkable, Cloneable, Serializable {

  /** the state of the object */
  private State state;

  /** flag to indicate whether events are enabled */
  private boolean eventsEnabled = false;

  /** the class responsible for managing property change events */
  protected transient PropertyChangeSupport propertyChangeSupport;

  /** the collection of properties that have changed since the last store */
  private transient ArrayList propertyChangeEvents;

  /** the collection of PebbleEvent instances that have been initiated */
  private transient List<PebbleEvent> events = new ArrayList<PebbleEvent>();

  /**
   * Default, no args constructor.
   */
  public Content() {
    this.propertyChangeSupport = new PropertyChangeSupport(this);
    this.propertyChangeEvents = new ArrayList();
    this.propertyChangeSupport.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent event) {
        if (areEventsEnabled()) {
          Object oldValue = event.getOldValue();
          Object newValue = event.getNewValue();
          if ((oldValue == null && newValue == null) ||
              (oldValue != null && newValue != null && oldValue.equals(newValue))) {
            return;
          } else {
            Content.this.propertyChangeEvents.add(event);
          }
        }
      }
    });
  }

  /**
   * Gets the content of this response.
   *
   * @return  a String
   */
  public abstract String getContent();

  /**
   * Gets the content of this response, truncated and without HTML tags.
   *
   * @return    the content of this response as a String
   */
  public String getTruncatedContent() {
    return StringUtils.truncate(getContent());
  }

  /**
   * Gets the state of this comment.
   *
   * @return  a State instance (APPROVED, REJECTED or PENDING)
   */
  public State getState() {
    return this.state;
  }

  /**
   * Sets the state of this comment.
   */
  void setState(State state) {
    this.state = state;
  }

  /**
   * Sets whether events are enabled.
   *
   * @param b   true to enable events, false otherwise
   */
  void setEventsEnabled(boolean b) {
    this.eventsEnabled = b;
  }

  /**
   * Determines whether events are enabled.
   *
   * @return  true if events are enabled, false otherwise
   */
  boolean areEventsEnabled() {
    return this.eventsEnabled;
  }

  /**
   * Clears existing property change events.
   */
  public void clearPropertyChangeEvents() {
    this.propertyChangeEvents = new ArrayList();
  }

  /**
   * Determines whether this class has had properties changed since it
   * was last persisted.
   *
   * @return  true if properties have changed, false otherwise
   */
  public boolean isDirty() {
    return !propertyChangeEvents.isEmpty();
  }

  /**
   * Gets the list of property change events.
   *
   * @return  a List of PropertyChangeEvent instances
   */
  public List getPropertyChangeEvents() {
    return (List)propertyChangeEvents.clone();
  }

  /**
   * Adds an event to the list.
   *
   * @param event   a PebbleEvent instance
   */
  synchronized void addEvent(PebbleEvent event) {
    events.add(event);
  }

  /**
   * Inserts an event to the list into the front of the list.
   *
   * @param event   a PebbleEvent instance
   */
  synchronized void insertEvent(PebbleEvent event) {
    events.add(0, event);
  }

  /**
   * Determines whether this object has outstanding events.
   *
   * @return    true if events are outstanding, false otherwise
   */
  public synchronized boolean hasEvents() {
    return !events.isEmpty();
  }

  /**
   * Gets the next event to be handled.
   *
   * @return  a PebbleEvent instance, or null if no more events
   */
  public synchronized PebbleEvent nextEvent() {
    if (hasEvents()) {
      return events.remove(0);
    } else {
      return null;
    }
  }

  synchronized void clearEvents() {
    events = new ArrayList<PebbleEvent>();
  }

  public synchronized List<PebbleEvent> getEvents() {
    return new ArrayList<PebbleEvent>(events);
  }

}