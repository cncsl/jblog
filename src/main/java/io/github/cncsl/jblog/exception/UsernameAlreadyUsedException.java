package io.github.cncsl.jblog.exception;

/**
 * 用户名已被使用
 *
 * @author cncsl
 */
public class UsernameAlreadyUsedException extends ClientException {

    public UsernameAlreadyUsedException() {
        super("username already used!");
    }

}
