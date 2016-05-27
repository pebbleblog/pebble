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

import com.rometools.propono.atom.common.Categories;
import com.rometools.propono.atom.server.AtomHandler;
import com.rometools.propono.atom.server.AtomException;
import com.rometools.propono.atom.common.AtomService;
import com.rometools.propono.atom.server.AtomMediaResource;
import com.rometools.propono.atom.server.AtomRequest;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.atom.Entry;

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

  public AtomService getAtomService(AtomRequest atomRequest) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Categories getCategories(AtomRequest atomRequest) throws AtomException {
    return null;
  }

  public Feed getCollection(AtomRequest atomRequest) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public AtomMediaResource getMediaResource(AtomRequest atomRequest) throws AtomException {
    return null;
  }

  public Entry postEntry(AtomRequest atomRequest, Entry entry) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Entry getEntry(AtomRequest atomRequest) throws AtomException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void putEntry(AtomRequest atomRequest, Entry entry) throws AtomException {
     //To change body of implemented methods use File | Settings | File Templates.
  }

  public void deleteEntry(AtomRequest atomRequest) throws AtomException {
  }

  public Entry postMedia(AtomRequest atomRequest, Entry entry) throws AtomException {
    return null;
  }

  public void putMedia(AtomRequest atomRequest) throws AtomException {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isCollectionURI(AtomRequest atomRequest) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isCategoriesURI(AtomRequest atomRequest) {
    return false;
  }

  public boolean isAtomServiceURI(AtomRequest atomRequest) {
    return false;
  }

  public boolean isEntryURI(AtomRequest atomRequest) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public boolean isMediaEditURI(AtomRequest atomRequest) {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }
}