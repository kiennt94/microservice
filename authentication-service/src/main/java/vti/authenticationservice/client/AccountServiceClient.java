package vti.authenticationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vti.authenticationservice.request.AuthenticationRequest;
import vti.common.dto.AccountDto;
import vti.commonservice.config.FeignClientConfig;

@FeignClient(name = "account-service", path = "/api/account", configuration = FeignClientConfig.class)
public interface AccountServiceClient {
    @PostMapping("/auth")
    AccountDto authenticate(@RequestBody AuthenticationRequest req);
}
