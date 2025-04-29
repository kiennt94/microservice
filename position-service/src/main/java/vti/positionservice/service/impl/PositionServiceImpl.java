package vti.positionservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vti.common.payload.PageResponse;
import vti.common.utils.ObjectMapperUtils;
import vti.positionservice.model.Position;
import vti.positionservice.repository.PositionRepository;
import vti.positionservice.response.PositionListDto;
import vti.positionservice.service.PositionService;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();

    @Override
    public PageResponse<PositionListDto> getAll(Pageable pageable, String search) {
        Page<Position> positions = positionRepository.findAll(pageable,search);
        return new PageResponse<>(objectMapperUtils.mapEntityPageIntoDtoPage(positions, PositionListDto.class));
    }
}
