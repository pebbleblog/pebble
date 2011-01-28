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

import java.util.regex.Pattern;

/**
 * Represents a blog category.
 *
 * @author    Simon Brown
 */
public class RefererFilter implements Comparable {

  // the internal ID for this filter
  private long id;

  /** the underlying filter expression */
  private String expression;

  /** the compiled version of the expression */
  private Pattern compiledExpression;

  public RefererFilter() {
  }

  /**
   * Creates a new category with the specified properties.
   *
   * @param expression    the filter expression
   */
  public RefererFilter(String expression) {
    setExpression(expression);
  }

  void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return this.id;
  }

  /**
   * Gets the expression.
   *
   * @return    the expression as a String
   */
  public String getExpression() {
    return this.expression;
  }

  /**
   * Sets the expression.
   *
   * @param expression    the expression as a String
   */
  public void setExpression(String expression) {
    if (expression == null) {
      this.expression = "";
    } else {
      this.expression = expression;
    }
    this.compiledExpression = Pattern.compile(this.expression);
  }

  /**
   * Gets the compiled version of the expression.
   *
   * @return    the compiled expression as a Pattern
   */
  public Pattern getCompiledExpression() {
    return this.compiledExpression;
  }

  /**
   * Gets the hashcode of this object.
   *
   * @return  the hashcode as an int
   */
  public int hashCode() {
    return expression.hashCode();
  }

  /**
   * Determines whether the specified object is equal to this one.
   *
   * @param o   the object to compare against
   * @return    true if Object o represents the same category, false otherwise
   */
  public boolean equals(Object o) {
    if (!(o instanceof RefererFilter)) {
      return false;
    }

    RefererFilter filter = (RefererFilter)o;
    return (filter.getExpression().equals(expression));
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer if this object is less
   * than, equal to, or greater than the specified object.
   *
   * @param   o the Object to be compared.
   * @return  a negative integer, zero, or a positive integer if this object
   *		is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this Object.
   */
  public int compareTo(Object o) {
    RefererFilter category = (RefererFilter)o;
    return getExpression().compareTo(category.getExpression());
  }

}