package vti.gatewayservice.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityHeadersFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();

        response.beforeCommit(() -> {
            response.getHeaders().set("X-Content-Type-Options", "nosniff");
            response.getHeaders().set("X-Frame-Options", "DENY");
            response.getHeaders().set("Referrer-Policy", "no-referrer");
            response.getHeaders().set("Permissions-Policy", "geolocation=(), camera=(), microphone=()");
            response.getHeaders().set("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
            response.getHeaders().set("Content-Security-Policy", "default-src 'self'; script-src 'self'; object-src 'none'; frame-ancestors 'none'");
            return Mono.empty();
        });

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
