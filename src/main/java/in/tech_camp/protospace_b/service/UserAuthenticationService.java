package in.tech_camp.protospace_b.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.custom_user.CustomUser;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(email);
    if (userEntity == null) {
      throw new UsernameNotFoundException("ユーザーが見つかりませんでした。: " + email);
    }

    return CustomUserDetail.createFrom(CustomUser.builder()
      .id(userEntity.getId())
      .name(userEntity.getName())
      .profile(userEntity.getProfile())
      .affiliation(userEntity.getAffiliation())
      .position(userEntity.getPosition())
      .email(userEntity.getEmail())
      .password(userEntity.getPassword())
      .build()
    );
  }
}