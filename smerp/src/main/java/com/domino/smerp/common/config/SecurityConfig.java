package com.domino.smerp.common.config;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(customizer ->
                customizer.configurationSource(getCorsConfigurationSource()))
            .authorizeHttpRequests(
                auth -> auth.requestMatchers("/api/v1/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .sessionManagement(sessionManagement -> sessionManagement.maximumSessions(1));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    private static CorsConfigurationSource getCorsConfigurationSource() {

        return (request) -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();

            corsConfiguration.setAllowedOriginPatterns(List.of("*"));

            corsConfiguration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));

            corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

            corsConfiguration.setExposedHeaders(List.of("Authorization"));

            corsConfiguration.setAllowCredentials(true);

            corsConfiguration.setMaxAge(3600L);

            return corsConfiguration;
        };
    }
}
