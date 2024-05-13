package com.taskage.core.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    public static final String ADMIN = "ADMIN";
    public static final String BASIC = "BASIC";
    public static final String MANAGER = "MANAGER";

    private final AuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/users/register").hasRole(ADMIN)
                        .requestMatchers("/users/checkLocalCredentials").hasAnyRole(ADMIN, BASIC, MANAGER)
                        .requestMatchers("/users/delete/**").hasAnyRole(ADMIN)
                        .requestMatchers("users/update").hasAnyRole(ADMIN)
                        .requestMatchers("/users/getAll").hasAnyRole(ADMIN, BASIC, MANAGER)
                        .requestMatchers("/users/getAllForTeam/**").hasAnyRole(ADMIN, BASIC, MANAGER)
                        .requestMatchers("/teams/create").hasAnyRole(ADMIN)
                        .requestMatchers("/teams/getAll").hasAnyRole(ADMIN, BASIC, MANAGER)
                        .requestMatchers("/teams/update").hasAnyRole(ADMIN)
                        .requestMatchers("/teams/delete/**").hasAnyRole(ADMIN)
                        .requestMatchers("/jobTitles/getAll").hasAnyRole(ADMIN)
                        .requestMatchers("/sprints/**").hasAnyRole(MANAGER, BASIC)
                        .requestMatchers("/dictionary/**").hasAnyRole(MANAGER, BASIC)
                        .requestMatchers("/tasks/**").hasAnyRole(BASIC, MANAGER))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter,
                        org.springframework.security.web.access.intercept.AuthorizationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
