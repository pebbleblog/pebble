package net.sourceforge.pebble.confirmation;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.domain.Blog;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReCaptchaConfirmationStrategy extends AbstractConfirmationStrategy {

     private static final Log log = LogFactory.getLog(ReCaptchaConfirmationStrategy.class);

     private static final String RECAPTCHA_PUBLIC_KEY = "reCAPTCHA.public.key";
     private static final String RECAPTCHA_PRIVATE_KEY = "reCAPTCHA.private.key";
     
     private static final String CHALLENGE = "reCAPTCHAChallenge";
     
     /**
      * Called before showing the confirmation page.
      *
      * @param request the HttpServletRequest used in the confirmation
      */
     public void setupConfirmation(HttpServletRequest request) {
           
           Blog blog = (Blog)request.getAttribute(Constants.BLOG_KEY);
           PluginProperties props = blog.getPluginProperties();
           
           boolean keySuccess = true;
           
           if (!props.hasProperty(RECAPTCHA_PUBLIC_KEY)) {
               keySuccess = false;
               log.error("failed to retrieve reCAPTCHA public API key");
           }
           if (!props.hasProperty(RECAPTCHA_PRIVATE_KEY)) { 
               keySuccess = false;
               log.error("failed to retrieve reCAPTCHA private API key");
           }
           
           if (keySuccess) {
               String publicKey = props.getProperty(RECAPTCHA_PUBLIC_KEY);
               String privateKey = props.getProperty(RECAPTCHA_PRIVATE_KEY);
         
               ReCaptcha c = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, false);
           
               request.getSession().setAttribute(CHALLENGE, c.createRecaptchaHtml(null, null));
           } else { 
               request.getSession().setAttribute(CHALLENGE, "failed to retrieve public and/or private reCAPTCHA key. Is the reCAPTCHA module configured?");
           }
     }

     /**
      * Gets the URI of the confirmation page.
      *
      * @return a URI, relative to the web application root.
      */
     public String getUri() {
       return "/WEB-INF/jsp/confirmation/reCaptcha.jsp";
     }

     /**
      * Called to determine whether confirmation was successful.
      *
      * @param request   the HttpServletRequest used in the confirmation
      * @return  true if the confirmation was successful, false otherwise
      */
     public boolean isConfirmed(HttpServletRequest request) {
         
           String remoteAddr = request.getRemoteAddr();
           
           Blog blog = (Blog)request.getAttribute(Constants.BLOG_KEY);
           PluginProperties props = blog.getPluginProperties();
           
           if (!props.hasProperty(RECAPTCHA_PRIVATE_KEY)) { 
               log.error("failed to retrieve reCAPTCHA private API key");
               return false;
           }
           
           String privateKey = props.getProperty(RECAPTCHA_PRIVATE_KEY);
             
           ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
           reCaptcha.setPrivateKey(privateKey);

           String challenge = request.getParameter("recaptcha_challenge_field");
           String uresponse = request.getParameter("recaptcha_response_field");
           
           ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

           return reCaptchaResponse.isValid();
     }

}

