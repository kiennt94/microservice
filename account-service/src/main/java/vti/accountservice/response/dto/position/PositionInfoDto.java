package vti.accountservice.response.dto.position;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vti.common.enums.PositionName;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class PositionInfoDto {
    private int positionId;
    private PositionName positionName;
}
