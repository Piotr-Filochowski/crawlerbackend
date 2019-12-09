package com.filochowski.crawlerbackend.auth.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class TestController {

  // INFO: anotacje @PreAuthorize sa jednym z 2 najwygodniejszych sposobow na ograniczanie dostepu do endpointow

  @PreAuthorize("permitAll()")
  @GetMapping({"/openService"})
  public String openService() {
    return "openService success";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping({"/authenticatedService"})
  public String authenticatedService() {
    return "authenticatedService success";
  }

  // INFO: Spring ma cos takiego jak Spring EL (Expression Language) pozwalajacy na ponizsze konstrukcje.
  // Zauwazcie, ze nazwy permissionow sa tutaj stringami, a nie odwolaniami do enuma.
  // Jest tak poniewaz adnotacje wymagaja constantow (stale w momencie kompilacji),
  // dostep do nazwy pola w enumie jest wywolaniem metody (.name()), wiec nie jest constantem
  // Wiecej info na temat Spring EL i adnotacji prePost - https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FULL_ACCESS')")
  @GetMapping({"/adminService"})
  public String adminService() {
    return "adminService success";
  }

  @PreAuthorize("hasAuthority('USER') or hasAuthority('FULL_ACCESS')")
  @GetMapping({"/userService"})
  public String userService() {
    return "userService success";
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') or hasAuthority('FULL_ACCESS')")
  @GetMapping({"/commonService"})
  public String commonService() {
    return "commonService success";
  }

  @GetMapping({"/unexpectedService"})
  public String unexpectedService() {
    return "unexpectedService success";
  }
}
