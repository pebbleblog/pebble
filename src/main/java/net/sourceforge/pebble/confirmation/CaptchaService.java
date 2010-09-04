package net.sourceforge.pebble.confirmation;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Singleton wrapper for the JCaptcha services.
 *
 * @author    Simon Brown
 */
public class CaptchaService {

  private static final ImageCaptchaService instance = new DefaultManageableImageCaptchaService();

  /**
   * Gets the singleton ImageCaptchaService instance.
   *
   * @return  an ImageCaptchaService instance
   */
  public static ImageCaptchaService getInstance(){
      return instance;
  }

}
