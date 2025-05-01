package vti.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import vti.common.config.OpenApiConfig;

@Configuration
@Import(OpenApiConfig.class)
public class GatewayConfig {
    @Value("${swagger.packages-to-scan}")
    private String packagesToScan;

    @Value("${openapi.service.server}")
    private String serverUrl;

    @Bean
    public GlobalFilter authFilter() {
        return (exchange, chain) -> {
            // Get token from request header
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token != null && token.startsWith("Bearer ")) {
                // Forward the token to the backend service
                exchange.getRequest().mutate()
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .build();
            }

            return chain.filter(exchange);
        };
    }
}
