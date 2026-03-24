package in.tech_camp.protospace_b.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.PositionEntity;
import in.tech_camp.protospace_b.exception.PositionAlreadyExistsException;
import in.tech_camp.protospace_b.repository.PositionRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PositionService {

  private final PositionRepository positionRepository;

  
  public List<PositionEntity> getPositionOptions () {
    
    return positionRepository.findAll();
  }

  public PositionEntity createPositions(String requestPosition) throws PositionAlreadyExistsException {

    if(positionRepository.existsByPosition(requestPosition)) {
      throw new PositionAlreadyExistsException("所属は既に存在します。");
    }

    var position = PositionEntity.builder()
        .position(requestPosition)
        .build();
    positionRepository.insert(position);

    return position;
  }
}
