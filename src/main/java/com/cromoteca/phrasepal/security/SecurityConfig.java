package com.cromoteca.phrasepal.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import com.cromoteca.phrasepal.user.UserRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

  @Autowired
  private UserRepository userRepository;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Set default security policy that permits Hilla internal requests and
    // denies all other
    // http.authorizeHttpRequests(registry -> registry.requestMatchers(
    //         routeUtil::isRouteAllowed).permitAll());
    super.configure(http);
    // use a custom login view and redirect to root on logout
    setLoginView(http, "/login", "/");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userRepository.findByEmail(username)
        .map(user -> (UserDetails) user)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }
}
