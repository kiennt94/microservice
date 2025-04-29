package vti.departmentservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vti.departmentservice.response.account.AccountInfoDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountClient {
    private final RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    public List<AccountInfoDto> getAccountInfos(int departmentId) {
        return restTemplate.exchange(
                accountServiceUrl + "/department?departmentId=" + departmentId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountInfoDto>>() {
                }
        ).getBody();
    }
}
