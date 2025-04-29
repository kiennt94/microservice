package vti.common.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AccountDto {
    int accountId;
    String username;
    String password;
    String fullName;
    List<String> authorities; // List of String only
}
