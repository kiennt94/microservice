package vti.accountservice.request.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import vti.common.anotation.FormatWhiteSpace;
import vti.common.anotation.Trim;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountCreateRequest {

    @Trim
    @Pattern(
            regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$",
            message = "{account.email.invalid}"
    )
    @Schema(description = "Email address of the account", example = "example@example.com")
    String email;

    @NotNull(message = "{account.username.required}")
    @NotBlank(message = "{account.username.required}")
    @Length(max = 20, message = "{account.username.length}")
    @Trim
    @Schema(description = "Username, max 20 characters", example = "example")
    String username;

    @NotNull(message = "{account.fullName.required}")
    @NotBlank(message = "{account.fullName.required}")
    @Length(max = 50, message = "{account.fullName.length}")
    @FormatWhiteSpace
    @Schema(description = "Full name, max 50 characters", example = "Peter Pain")
    String fullName;

    @NotNull(message = "{account.password.required}")
    @NotBlank(message = "{account.password.required}")
    @Length(min = 6, max = 20, message = "{account.password.length}")
    @Schema(description = "Password with 6-20 characters", example = "securePass123")
    String password;

    @Schema(description = "Department ID that the account belongs to", example = "1")
    int departmentId;

    @Schema(description = "Position ID that the account holds", example = "2")
    int positionId;
}
