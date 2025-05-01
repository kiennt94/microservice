package vti.positionservice.response;

import lombok.*;
import vti.common.enums.PositionName;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionInfoDto {
    int positionId;
    PositionName positionName;
}
