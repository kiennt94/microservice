package vti.departmentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vti.commonservice.config.FeignClientConfig;
import vti.departmentservice.response.account.AccountInfoDto;

import java.util.List;

@FeignClient(name = "account-service", path = "/api/account", configuration = FeignClientConfig.class)
public interface AccountServiceClient {
    @GetMapping("/department")
    List<AccountInfoDto> getAccountInfosByDepartmentId(@RequestParam int departmentId);
}
