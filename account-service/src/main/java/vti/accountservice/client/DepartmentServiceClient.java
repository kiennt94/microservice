package vti.accountservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vti.accountservice.response.dto.department.DepartmentInfoDto;
import vti.commonservice.config.FeignClientConfig;

import java.util.List;

@FeignClient(name = "department-service", path = "/api/department", configuration = FeignClientConfig.class)
public interface DepartmentServiceClient {

        @PostMapping("/by-ids")
        List<DepartmentInfoDto> getDepartmentsByIds(@RequestBody List<Integer> ids);

        @GetMapping("/{id}")
        DepartmentInfoDto getDepartmentById(@PathVariable Integer id);
}
