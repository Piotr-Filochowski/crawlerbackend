package com.filochowski.crawlerbackend.auth.service;

import com.filochowski.crawlerbackend.auth.model.AuthData;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;

  public void login(AuthData authData) {
    // INFO: w tym przykladzie jest to jedyne miejsce gdzie dzieje sie jakakolwiek magia
    // Najpierw nastepuje autentykacja uzytkownika przez authenticationManager'a
    // Uzywajac zdefiniowanego w SecurityConfig PasswordEncodera i implementacji UserDetailsService uzytkownik jest pobierany z bazy i jest walidowane jego haslo, a takze inne cechy konta
    Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authData.getLogin(), authData.getPassword()));
    // Nastepnie pobrany uzytkownik jest umieszczany w SecurityContext.
    // Dzieki ustawieniu SessionCreationPolicy na ALWAYS po zakonczeniu requestu nie zostanie on usuniety z sesji
    // Dlatego przy uderzeniu do nastepnego endpointa, nie musimy przekazywac zadnego tokena ani credentiali
    // JSESSIONID jest wystarczajacy zeby powiazac sesje na serwerze z instancja przegladarki, z ktorej uzytkownik sie dobija
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public void logout (HttpServletRequest request, HttpServletResponse response) {
    // INFO: wylogowanie uzytkownika jest dosyc analogiczne, polegajac na automatycznym kojarzeniu sesji wystarczy jedynie wywolac wewnetrzna procedure logouta, ktora czysci sesje
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
  }
}
