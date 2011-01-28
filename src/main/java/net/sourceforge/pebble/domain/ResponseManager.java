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

import net.sourceforge.pebble.comparator.ResponseByDateComparator;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackListener;

import java.util.*;

/**
 * Internal comment and TrackBack listener used to manage the list of
 * responses for the associated blog.
 *
 * @author    Simon Brown
 */
public class ResponseManager implements CommentListener, TrackBackListener {

  /** the owning blog */
  private Blog blog;

  /** the set of recent comments */
  private SortedSet recentComments;

  /** the set of recent TrackBacks */
  private SortedSet recentTrackBacks;

  /** the set of recent responses (comments and TrackBacks) */
  private SortedSet approvedResponses;

  /** the set of pending responses (comments and TrackBacks) */
  private SortedSet pendingResponses;

  /** the set of rejected responses (comments and TrackBacks) */
  private SortedSet rejectedResponses;

  /**
   * Creates a new instance associated with the specified blog.
   *
   * @param blog    a Blog instance
   */
  public ResponseManager(Blog blog) {
    this.blog = blog;

    recentComments = new TreeSet(new ResponseByDateComparator());
    recentTrackBacks = new TreeSet(new ResponseByDateComparator());
    approvedResponses = new TreeSet(new ResponseByDateComparator());
    pendingResponses = new TreeSet(new ResponseByDateComparator());
    rejectedResponses = new TreeSet(new ResponseByDateComparator());
  }

  /**
   * Gets recent comments.
   *
   * @return  a collection containing comments that have been left most recently
   */
  public Collection getRecentComments() {
    return this.recentComments;
  }

  /**
   * Gets recent TrackBacks.
   *
   * @return  a collection containing TrackBacks that have been left most recently
   */
  public Collection getRecentTrackBacks() {
    return this.recentTrackBacks;
  }

  /**
   * Gets recent responses (combined comments and TrackBacks).
   *
   * @return  a collection containing comments and TrackBacks that have been left
   *          most recently
   */
  public Collection getRecentResponses() {
    List list = new ArrayList();
    list.addAll(approvedResponses);
    return list;
  }

  /**
   * Gets the number of approved responses.
   *
   * @return  an int
   */
  public int getNumberOfApprovedResponses() {
    return approvedResponses.size();
  }

  /**
   * Gets pending responses (combined comments and TrackBacks).
   *
   * @return  a collection containing comments and TrackBacks that are pending
   */
  public Collection getPendingResponses() {
    List list = new ArrayList();
    list.addAll(pendingResponses);
    return list;
  }

  /**
   * Gets the number of pending responses.
   *
   * @return  an int
   */
  public int getNumberOfPendingResponses() {
    return pendingResponses.size();
  }

  /**
   * Gets rejected responses (combined comments and TrackBacks).
   *
   * @return  a collection containing comments and TrackBacks that are rejected
   */
  public Collection getRejectedResponses() {
    List list = new ArrayList();
    list.addAll(rejectedResponses);
    return list;
  }

  /**
   * Gets the number of rejected responses.
   *
   * @return  an int
   */
  public int getNumberOfRejectedResponses() {
    return rejectedResponses.size();
  }

  /**
   * Called when a comment has been added.
   *
   * @param comment   a Comment instance
   */
  synchronized void addRecentComment(Comment comment) {
    if (comment.isApproved()) {
      recentComments.add(comment);
      approvedResponses.add(comment);
    } else if (comment.isPending()) {
      pendingResponses.add(comment);
    } else if (comment.isRejected()) {
      rejectedResponses.add(comment);
    }
  }

  /**
   * Called when a comment has been removed.
   *
   * @param comment   a Comment instance
   */
  synchronized void removeRecentComment(Comment comment) {
    recentComments.remove(comment);
    approvedResponses.remove(comment);
    pendingResponses.remove(comment);
    rejectedResponses.remove(comment);
  }

  /**
   * Called when a TrackBack has been added.
   *
   * @param trackBack   a TrackBack instance
   */
  synchronized void addRecentTrackBack(TrackBack trackBack) {
    if (trackBack.isApproved()) {
      recentTrackBacks.add(trackBack);
      approvedResponses.add(trackBack);
    } else if (trackBack.isPending()) {
      pendingResponses.add(trackBack);
    } else if (trackBack.isRejected()) {
      rejectedResponses.add(trackBack);
    }
  }

  /**
   * Called when a TrackBack has been removed.
   *
   * @param trackBack   a TrackBack instance
   */
  synchronized void removeRecentTrackBack(TrackBack trackBack) {
    recentTrackBacks.remove(trackBack);
    approvedResponses.remove(trackBack);
    pendingResponses.remove(trackBack);
    rejectedResponses.remove(trackBack);
  }

  /**
   * Called when a comment has been added.
   *
   * @param event a CommentEvent instance
   */
  public void commentAdded(CommentEvent event) {
    addRecentComment(event.getComment());
  }

  /**
   * Called when a comment has been removed.
   *
   * @param event a CommentEvent instance
   */
  public void commentRemoved(CommentEvent event) {
    removeRecentComment(event.getComment());
  }

  /**
   * Called when a comment has been approved.
   *
   * @param event a CommentEvent instance
   */
  public void commentApproved(CommentEvent event) {
    removeRecentComment(event.getComment());
    addRecentComment(event.getComment());
  }

  /**
   * Called when a comment has been rejected.
   *
   * @param event a CommentEvent instance
   */
  public void commentRejected(CommentEvent event) {
    removeRecentComment(event.getComment());
    addRecentComment(event.getComment());
  }

  /**
   * Called when a TrackBack has been added.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackAdded(TrackBackEvent event) {
    addRecentTrackBack(event.getTrackBack());
  }

  /**
   * Called when a TrackBack has been removed.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackRemoved(TrackBackEvent event) {
    removeRecentTrackBack(event.getTrackBack());
  }

  /**
   * Called when a TrackBack has been approved.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackApproved(TrackBackEvent event) {
    removeRecentTrackBack(event.getTrackBack());
    addRecentTrackBack(event.getTrackBack());
  }

  /**
   * Called when a TrackBack has been rejected.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackRejected(TrackBackEvent event) {
    removeRecentTrackBack(event.getTrackBack());
    addRecentTrackBack(event.getTrackBack());
  }

  /**
   * Gets the number of responses.
   */
  public int getNumberOfResponses() {
    return approvedResponses.size() + pendingResponses.size() + rejectedResponses.size();
  }

}
