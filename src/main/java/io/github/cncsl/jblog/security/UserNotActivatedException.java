package io.github.cncsl.jblog.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 用户未激活异常
 *
 * @author cncsl
 */
public class UserNotActivatedException extends AuthenticationException {

    public UserNotActivatedException(String message) {
        super(message);
    }

}

