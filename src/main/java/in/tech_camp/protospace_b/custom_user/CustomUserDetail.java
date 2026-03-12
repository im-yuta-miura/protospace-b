package in.tech_camp.protospace_b.custom_user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetail implements UserDetails {

  private final CustomUser user;

  private CustomUserDetail(CustomUser customUser) {
    this.user = customUser;
  }
  public Integer getId() {
    return user.id();
  }
  
  public String getName() {
    return user.name();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return user.password();
  }

  @Override
  public String getUsername() {
    return user.email();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static CustomUserDetail createFrom(CustomUser user) {
    return new CustomUserDetail(user);
  }
}