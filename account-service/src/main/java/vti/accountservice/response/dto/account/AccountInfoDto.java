package vti.accountservice.response.dto.account;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vti.common.enums.PositionName;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoDto {
    int accountId;
    String username;
    String email;
    String fullName;
    LocalDateTime createDate;
    int departmentId;
    String departmentName;
    int positionId;
    PositionName positionName;
}
