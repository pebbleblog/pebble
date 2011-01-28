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
package net.sourceforge.pebble.confirmation;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * Simple maths confirmation strategy that asks the user to add/subtract/multiply
 * two random numbers together.
 *
 * @author    Simon Brown
 */
public class SimpleMathsConfirmationStrategy extends AbstractConfirmationStrategy {

  private static final String ARGUMENT1 = "SimpleMathsConfirmationStrategyArg1";
  private static final String ARGUMENT2 = "SimpleMathsConfirmationStrategyArg2";
  private static final String OPERATOR = "SimpleMathsConfirmationStrategyOperator";
  private static final String ANSWER = "SimpleMathsConfirmationStrategyAnswer";

  /**
   * Called before showing the confirmation page.
   *
   * @param request the HttpServletRequest used in the confirmation
   */
  public void setupConfirmation(HttpServletRequest request) {
    Random r = new Random();
    int arg1 = r.nextInt(10) + 1;
    int arg2 = r.nextInt(10) + 1;
    int op = r.nextInt(3);
    request.getSession().setAttribute(ARGUMENT1, arg1);
    request.getSession().setAttribute(ARGUMENT2, arg2);

    switch (op) {
      case 0 :
        request.getSession().setAttribute(OPERATOR, "+");
        request.getSession().setAttribute(ANSWER, arg1 + arg2);
        break;
      case 1 :
        request.getSession().setAttribute(OPERATOR, "-");
        request.getSession().setAttribute(ANSWER, arg1 - arg2);
        break;
      case 2 :
        request.getSession().setAttribute(OPERATOR, "*");
        request.getSession().setAttribute(ANSWER, arg1 * arg2);
        break;
    }
  }

  /**
   * Gets the URI of the confirmation page.
   *
   * @return a URI, relative to the web application root.
   */
  public String getUri() {
    return "/WEB-INF/jsp/confirmation/maths.jsp";
  }

  /**
   * Called to determine whether confirmation was successful.
   *
   * @param request   the HttpServletRequest used in the confirmation
   * @return  true if the confirmation was successful, false otherwise
   */
  public boolean isConfirmed(HttpServletRequest request) {
    Integer answer = (Integer)request.getSession().getAttribute(ANSWER);
    String userAnswer = request.getParameter("answer");

    return answer.toString().equals(userAnswer);
  }

}