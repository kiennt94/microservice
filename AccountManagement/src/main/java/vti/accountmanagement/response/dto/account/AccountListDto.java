package vti.accountmanagement.response.dto.account;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vti.common.enums.PositionName;
import vti.common.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountListDto {
    int accountId;
    String username;
    String email;
    String fullName;
    LocalDateTime createDate;
    Role role;
    int departmentId;
    String departmentName;
    int positionId;
    PositionName positionName;
}
