package vti.positionservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vti.common.payload.PageResponse;
import vti.positionservice.response.PositionListDto;

@Service
public interface PositionService {
    PageResponse<PositionListDto> getAll(Pageable pageable, String search);
}
