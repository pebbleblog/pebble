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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Given a Collection or array, this tag produces a HTML select (dropdown) list
 * based upon the items contained within.
 *
 * @author    Simon Brown
 */
public class SelectTag extends TagSupport {

  /** the items over which this tag should iterate */
  private Collection items;

  /** the name of the select control */
  private String name;

  /** the size of the select control */
  private int size = -1;

  /** the multiple attribute */
  private boolean multiple = false;

  /** the name of the property to be used as the displayed label */
  private String label;

  /** the name of the property to be used as the hidden value */
  private String value;

  /** the selected value */
  private Object selected;

  /**
   * Called when the starting tag is encountered.
   */
  public int doStartTag() throws JspException {
    // setup the iterator to be used
    Iterator iterator = items.iterator();

    try {
      JspWriter out = pageContext.getOut();

      // write the starting tag of the select control
      out.print("<select name=\"");
      out.print(name);
      out.print("\"");

      if (size > 0) {
        out.print(" size=\"");
        out.print(size);
        out.print("\"");
      }

      if (multiple) {
        out.print(" multiple=\"true\"");
      }

      out.print(">");

      while (iterator.hasNext()) {
        // get the next JavaBean from the items
        Object o = iterator.next();

        // and the property used to represent the hidden value
        String hiddenValue;
        if (value != null) {
          Method m = o.getClass().getMethod("get" + value.substring(0, 1).toUpperCase() + value.substring(1), new Class[] {});
          hiddenValue = m.invoke(o, new Object[]{}).toString();
        } else {
          hiddenValue = o.toString();
        }

        // and now generate the HTML
        out.print("<option value=\"");

        // call the accessor method for the value property
        // (this is the same as calling get<PropertyName>() on
        // the JavaBean instance)
        out.print(hiddenValue);
        out.print("\"");

        if (selected != null) {
          if (selected instanceof Collection) {
            Collection coll = (Collection)selected;
            if (coll.contains(hiddenValue)) {
              out.print(" selected=\"true\"");
            }
          } else if (selected.toString().equals(hiddenValue)) {
            out.print(" selected=\"true\"");
          }
        }
        out.print(">");

        if (label != null) {
          // and do the same for the label property
          // and use it to create a description of the property used
          // to represent the displayable label
          Method m = o.getClass().getMethod("get" + label.substring(0, 1).toUpperCase() + label.substring(1), new Class[] {});
          out.print(
              m.invoke(o, new Object[]{}));
        } else {
          out.print(o.toString());
        }
        out.print("</option>");
      }

      // write the ending tag of the select control
      out.print("</select>");
    } catch (Exception e) {
      e.printStackTrace();
      throw new JspTagException(e.getMessage());
    }

    // and skip the body
    return SKIP_BODY;
  }

  /**
   * Sets the items over which this tag should iterate.
   *
   * @param items   a Collection or array
   */
  public void setItems(Object items) {
    if (items instanceof Collection) {
      this.items = (Collection)items;
    } else if (items instanceof Object[]) {
      this.items = Arrays.asList((Object[])items);
    }
  }

  /**
   * Sets the name for the generated select control.
   *
   * @param name    the name as a String
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the size of the generated select control.
   *
   * @param size    the size
   */
  public void setSize(int size) {
    this.size = size;
  }

  /**
   * Sets the multiple attribute on the underlying select control.
   *
   * @param   multiple    a boolean
   */
  public void setMultiple(boolean multiple) {
    this.multiple = multiple;
  }

  /**
   * Sets the name of the property to display.
   *
   * @param label   the name of the label property
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Sets the name of the property to use as the hidden value.
   *
   * @param value   the name of the value property
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Sets the selected value.
   *
   * @param selected    the selected value
   */
  public void setSelected(Object selected) {
    this.selected = selected;
  }

}