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
package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.plugins.Plugin;
import net.sourceforge.pebble.plugins.PluginConfig;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * Given a Collection or array, this tag produces a HTML select (dropdown) list
 * based upon the items contained within.
 *
 * @author    Simon Brown
 */
public class RenderPluginConfigTag extends TagSupport {

  /** the plugin config to render */
  private PluginConfig pluginConfig;

  /** The current value of the plugin config item */
  private Properties properties;

  /**
   * Called when the starting tag is encountered.
   */
  public int doStartTag() throws JspException {
    try
    {
      pluginConfig.getType().render(pageContext.getOut(), pluginConfig,
              properties.getProperty(pluginConfig.getKey()));
    }
    catch (IOException ioe)
    {
      throw new JspException(ioe);
    }
    // and skip the body
    return SKIP_BODY;
  }

  public void setPluginConfig(PluginConfig pluginConfig) {
    this.pluginConfig = pluginConfig;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }
}