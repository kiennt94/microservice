package vti.positionservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vti.common.payload.PageResponse;
import vti.positionservice.response.PositionInfoDto;
import vti.positionservice.response.PositionListDto;

import java.util.List;

@Service
public interface PositionService {
    PageResponse<PositionListDto> getAll(Pageable pageable, String search);
    List<PositionInfoDto> getPositionsByIds(List<Integer> ids);
}
