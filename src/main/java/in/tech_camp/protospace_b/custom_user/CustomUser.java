package in.tech_camp.protospace_b.custom_user;

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

}
