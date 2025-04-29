package vti.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vti.common.dto.AccountDto;
import vti.common.service.CommonAccountService;

@Service
@RequiredArgsConstructor
public class CommonAccountServiceImpl implements CommonAccountService {

    private final RestTemplate restTemplate;
    @Value("${account.service.find.username.url}")
    private String accountServiceFindUsernameUrl;

    @Override
    public AccountDto findByUsername(String username, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<AccountDto> response = restTemplate.exchange(
                accountServiceFindUsernameUrl + username,
                HttpMethod.GET,
                entity,
                AccountDto.class
        );
        return response.getBody();
    }
}
