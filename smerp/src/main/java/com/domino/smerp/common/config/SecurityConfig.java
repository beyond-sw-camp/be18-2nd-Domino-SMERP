package com.domino.smerp.common.config;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(customizer ->
                customizer.configurationSource(getCorsConfigurationSource()))
            .authorizeHttpRequests(
                auth -> auth.requestMatchers("/api/v1/auth/login", "/api/v1/users")
                            .permitAll()
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/**")
//                            .hasRole("ADMIN")
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
            // CorsConfiguration 객체를 생성해서 CORS 설정을 한다.
            CorsConfiguration corsConfiguration = new CorsConfiguration();

            // CORS 요청에서 허용할 출처를 지정한다.
            // corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174"));
            corsConfiguration.setAllowedOriginPatterns(List.of("*"));

            // CORS 요청에서 허용할 HTTP 메소드를 지정한다.
            corsConfiguration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

            // 클라이언트가 요청 시 사용할 수 있는 헤더를 지정한다.
            corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

            // 클라이언트가 응답에서 접근할 수 있는 헤더를 지정한다.
            corsConfiguration.setExposedHeaders(List.of("Authorization"));

            // 자격 증명(쿠키, 세션) 허용 여부를 설정한다.
            corsConfiguration.setAllowCredentials(true);

            // CORS Preflight 요청을 브라우저가 캐싱하는 시간(초 단위)을 설정한다.
            corsConfiguration.setMaxAge(3600L);

            return corsConfiguration;
        };
    }
}
