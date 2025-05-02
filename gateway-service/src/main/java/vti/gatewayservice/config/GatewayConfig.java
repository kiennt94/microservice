package vti.gatewayservice.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class GatewayConfig {
    @Bean
    public GlobalFilter authFilter() {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token != null && token.startsWith("Bearer ")) {
                exchange.getRequest().mutate()
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .build();
            }

            return chain.filter(exchange);
        };
    }
}
