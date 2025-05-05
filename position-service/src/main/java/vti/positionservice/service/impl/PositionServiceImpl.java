package vti.positionservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vti.common.exception_handler.NotFoundException;
import vti.common.payload.PageResponse;
import vti.common.utils.ConstantUtils;
import vti.common.utils.MessageUtil;
import vti.common.utils.ObjectMapperUtils;
import vti.positionservice.model.Position;
import vti.positionservice.repository.PositionRepository;
import vti.positionservice.response.PositionInfoDto;
import vti.positionservice.response.PositionListDto;
import vti.positionservice.service.PositionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final ObjectMapperUtils objectMapperUtils = new ObjectMapperUtils();
    private final MessageUtil messageUtil;

    @Override
    public PageResponse<PositionListDto> getAll(Pageable pageable, String search) {
        Page<Position> positions = positionRepository.findAll(pageable, search);
        return new PageResponse<>(objectMapperUtils.mapEntityPageIntoDtoPage(positions, PositionListDto.class));
    }

    @Override
    public List<PositionInfoDto> getPositionsByIds(List<Integer> ids) {
        return positionRepository.findAllById(ids)
                .stream()
                .map(pos -> PositionInfoDto.builder()
                        .positionId(pos.getPositionId())
                        .positionName(pos.getPositionName())
                        .build())
                .toList();
    }

    @Override
    public PositionInfoDto getPositionById(Integer id) {
        Position position = positionRepository.findById(id).orElse(null);
        if (position == null) {
            throw new NotFoundException(messageUtil.getMessage(ConstantUtils.POSITION_ID_NOT_EXISTS));
        }
        return objectMapperUtils.map(position, PositionInfoDto.class);
    }
}
