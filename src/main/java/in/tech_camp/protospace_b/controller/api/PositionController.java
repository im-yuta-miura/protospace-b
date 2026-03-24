package in.tech_camp.protospace_b.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.protospace_b.exception.PositionAlreadyExistsException;
import in.tech_camp.protospace_b.service.PositionService;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/positions")
@AllArgsConstructor
public class PositionController {
  
  private final PositionService positionService;

  @PostMapping("/")
  public ResponseEntity<Void> createPositions(
      @RequestBody String requestPosition
  ) {
    
    try {
      positionService.createPositions(requestPosition);

    } catch (PositionAlreadyExistsException e) {

      return ResponseEntity
          .status(HttpStatus.CONFLICT) // 既に同名が存在するHTTPステータスコード
          .build();
    }

    return ResponseEntity.ok()
        .build();
  }
}
