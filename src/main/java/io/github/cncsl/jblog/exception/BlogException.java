package io.github.cncsl.jblog.exception;

/**
 * 博客程序通用自定义异常
 *
 * @author cncsl
 */
public class BlogException extends RuntimeException {

    public BlogException() {
        super("unknown exception!");
    }

    public BlogException(String message) {
        super(message);
    }
}
