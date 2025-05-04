package vti.accountservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import vti.common.utils.ConstantUtils;

import java.util.List;

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
                        .header(HttpHeaders.AUTHORIZATION, ConstantUtils.BEARER + token)
                        .retrieve()
                        .bodyToFlux(JsonNode.class)
                        .next());
    }

    public Mono<List<String>> getAllRealmRoles() {
        return getAdminAccessToken()
                .flatMap(token -> webClient.get()
                        .uri("/admin/realms/{realm}/roles", realm)
                        .header(HttpHeaders.AUTHORIZATION, ConstantUtils.BEARER + token)
                        .retrieve()
                        .bodyToFlux(JsonNode.class)
                        .map(roleNode -> roleNode.get("name").asText())
                        .collectList()
                );
    }

    public Mono<String> createUserOnKeycloak(String username, String password, String roleName) {
        return getAdminAccessToken()
                .flatMap(token -> webClient.post()
                        .uri("/admin/realms/{realm}/users", realm)
                        .header(HttpHeaders.AUTHORIZATION, ConstantUtils.BEARER + token)
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .bodyValue(createUserPayload(username, password))
                        .retrieve()
                        .toBodilessEntity()
                        .flatMap(response -> findUserByUsername(username)
                                .flatMap(userJson -> assignRealmRoleToUser(userJson.get("id").asText(), roleName)
                                        .thenReturn(userJson.get("id").asText())))
                );
    }


    public Mono<JsonNode> getRealmRoleRepresentation(String token, String roleName) {
        return webClient.get()
                .uri("/admin/realms/{realm}/roles/{roleName}", realm, roleName)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    public Mono<Void> assignRealmRoleToUser(String userId, String roleName) {
        return getAdminAccessToken()
                .flatMap(token -> getRealmRoleRepresentation(token, roleName)
                        .flatMap(role -> webClient.post()
                                .uri("/admin/realms/{realm}/users/{userId}/role-mappings/realm", realm, userId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                                .bodyValue(List.of(role))
                                .retrieve()
                                .bodyToMono(Void.class)
                        )
                );
    }


    private JsonNode createUserPayload(String username, String password) {
        return new com.fasterxml.jackson.databind.node.ObjectNode(com.fasterxml.jackson.databind.node.JsonNodeFactory.instance)
                .put("username", username)
                .put("enabled", true)
                .set("credentials", com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.arrayNode()
                        .add(new com.fasterxml.jackson.databind.node.ObjectNode(com.fasterxml.jackson.databind.node.JsonNodeFactory.instance)
                                .put("type", "password")
                                .put("value", password)
                                .put("temporary", false)));
    }

}
