package io.github.cncsl.jblog.exception;

/**
 * 无效密码
 *
 * @author cncsl
 */
public class InvalidPasswordException extends ClientException {

    public InvalidPasswordException() {
        super("incorrect password!");
    }

}
