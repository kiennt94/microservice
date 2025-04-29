package vti.departmentservice.response.department;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentInfoDto {
    int departmentId;
    String departmentName;
}