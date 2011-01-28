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
package net.sourceforge.pebble.webservice;

import com.sun.syndication.propono.atom.server.AtomHandler;
import com.sun.syndication.propono.atom.server.AtomException;
import com.sun.syndication.propono.atom.common.AtomService;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Entry;

import java.io.InputStream;

/**
 * Implementation of a handler for the Atom Publishing Protocol.
 *
 * @author Simon Brown
 */
public class PebbleAtomHandler implements AtomHandler {

  public String getAuthenticatedUsername() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public AtomService getIntrospection() throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Feed getCollection(String[] strings) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Entry postEntry(String[] strings, Entry entry) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Entry getEntry(String[] strings) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Entry putEntry(String[] strings, Entry entry) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void deleteEntry(String[] strings) throws AtomException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public Entry postMedia(String[] strings, String string, String string1, String string2, InputStream inputStream) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Entry putMedia(String[] strings, String string, InputStream inputStream) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isIntrospectionURI(String[] strings) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isCollectionURI(String[] strings) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isEntryURI(String[] strings) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isMediaEditURI(String[] strings) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

}