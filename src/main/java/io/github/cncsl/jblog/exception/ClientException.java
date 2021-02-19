package io.github.cncsl.jblog.exception;

public class ClientException extends BlogException {

    public ClientException() {
        super("client unknown error");
    }

    public ClientException(String message) {
        super(message);
    }
}
