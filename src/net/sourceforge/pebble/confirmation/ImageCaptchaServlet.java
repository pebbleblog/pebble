package net.sourceforge.pebble.confirmation;

import com.octo.captcha.service.CaptchaServiceException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Servlet that serves up the JCaptcha image captcha.
 *
 * @author Simon Brown
 */
public class ImageCaptchaServlet extends HttpServlet {

  private static final long serialVersionUID = -6227490839816434342L;

  private static final String JPG_FORMAT = "JPG";
  
  /**
   * Called to initialise the servlet.
   *
   * @param servletConfig
   * @throws ServletException
   */
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
  }

  /**
   * Called when a HTTP GET request is made to the servlet.
   *
   * @param httpServletRequest
   * @param httpServletResponse
   * @throws ServletException
   * @throws IOException
   */
  protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
    byte[] captchaChallengeAsJpeg;
    // the output stream to render the captcha image as jpeg into
    ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
    try {
      // get the session id that will identify the generated captcha.
      // the same id must be used to validate the response, the session id is a good candidate!
      String captchaId = httpServletRequest.getSession().getId();
      // call the CaptchaService getChallenge method
      BufferedImage challenge =
          CaptchaService.getInstance().getImageChallengeForID(captchaId,
              httpServletRequest.getLocale());

      javax.imageio.ImageIO.write(challenge, JPG_FORMAT, jpegOutputStream);
      
    } catch (IllegalArgumentException e) {
      httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    } catch (CaptchaServiceException e) {
      httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      return;
    }

    captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

    // flush it in the response
    httpServletResponse.setHeader("Cache-Control", "no-store");
    httpServletResponse.setHeader("Pragma", "no-cache");
    httpServletResponse.setDateHeader("Expires", 0);
    httpServletResponse.setContentType("image/jpeg");
    ServletOutputStream responseOutputStream =
        httpServletResponse.getOutputStream();
    responseOutputStream.write(captchaChallengeAsJpeg);
    responseOutputStream.flush();
    responseOutputStream.close();
  }

}