package vti.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vti.common.rest_template.CustomResponseErrorHandler;

@Configuration
public class RestTemplateConfig {

    @Bean
    public CustomResponseErrorHandler customResponseErrorHandler(ObjectMapper objectMapper) {
        return new CustomResponseErrorHandler(objectMapper);
    }

    @Bean
    public RestTemplate restTemplate(CustomResponseErrorHandler errorHandler) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(errorHandler);

        restTemplate.getInterceptors().add((request, body, execution) -> {
            String token = extractTokenFromRequestHeader();
            if (token != null && !token.isEmpty()) {
                request.getHeaders().set("Authorization", "Bearer " + token);
            }
            return execution.execute(request, body);
        });

        return restTemplate;
    }

    private String extractTokenFromRequestHeader() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        return null;
    }
}
