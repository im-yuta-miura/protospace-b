package in.tech_camp.protospace_b.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import in.tech_camp.protospace_b.custom_user.CustomUser;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.event.UserRegisteredEvent;
import in.tech_camp.protospace_b.service.AuthenticationService;
import lombok.AllArgsConstructor;

/**
 * ユーザー登録関連のイベントを監視するリスナークラス。
 */
@Component
@AllArgsConstructor
public class UserRegisteredListener {

  private final AuthenticationService authenticationService;

  @EventListener
  public void handleUserRegisteredEvent(UserRegisteredEvent event) {
    System.out.println("\tパブリッシャーから呼び出し");
    // RequestとResponseをコンテキストから取得
    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    
    // Attributesがnullであれば終了
    if(attr == null) return; 

    UserEntity userEntity = event.user();

    // 認証に利用するCustomUserレコードを生成
    CustomUserDetail customUser = CustomUserDetail.createFrom(CustomUser.fromEntity(userEntity));

    // サービスを呼び出してログイン処理を実行
    authenticationService.login(
      customUser, 
      attr.getRequest(), 
      attr.getResponse()
    );
  }
}