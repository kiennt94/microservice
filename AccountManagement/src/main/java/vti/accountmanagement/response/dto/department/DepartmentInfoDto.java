package vti.accountmanagement.response.dto.department;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentInfoDto {
    int departmentId;
    String departmentName;
}
