package vti.authenticationservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vti.authenticationservice.request.AuthenticationRequest;
import vti.common.dto.AccountDto;

@Service
@RequiredArgsConstructor
public class AccountClient {
    private final RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    public AccountDto getAccountDto(AuthenticationRequest authenticationRequest) {
        return restTemplate.postForObject(accountServiceUrl + "/auth", authenticationRequest, AccountDto.class);
    }
}
