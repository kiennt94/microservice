package vti.departmentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vti.common.exception_handler.NotFoundException;
import vti.common.exception_handler.DuplicateException;
import vti.departmentservice.client.AccountClient;
import vti.departmentservice.model.Department;
import vti.common.payload.PageResponse;
import vti.departmentservice.repository.DepartmentRepository;
import vti.departmentservice.request.department.DepartmentCreateRequest;
import vti.departmentservice.request.department.DepartmentUpdateRequest;
import vti.departmentservice.response.account.AccountInfoDto;
import vti.departmentservice.response.department.DepartmentInfoDto;
import vti.departmentservice.response.department.DepartmentListDto;
import vti.departmentservice.service.DepartmentService;
import vti.common.utils.MessageUtil;
import vti.common.utils.ObjectMapperUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final MessageSource messageSource;
    private final ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();
    private static final String DEPARTMENT_ID_NOT_EXISTS = "department.id.not.exists";
    private static final String DEPARTMENT_NAME_EXISTS = "department.name.exists";
    private static final String DEPARTMENT_ID_EXISTS_IN_ACCOUNT = "department.id.exists.reference.key.account";

    private final AccountClient accountClient;

    @Override
    public PageResponse<DepartmentListDto> getAll(Pageable pageable, String search) {
        Page<Department> departments = departmentRepository.findAll(pageable, search);
        return new PageResponse<>(objectMapperUtils.mapEntityPageIntoDtoPage(departments, DepartmentListDto.class));
    }

    @Override
    public void save(DepartmentCreateRequest department) {
        if (departmentRepository.findByDepartmentName(department.getDepartmentName()) == null) {
            Department dep = objectMapperUtils.map(department, Department.class);
            departmentRepository.save(dep);
        } else {
            throw new DuplicateException(MessageUtil.getMessage(DEPARTMENT_NAME_EXISTS));
        }
    }

    @Override
    public void update(DepartmentUpdateRequest department) {
        Department dep = departmentRepository.findById(department.getDepartmentId()).orElse(null);
        if (dep == null) {
            throw new NotFoundException(MessageUtil.getMessage(DEPARTMENT_ID_NOT_EXISTS));
        }
        if (departmentRepository.findByDepartmentNameAndDepartmentIdNot(department.getDepartmentName(), department.getDepartmentId()) == null) {
            dep = objectMapperUtils.map(department, Department.class);
            departmentRepository.save(dep);
        } else {
            throw new DuplicateException(MessageUtil.getMessage(DEPARTMENT_NAME_EXISTS));
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Department department = departmentRepository.findByDepartmentId(id);
        if (department == null) {
            throw new NotFoundException(MessageUtil.getMessage(DEPARTMENT_ID_NOT_EXISTS));
        }

        List<AccountInfoDto> accountInfos = accountClient.getAccountInfos(id);
        if (accountInfos != null && !accountInfos.isEmpty()) {
            throw new NotFoundException(MessageUtil.getMessage(DEPARTMENT_ID_EXISTS_IN_ACCOUNT));
        }
        departmentRepository.delete(department);
    }

    @Override
    public List<DepartmentInfoDto> getDepartmentsByIds(List<Integer> ids) {
        return departmentRepository.findAllById(ids)
                .stream()
                .map(dept -> DepartmentInfoDto.builder()
                        .departmentId(dept.getDepartmentId())
                        .departmentName(dept.getDepartmentName())
                        .build())
                .toList();
    }
}
