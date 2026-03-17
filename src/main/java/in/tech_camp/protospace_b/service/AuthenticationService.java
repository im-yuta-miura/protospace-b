package in.tech_camp.protospace_b.service;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
  
  private final SecurityContextRepository securityContextRepository;
  
  /**
   * 指定されたユーザーで手動ログイン処理（認証コンテキストの作成と保存）を行う。
   */
  public void login(
    CustomUserDetail user,
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    // 1. 認証済みトークンの作成
    // パスワード認証は呼び出し元で完了している前提とし、権限リストは空で「認証済み」として生成
    Authentication auth = UsernamePasswordAuthenticationToken.authenticated(
            user,
            null,
            Collections.emptyList());

    // 2. 新しいセキュリティコンテキストを作成し、認証情報をセット
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);

    // 3. 現在の実行スレッド（SecurityContextHolder）にコンテキストを保持させる
    SecurityContextHolder.setContext(context);
    
    // 4. セッション等の外部ストレージにコンテキストを永続化する
    // これにより、次回以降のリクエストでもログイン状態が継続される
    securityContextRepository.saveContext(context, request, response);
  }
}