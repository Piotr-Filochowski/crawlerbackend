package com.filochowski.crawlerbackend.auth.config;

import com.filochowski.crawlerbackend.auth.config.service.BasicUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // INFO: aktywacja anotacji uzywanych w Controllerach
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final BasicUserDetailsService userDetailsService;

  // INFO: konfigurujemy authentication managera - narzedzie odpowiedzialne za duza czesc procesu autentykacji
  // 2 podstawowy rzeczy ktore musimy mu zdefiniowac to:
  // - user details service - odpowiedzialny za wczytywanie usera po loginie
  // - password encoder - odpowiedzialny za szyfrowanie przy porownaniu z baza
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  // INFO: konfiguracja password encodera - poza uzyciem przy konfiguracji auth managera, jest takze uzywany przy zapisie userow do bazy
  // wybralismy najbardziej standardowy encoderow dostepnych w Spring Security
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // INFO: potrzebujemy jakiejkolwiek deklaracji beana dla AuthenticationManager, zebysmy mogli uzyc go w AuthService
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
        .addFilterBefore(new CorsFilter(), LogoutFilter.class)
        .headers()
        .cacheControl().and()
        .frameOptions().deny()
        .xssProtection().xssProtectionEnabled(true).and()
        .contentTypeOptions().and()
        .and()
        .csrf().disable()
    ;
  }
}
