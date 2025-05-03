package vti.accountservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KeycloakAdminWebClientConfig {

    @Value("${keycloak.auth-server-url}")
    private String keycloakBaseUrl;

    @Bean
    public WebClient keycloakAdminWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(keycloakBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }
}