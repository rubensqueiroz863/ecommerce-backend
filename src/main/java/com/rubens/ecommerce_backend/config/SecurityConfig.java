package com.rubens.ecommerce_backend.config;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  
  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .csrf(csrf -> csrf.disable())
          .headers(headers -> headers.frameOptions(frame -> frame.disable()))
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(
                "/auth/**",
                "/events/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html"
              ).permitAll()
              .anyRequest().authenticated()
          )
          .exceptionHandling(e -> e
              .authenticationEntryPoint((request, response, authException) -> {
                  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                  response.getWriter().write("Unauthorized");
              })
          )
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of(
          "/*",
          "http://localhost:3000",
          "http://10.0.0.36:4200/",
          "https://rubensinternetbanking.vercel.app",
          "https://internet-banking-git-main-rubens-projects-1b4c900a.vercel.app",
          "https://internet-banking-kdozt76w7-rubens-projects-1b4c900a.vercel.app"
      ));
      configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
      configuration.setAllowCredentials(true);

      // ✅ Importante: Authorization precisa estar aqui
      configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
      configuration.setExposedHeaders(List.of("Authorization"));
      configuration.setMaxAge(3600L);

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}