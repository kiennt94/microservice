package vti.common.exception_handler;

import lombok.Getter;
import vti.common.payload.ApiError;

@Getter
public class CustomApiException extends RuntimeException {
    private final transient ApiError apiError;

    public CustomApiException(ApiError apiError) {
        super(apiError.getMessage());
        this.apiError = apiError;
    }
}
