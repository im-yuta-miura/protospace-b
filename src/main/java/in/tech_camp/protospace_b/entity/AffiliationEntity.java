package in.tech_camp.protospace_b.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AffiliationEntity {
  private Integer id;
  private String affiliation;
}