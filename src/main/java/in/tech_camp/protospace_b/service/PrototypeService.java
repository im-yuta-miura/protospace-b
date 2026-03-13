package in.tech_camp.protospace_b.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PrototypeService {
  
  private final PrototypeRepository prototypeRepository;

  public List<PrototypeEntity> getPrototypeByUserId(Integer userId) {
    
    List<PrototypeEntity> prototypes = prototypeRepository.getByUserId(userId);

    return prototypes;
  }
}
