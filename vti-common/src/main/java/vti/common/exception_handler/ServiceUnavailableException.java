package vti.common.exception_handler;

import lombok.Getter;

@Getter
public class ServiceUnavailableException extends RuntimeException {
    private final String path;

    public ServiceUnavailableException(String message, String path) {
        super(message);
        this.path = path;
    }
}
