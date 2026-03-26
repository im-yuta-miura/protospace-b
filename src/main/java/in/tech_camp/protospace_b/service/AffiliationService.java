package in.tech_camp.protospace_b.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.AffiliationEntity;
import in.tech_camp.protospace_b.exception.AffiliationAlreadyExistsException;
import in.tech_camp.protospace_b.repository.AffiliationRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AffiliationService {

  private final AffiliationRepository affiliationRepository;

  public AffiliationEntity getAffiliation(Integer id) {
    return affiliationRepository.findById(id);
  }

  public List<AffiliationEntity> getAffiliationOptions () {
    
    return affiliationRepository.findAll();
  }

  public AffiliationEntity createAffiliations(String requestAffiliation) throws AffiliationAlreadyExistsException {

    if(affiliationRepository.existsByAffiliation(requestAffiliation)) {
      throw new AffiliationAlreadyExistsException("所属は既に存在します。");
    }

    var affiliation = AffiliationEntity.builder()
        .affiliation(requestAffiliation)
        .build();
    affiliationRepository.insert(affiliation);

    return affiliation;
  }
  
}
