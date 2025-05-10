//package vti.common.exception_handler;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//public class FeignErrorDecoder implements ErrorDecoder {
//
//    private final ObjectMapper objectMapper;
//
//    public FeignErrorDecoder() {
//        this.objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.findAndRegisterModules();
//    }
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        try (InputStream inputStream = response.body().asInputStream()) {
//            String body = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
//                    .lines()
//                    .collect(Collectors.joining("\n"));
//
//            log.warn("Feign raw error response: {}", body);
//
//            JsonNode node = objectMapper.readTree(body);
//
//            int status = node.has("status") ? node.get("status").asInt() : response.status();
//            String message = node.has("message") ? node.get("message").asText() : "Unknown error";
//            String path = node.has("path") ? node.get("path").asText() : "UNKNOWN";
//
//            return new HttpResponseException(status, message, path);
//
//        } catch (Exception ex) {
//            log.error("Failed to decode Feign error", ex);
//            return new HttpResponseException(
//                    response.status(),
//                    "Unexpected error occurred while decoding error response",
//                    "UNKNOWN"
//            );
//        }
//    }
//}
