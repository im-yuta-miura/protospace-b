package in.tech_camp.protospace_b.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.protospace_b.exception.AffiliationAlreadyExistsException;
import in.tech_camp.protospace_b.service.AffiliationService;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/affiliations")
@AllArgsConstructor
public class AffiliationController {
  
  private final AffiliationService affiliationService;

  @PostMapping("/")
  public ResponseEntity<Void> createAffiliations(
      @RequestBody String requestAffiliation
  ) {
    
    try {
      affiliationService.createAffiliations(requestAffiliation);

    } catch (AffiliationAlreadyExistsException e) {

      return ResponseEntity
          .status(HttpStatus.CONFLICT) // 既に同名が存在するHTTPステータスコード
          .build();
    }

    return ResponseEntity.ok()
        .build();
  }
}
