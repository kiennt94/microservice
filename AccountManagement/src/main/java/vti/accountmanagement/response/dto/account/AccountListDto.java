package vti.accountmanagement.response.dto.account;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vti.common.enums.Role;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountListDto {
    int accountId;
    String email;
    String username;
    String fullName;
    LocalDateTime createDate;
    String departmentName;
    String positionName;
    Role role;
}
