package io.github.cncsl.jblog.exception;

public class ServerException extends BlogException {

    public ServerException() {
        super("server unknown error");
    }

    public ServerException(String message) {
        super(message);
    }
}
