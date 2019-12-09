package com.filochowski.crawlerbackend.auth.repository;

import com.filochowski.crawlerbackend.auth.config.model.BasicUserDetails;

public interface UserRepository {

  // INFO: tutaj robimy sobie interfejs dla prostego repozytorium uzytkownikow
  // Niezaleznie czy robimy repozytorium pamieciowe, bazodanowe czy plikowe, wystarczy ze bedzie spelniac wymogi tego interfejsu
  // Wiecej mozliwosci takiego podejscia pokaze w przykladzie step6e_custom

  void addUser(BasicUserDetails userDetails);

  BasicUserDetails findByUserame(String username);
}
