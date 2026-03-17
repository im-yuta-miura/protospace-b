package in.tech_camp.protospace_b.service;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.event.UserRegisteredEvent;
import in.tech_camp.protospace_b.exception.EmailAlreadyExistsException;
import in.tech_camp.protospace_b.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /** アプリケーションイベントを発行するためのパブリッシャー */
  private final ApplicationEventPublisher publisher;

  public UserEntity registerUser(UserEntity userInput) throws EmailAlreadyExistsException {

    if(userRepository.existsByEmail(userInput.getEmail())) {
      throw new EmailAlreadyExistsException("メールアドレスが重複しています。");
    }

    String hashedPassword = passwordEncoder.encode(userInput.getPassword());

    UserEntity user = new UserEntity();
    BeanUtils.copyProperties(userInput, user); // 引数のオブジェクトをコピー
    user.setPassword(hashedPassword);

    userRepository.insert(user);

    publisher.publishEvent(new UserRegisteredEvent(user));

    return user;
  }

  public UserEntity findUserById(Integer id) {

    return userRepository.findById(id);
  }
  
}
