package vti.departmentservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vti.common.payload.PageResponse;
import vti.departmentservice.request.department.DepartmentCreateRequest;
import vti.departmentservice.request.department.DepartmentUpdateRequest;
import vti.departmentservice.response.department.DepartmentInfoDto;
import vti.departmentservice.response.department.DepartmentListDto;

import java.util.List;


@Service
public interface DepartmentService {
    PageResponse<DepartmentListDto> getAll(Pageable pageable, String search);
    void save(DepartmentCreateRequest department);
    void update(DepartmentUpdateRequest department);
    void delete(Integer id);
    List<DepartmentInfoDto> getDepartmentsByIds(List<Integer> ids);
    DepartmentInfoDto getDepartmentById(Integer id);
}
