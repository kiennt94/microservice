package vti.accountmanagement.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vti.accountmanagement.response.dto.position.PositionInfoDto;
import vti.common.enums.PositionName;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionClient {
    private final RestTemplate restTemplate;

    @Value("${position.service.url}")
    private String positionServiceUrl;

    public Map<Integer, PositionName> getPositionsAsMap(List<Integer> positionIds) {
        try {
            HttpEntity<List<Integer>> request = new HttpEntity<>(positionIds);

            ResponseEntity<List<PositionInfoDto>> response = restTemplate.exchange(
                    positionServiceUrl + "/by-ids",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            List<PositionInfoDto> positions = response.getBody();
            if (positions == null) return Collections.emptyMap();

            return positions.stream()
                    .collect(Collectors.toMap(
                            PositionInfoDto::getPositionId,
                            PositionInfoDto::getPositionName
                    ));

        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}
