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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("GET");
    configuration.addAllowedMethod("PUT");
    configuration.addAllowedMethod("POST");
    source.registerCorsConfiguration("/**", configuration);
    CorsFilter corsFilter = new CorsFilter(source);

    http
        // INFO: Kontrola tworzenia sesji, dzieki uzyciu NEVER lub STATELESS mamy pewnosc ze Spring nie uzyje automagicznej propagacji sesji poprzez JSESSIONID.
        // My jednak uzyjemy teraz ALWAYS, bo mechanizm autentykacji i autoryzacji w tym przykladzie polega wlasnie na JSESSIONID
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
        .addFilterBefore(corsFilter, LogoutFilter.class)
        // INFO: kilka zabezpieczen
        .headers() // konfiguruje zabezpieczenia oparte o headery
        .cacheControl().and() // wylacza cachowanie
        .frameOptions().deny() // uniemozliwienie dostepu z ramek (np iFrame)
        .xssProtection().xssProtectionEnabled(true).and() // ochrona przed atakami XSS
        .contentTypeOptions().and() // zabezpieczenie przed sniffingiem
        .and()
        .csrf().disable() // bez tego nie bedzie dzialac uzywanie czegokolwiek poza GETami z plikow .http
    ;
  }
}
