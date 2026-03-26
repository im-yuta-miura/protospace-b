package in.tech_camp.protospace_b.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  private Integer id;
  private String name;
  private String profile;
  private Integer affiliationId;
  private Integer positionId;
  private String email;
  private String password;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<PrototypeEntity> prototypes;
  private List<CommentEntity> comments;
}