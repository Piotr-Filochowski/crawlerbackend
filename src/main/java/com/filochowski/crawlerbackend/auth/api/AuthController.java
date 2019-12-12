package com.filochowski.crawlerbackend.auth.api;

import com.filochowski.crawlerbackend.auth.model.AuthData;
import com.filochowski.crawlerbackend.auth.service.AuthService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PreAuthorize("permitAll()")
  @PostMapping("/login")
  public ResponseEntity login(@RequestBody AuthData authData) {
    authService.login(authData);
    return new ResponseEntity(HttpStatus.OK);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) {

    authService.logout(request, response);
  }
}
