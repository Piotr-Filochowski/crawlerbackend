package com.filochowski.crawlerbackend.auth.config.model;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
@Getter
public class BasicUserDetails implements UserDetails {

  // INFO: tutaj mamy klase reprezentujaca uzytkownika, na ktorej operuje spring security (mozemy ja dowolnie rozszerzyc)

  private final List<GrantedAuthority> authorities;
  private final String password;
  private final String username;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;
}
