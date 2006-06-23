package net.sourceforge.pebble.comment;

import net.sourceforge.pebble.api.comment.AbstractCommentConfirmationStrategy;
import net.sourceforge.pebble.domain.Comment;

import javax.servlet.http.HttpServletRequest;

import com.octo.captcha.service.CaptchaServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Image captcha comment confirmation strategy.
 *
 * @author    Simon Brown
 */
public class ImageCaptchaCommentConfirmationStrategy extends AbstractCommentConfirmationStrategy {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(ImageCaptchaCommentConfirmationStrategy.class);

  /**
   * Called before showing the confirmation page.
   *
   * @param request the HttpServletRequest used in the confirmation
   * @param comment the Comment being confirmed
   */
  public void setupConfirmation(HttpServletRequest request, Comment comment) {
  }

  /**
   * Gets the URI of the confirmation page.
   *
   * @return a URI, relative to the web application root.
   */
  public String getUri() {
    return "/WEB-INF/jsp/imageCaptchaCommentConfirmation.jsp";
  }

  /**
   * Called to confirm a comment.
   *
   * @param request the HttpServletRequest used in the confirmation
   * @param comment the Comment being confirmed
   * @return true if the comment has been successfully confirmed,
   *         false otherwise
   */
  public boolean confirmComment(HttpServletRequest request, Comment comment) {
    String captchaId = request.getSession().getId();
    String response = request.getParameter("j_captcha_response");
    try {
      return CaptchaService.getInstance().validateResponseForID(captchaId, response);
    } catch (CaptchaServiceException e) {
      log.error(e);
    }

    return false;
  }

}
