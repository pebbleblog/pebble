<div class="contentItem">
  <h1>Writing Confirmation Strategies</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      <a href="./help/confirmationStrategies.html">Confirmation Strategies</a> are a type of Pebble plugin that can be used to better assert that somebody
      leaving a comment or trying to send a TrackBack is human, as opposed to an automated comment spam agent. It does this by providing a pluggable
      strategy for asking readers to confirm their action by means of clicking a button or through some other kind of CAPTCHA. To implement your own
      confirmation strategy, you need to implement an interface that performs the processing and provide a JSP that provides the interaction with the user.
    </p>

    <p>
      The top level interface for all confirmation strategies is called <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/confirmation/ConfirmationStrategy.html">ConfirmationStrategy</a>, while
      the comment and TrackBack specific interfaces are called <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/confirmation/CommentConfirmationStrategy.html">CommentConfirmationStrategy</a> and <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/api/confirmation/TrackBackConfirmationStrategy.html">TrackBackConfirmationStrategy</a> respectively.
      <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/confirmation/AbstractConfirmationStrategy.html">AbstractConfirmationStrategy</a> can be used as a starting point for both comment and TrackBack confirmation strategy implementations.
      This class provides implementations of the methods that determine whether confirmation is required for a given comment or TrackBack request, using the rules
      outlined in the confirmation process for <a href="./help/confirmationStrategies.html#commentProcess">comments</a> and <a href="./help/confirmationStrategies.html#trackbackProcess">TrackBacks</a>.
    </p>

    <p>
      The <a href="${pageContext.request.contextPath}/javadoc/net/sourceforge/pebble/confirmation/SimpleMathsConfirmationStrategy.html">SimpleMathsConfirmationStrategy</a> is shown here as an example.
      This extends the abstract implementation to provide a specific implementation that asks the user to add/subtract/divide two
      random numbers between one and ten.
    </p>

<pre class="codeSample">package net.sourceforge.pebble.confirmation;

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

}</pre>

    <p>
      Assuming confirmation is deemed to be required, the <code>setupConfirmation()</code> method is called
      to initialise the confirmation strategy. Next, the JSP referenced by the <code>getUri()</code> method is presented to the user to initiate the human interaction part
      of the strategy. When submitted, the <code>isConfirmed()</code> method is called to determine whether confirmation was successful.
    </p>

    <p>
      The JSP used in the simple maths strategy is shown below, and the only real requirememt is that it submits back to an action of <code>&#0036;{confirmationAction}</code>.
    </p>

<pre class="codeSample">&lt;div class="contentItem"&gt;

  &lt;h1&gt;&lt;fmt:message key="confirmation.confirm" /&gt;&lt;/h1&gt;
  &lt;h2&gt;&amp;nbsp;&lt;/h2&gt;

  &lt;div class="contentItemBody"&gt;
    &lt;form name="confirmationForm" action="&#0036;{confirmationAction}" method="post" accept-charset="&#0036;{blog.characterEncoding}"&gt;

      &lt;p&gt;
        &lt;fmt:message key="confirmation.simpleMathsMessage" /&gt;
      &lt;/p&gt;

      &lt;table width="99%"&gt;

        &lt;tr&gt;
          &lt;td&gt;&#0036;{SimpleMathsConfirmationStrategyArg1} &#0036;{SimpleMathsConfirmationStrategyOperator} &#0036;{SimpleMathsConfirmationStrategyArg2} = &lt;input type="text" name="answer" size="4" /&gt;&lt;/td&gt;
        &lt;/tr&gt;

        &lt;tr&gt;
          &lt;td align="right"&gt;
            &lt;input name="submit" type="submit" value="&lt;fmt:message key='confirmation.confirm' /&gt;" /&gt;
          &lt;/td&gt;
        &lt;/tr&gt;

      &lt;/table&gt;
    &lt;/form&gt;
  &lt;/div&gt;

&lt;/div&gt;

&lt;script type="text/javascript"&gt;
window.onload = function()
{
  document.confirmationForm.answer.focus();
}
&lt;/script&gt;</pre>

  </div>
</div>