package in.tech_camp.protospace_b.custom_user;

import in.tech_camp.protospace_b.entity.UserEntity;
import lombok.Builder;

@Builder
public record CustomUser(
  Integer id,
  String name,
  String profile,
  Integer affiliationId,
  Integer positionId,
  String email,
  String password
) {
  public static CustomUser fromEntity(UserEntity entity) {
    return CustomUser.builder()
      .id(entity.getId())
      .name(entity.getName())
      .profile(entity.getProfile())
      .affiliationId(entity.getAffiliationId())
      .positionId(entity.getPositionId())
      .email(entity.getEmail())
      .password(entity.getPassword())
      .build();
}
}
