package vti.positionservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vti.common.enums.PositionName;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionListDto {
    int positionId;
    PositionName positionName;
}
