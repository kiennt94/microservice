package vti.accountmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import vti.common.config.OpenApiConfig;

@Configuration
@Import(OpenApiConfig.class)
public class AccountManagementConfig {
    @Value("${swagger.packages-to-scan}")
    private String packagesToScan;

    @Value("${openapi.service.server}")
    private String serverUrl;
}
