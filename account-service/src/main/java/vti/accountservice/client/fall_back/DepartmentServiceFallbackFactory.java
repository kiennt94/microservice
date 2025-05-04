package vti.accountservice.client.fall_back;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import vti.accountservice.client.DepartmentServiceClient;
import vti.accountservice.response.dto.department.DepartmentInfoDto;
import vti.common.exception_handler.NotFoundException;
import vti.common.exception_handler.ServiceUnavailableException;
import vti.common.utils.ConstantUtils;
import vti.common.utils.MessageUtil;

import java.util.List;

@Component
public class DepartmentServiceFallbackFactory implements FallbackFactory<DepartmentServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceFallbackFactory.class);

    @Override
    public DepartmentServiceClient create(Throwable cause) {
        return new DepartmentServiceClient() {
            @Override
            public DepartmentInfoDto getDepartmentById(Integer id) {
                logger.warn("Fallback activated due to: {}", cause.toString());
                if (cause instanceof FeignException.NotFound) {
                    throw new NotFoundException(MessageUtil.getMessage(ConstantUtils.DEPARTMENT_ID_NOT_EXISTS));
                }
                throw new ServiceUnavailableException("Department service error: " + cause.getMessage(), "/api/department/" + id);
            }

            @Override
            public List<DepartmentInfoDto> getDepartmentsByIds(List<Integer> ids) {
                if (cause instanceof FeignException.NotFound) {
                    throw new NotFoundException();
                }
                throw new ServiceUnavailableException("Department service error: " + cause.getMessage(), "/api/department/by-ids");
            }
        };
    }
}

