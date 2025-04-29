package vti.accountmanagement.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import vti.accountmanagement.response.dto.department.DepartmentInfoDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentClient {

    private final RestTemplate restTemplate;

    @Value("${department.service.url}")
    private String departmentServiceUrl;

    public Map<Integer, String> getDepartmentsAsMap(List<Integer> departmentIds) {
        try {
            HttpEntity<List<Integer>> request = new HttpEntity<>(departmentIds);

            ResponseEntity<List<DepartmentInfoDto>> response = restTemplate.exchange(
                    departmentServiceUrl + "/by-ids",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            List<DepartmentInfoDto> departments = response.getBody();
            if (departments == null) return Collections.emptyMap();

            return departments.stream()
                    .collect(Collectors.toMap(
                            DepartmentInfoDto::getDepartmentId,
                            DepartmentInfoDto::getDepartmentName
                    ));

        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}
