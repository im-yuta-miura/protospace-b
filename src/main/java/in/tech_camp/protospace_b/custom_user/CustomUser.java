package in.tech_camp.protospace_b.custom_user;

import in.tech_camp.protospace_b.entity.UserEntity;
import lombok.Builder;

@Builder
public record CustomUser(
  Integer id,
  String name,
  String profile,
  String affiliation,
  String position,
  String email,
  String password
) {
  public static CustomUser fromEntity(UserEntity entity) {
    return CustomUser.builder()
      .id(entity.getId())
      .name(entity.getName())
      .profile(entity.getProfile())
      .affiliation(entity.getAffiliation())
      .position(entity.getPosition())
      .email(entity.getEmail())
      .password(entity.getPassword())
      .build();
}
}
