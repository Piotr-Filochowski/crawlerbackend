package com.filochowski.crawlerbackend.auth;

import com.filochowski.crawlerbackend.auth.config.model.BasicUserDetails;
import com.filochowski.crawlerbackend.auth.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostConstructService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // INFO: tutaj tworzymy nasza baze userow
  // Istotne jest, zeby hasla, ktore zapisujemy byly zaszyfrowane (zarowno zeby dalo sie je pozniej porownac przy logowaniu, jak i z powodow bezpieczenstwa, wiecej w step6c_session)

  @PostConstruct
  private void init() {
    // INFO: mozecie pomanipulowac polami boolowskimi i zobaczyc efekty
    userRepository.addUser(new BasicUserDetails(Arrays.asList(new SimpleGrantedAuthority("ADMIN")), passwordEncoder.encode("Password1"), "User1", true, true, true, true));
    userRepository.addUser(new BasicUserDetails(Arrays.asList(new SimpleGrantedAuthority("FULL_ACCESS")), passwordEncoder.encode("Password2"), "User2", true, true, true, true));
    userRepository.addUser(new BasicUserDetails(Arrays.asList(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER")), passwordEncoder.encode("Password3"), "User3", true, true, true, true));
    userRepository.addUser(new BasicUserDetails(Arrays.asList(new SimpleGrantedAuthority("FULL_ACCESS")), passwordEncoder.encode("Password4"), "User4", true, true, true, true));
    userRepository.addUser(new BasicUserDetails(new ArrayList<>(), passwordEncoder.encode("Password5"), "User5", true, true, true, true));
  }
}
