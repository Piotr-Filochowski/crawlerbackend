package com.filochowski.crawlerbackend.auth.config.service;

import com.filochowski.crawlerbackend.auth.config.model.BasicUserDetails;
import com.filochowski.crawlerbackend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  // INFO: w tym serwisie musimy podac sposob znajdowania uzytkownika po lognie
  // Wynik jest uzywany zarowno w wewnetrznych mechanizmach jak i moze byc uzyty przez nas (wiecej w przykladzie ste6c_session)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    BasicUserDetails res = userRepository.findByUserame(username);
    if (res == null) {
      throw new UsernameNotFoundException("User " + username + " not found");
    }
    return res;
  }
}
