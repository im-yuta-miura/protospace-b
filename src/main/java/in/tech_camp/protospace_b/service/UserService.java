package in.tech_camp.protospace_b.service;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.exception.EmailAlreadyExistsException;
import in.tech_camp.protospace_b.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserEntity registerUser(UserEntity userInput) throws EmailAlreadyExistsException {

    if(userRepository.existsByEmail(userInput.getEmail())) {
      throw new EmailAlreadyExistsException("メールアドレスが重複しています。");
    }

    String hashedPassword = passwordEncoder.encode(userInput.getPassword());

    UserEntity user = new UserEntity();
    BeanUtils.copyProperties(userInput, user); // 引数のオブジェクトをコピー
    user.setPassword(hashedPassword);

    userRepository.insert(user);

    return user;
  }

  public UserEntity findUserById(Integer id) {

    // 中身がnullかもしれないことを呼び出し元に伝えるため、Optionalで包む
    UserEntity user = userRepository.findById(id);

    return user;
  }
}
