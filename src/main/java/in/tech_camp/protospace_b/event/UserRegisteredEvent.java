package in.tech_camp.protospace_b.event;

import in.tech_camp.protospace_b.entity.UserEntity;

/** ユーザー登録成功時に発行されるイベント */
public record UserRegisteredEvent(
  UserEntity user
){
}
