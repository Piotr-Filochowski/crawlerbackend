package com.filochowski.crawlerbackend.auth.repository;

import com.filochowski.crawlerbackend.auth.config.model.BasicUserDetails;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMemoryRepository implements UserRepository {

  // INFO: tutaj robimy sobie brzydkie repozytorium pamieciowe

  private Map<String, BasicUserDetails> USERS = new HashMap<>();

  @Override
  public void addUser( BasicUserDetails userDetails) {
    USERS.put(userDetails.getUsername(), userDetails);
  }

  @Override
  public BasicUserDetails findByUserame(String username) {
    return USERS.get(username);
  }
}
