package vti.accountservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vti.accountservice.response.dto.position.PositionInfoDto;
import vti.commonservice.config.FeignClientConfig;

import java.util.List;

@FeignClient(name = "position-service", path = "/api/position", configuration = FeignClientConfig.class)
public interface PositionServiceClient {

    @PostMapping("/by-ids")
    List<PositionInfoDto> getPositionsByIds(@RequestBody List<Integer> ids);

    @GetMapping("/{id}")
    PositionInfoDto getPositionById(@PathVariable Integer id);
}
