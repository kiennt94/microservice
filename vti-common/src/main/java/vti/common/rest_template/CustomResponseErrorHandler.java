package vti.common.rest_template;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import vti.common.exception_handler.ApiError;
import vti.common.exception_handler.CustomApiException;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomResponseErrorHandler implements ResponseErrorHandler {

    private final @Lazy ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        ApiError apiError = objectMapper.readValue(response.getBody(), ApiError.class);
        throw new CustomApiException(apiError);
    }
}
