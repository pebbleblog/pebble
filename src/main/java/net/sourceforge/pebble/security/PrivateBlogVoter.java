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
package net.sourceforge.pebble.security;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.util.SecurityUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * AccessDecisionVoter that votes ACCESS_GRANTED if the user is :
 *  - a blog admin user
 *  - authorised for the blog (owner, publisher or contributor)
 *  - a blog reader
 *
 * Otherwise, access is denied.
 *
 * @author Simon Brown
 */
public class PrivateBlogVoter implements AccessDecisionVoter {

  /**
   * Indicates whether this <code>AccessDecisionVoter</code> is able to vote on the passed
   * <code>ConfigAttribute</code>.<p>This allows the <code>AbstractSecurityInterceptor</code> to check every
   * configuration attribute can be consumed by the configured <code>AccessDecisionManager</code> and/or
   * <code>RunAsManager</code> and/or <code>AfterInvocationManager</code>.</p>
   *
   * @param attribute a configuration attribute that has been configured against the
   *                  <code>AbstractSecurityInterceptor</code>
   * @return true if this <code>AccessDecisionVoter</code> can support the passed configuration attribute
   */
  public boolean supports(ConfigAttribute attribute) {
    return attribute instanceof PrivateBlogConfigAttributeDefinition;
  }

  /**
   * Indicates whether the <code>AccessDecisionVoter</code> implementation is able to provide access control
   * votes for the indicated secured object type.
   *
   * @param clazz the class that is being queried
   * @return true if the implementation can process the indicated class
   */
  public boolean supports(Class clazz) {
    return true;
  }

  /**
   * Indicates whether or not access is granted.<p>The decision must be affirmative
   * (<code>ACCESS_GRANTED</code>), negative (<code>ACCESS_DENIED</code>) or the <code>AccessDecisionVoter</code>
   * can abstain (<code>ACCESS_ABSTAIN</code>) from voting. Under no circumstances should implementing classes
   * return any other value. If a weighting of results is desired, this should be handled in a custom {@link
   * org.springframework.security.access.AccessDecisionManager} instead.</p>
   * <P>Unless an <code>AccessDecisionVoter</code> is specifically intended to vote on an access control
   * decision due to a passed method invocation or configuration attribute parameter, it must return
   * <code>ACCESS_ABSTAIN</code>. This prevents the coordinating <code>AccessDecisionManager</code> from counting
   * votes from those <code>AccessDecisionVoter</code>s without a legitimate interest in the access control
   * decision.</p>
   * <p>Whilst the method invocation is passed as a parameter to maximise flexibility in making access
   * control decisions, implementing classes must never modify the behaviour of the method invocation (such as
   * calling <Code>MethodInvocation.proceed()</code>).</p>
   *
   * @param authentication the caller invoking the method
   * @param object         the secured object
   * @param config         the configuration attributes associated with the method being invoked
   * @return either {@link #ACCESS_GRANTED}, {@link #ACCESS_ABSTAIN} or {@link #ACCESS_DENIED}
   */
  public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> config) {
    // Ok, the way this has been implemented is bad... but it's 2am and I'm not about to fix it.
    for (ConfigAttribute attribute : config) {
      if (attribute instanceof PrivateBlogConfigAttributeDefinition) {
        PrivateBlogConfigAttributeDefinition cad = (PrivateBlogConfigAttributeDefinition) attribute;
        Blog blog = cad.getBlog();
        if (SecurityUtils.isBlogAdmin(authentication)) {
          // admin users need access to all blogs
          return ACCESS_GRANTED;
        } else if (SecurityUtils.isUserAuthorisedForBlog(authentication, blog)) {
          // blog owners/publishers/contributors need access, if they have it
          return ACCESS_GRANTED;
        } else if (SecurityUtils.isUserAuthorisedForBlogAsBlogReader(authentication, blog)) {
          // the user is an authorised blog reader
            return ACCESS_GRANTED;
        }
        return ACCESS_DENIED;
      }
    }
    return ACCESS_ABSTAIN;
  }
}