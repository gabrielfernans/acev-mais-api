package com.acev.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  @Autowired
  SecurityFilter securityFilter;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
          .csrf(csrf -> csrf.disable())
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/authentication").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/agrupes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/agrupes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/agrupes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/persons").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/persons").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/persons").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/ministries").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/ministries").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/ministries").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/lessons").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/lessons").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/lessons").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/series").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/series").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/series").hasRole("ADMIN")
                .anyRequest().authenticated()
          )
          .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
          .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*", "https://acev-web.vercel.app", "http://localhost:4200"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
