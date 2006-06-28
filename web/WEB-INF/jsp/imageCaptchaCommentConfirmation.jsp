<div class="contentItem">

  <h1><fmt:message key="comment.addComment" /></h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <form action="confirmComment.action" method="post" accept-charset="${blog.characterEncoding}">
      
      <p>
        Please enter the word to confirm your comment.
        <br />
        <img src="${pageContext.request.contextPath}/jcaptcha" alt="Image captcha" />
        <br />
        <input type="text" name="j_captcha_response" value="">
      </p>

      <table width="99%">
        <tr>
          <td align="right">
            <input name="submit" type="submit" value="Confirm Comment" />
          </td>
        </tr>
      </table>
    </form>
  </div>

</div>
