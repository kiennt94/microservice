package vti.common.exception_handler;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {
    private final ApiError apiError;

    public CustomApiException(ApiError apiError) {
        super(apiError.getMessage());
        this.apiError = apiError;
    }
}
