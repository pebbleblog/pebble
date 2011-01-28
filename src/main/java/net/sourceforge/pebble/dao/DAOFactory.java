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
package net.sourceforge.pebble.dao;

/**
 * Represents a strategy used to load and store blog entries
 * in the filing system.
 *
 * @author    Simon Brown
 */
public abstract class DAOFactory {

  /** the configured DAO factory */
  private static DAOFactory configuredFactory;

  /**
   * Gets a DAO instance responsible for the dao of blog entries.
   *
   * @return  a BlogEntryDAO instance
   */
  public abstract BlogEntryDAO getBlogEntryDAO();

  /**
   * Gets a DAO instance responsible for the dao of static pages.
   *
   * @return  a StaticPageDAO instance
   */
  public abstract StaticPageDAO getStaticPageDAO();

  /**
   * Gets a DAO instance responsible for the dao of categories.
   *
   * @return  a CategoryDAO instance
   */
  public abstract CategoryDAO getCategoryDAO();

  /**
   * Gets a DAO instance responsible for the dao of referer filters.
   *
   * @return  a RefererFilterDAO instance
   */
  public abstract RefererFilterDAO getRefererFilterDAO();

  public static void setConfiguredFactory(DAOFactory factory) {
    configuredFactory = factory;
  }

  public static DAOFactory getConfiguredFactory() {
    return configuredFactory;
  }

}