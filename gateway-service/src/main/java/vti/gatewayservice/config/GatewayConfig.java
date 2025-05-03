package vti.gatewayservice.config;

import com.nimbusds.jwt.JWTParser;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Configuration
public class GatewayConfig {

    @Bean
    public GlobalFilter authFilter() {
        return (exchange, chain) -> {
            String token = extractToken(exchange);

            if (token != null) {
                try {
                    Map<String, Object> claims = JWTParser.parse(token).getJWTClaimsSet().getClaims();

                    // Lấy roles từ realm_access -> roles
                    Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
                    if (realmAccess != null && realmAccess.get("roles") instanceof List<?> rolesList) {
                        String roles = String.join(",", rolesList.stream().map(Object::toString).toList());

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-Roles", roles)
                                .build();

                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(mutatedExchange);
                    }
                } catch (ParseException e) {
                }
            }

            return chain.filter(exchange);
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
