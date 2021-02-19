package io.github.cncsl.jblog.exception;

/**
 * 邮箱已被使用
 *
 * @author cncsl
 */
public class EmailAlreadyUsedException extends ClientException {

    public EmailAlreadyUsedException() {
        super("email is already in use!");
    }

}
