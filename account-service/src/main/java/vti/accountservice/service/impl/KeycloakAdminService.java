package vti.accountservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.admin-client-id}")
    private String clientId;

    @Value("${keycloak.admin-client-secret}")
    private String clientSecret;

    @Value("${keycloak.realm}")
    private String realm;

    private final WebClient webClient;

    public KeycloakAdminService(WebClient keycloakAdminWebClient) {
        this.webClient = keycloakAdminWebClient;
    }

    public Mono<String> getAdminAccessToken() {
        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("access_token").asText());
    }

    public Mono<JsonNode> findUserByUsername(String username) {
        return getAdminAccessToken()
                .flatMap(token -> webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/admin/realms/{realm}/users")
                                .queryParam("username", username)
                                .build(realm))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .bodyToFlux(JsonNode.class)
                        .next());
    }
}
