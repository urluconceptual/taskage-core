package com.taskage.core.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
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

    @Value("${cors.allowedOrigins}")
    private String ALLOWED_ORIGIN;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers ->
                        headers.xssProtection(
                                xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                        ).contentSecurityPolicy(
                                cps -> cps.policyDirectives("script-src 'self'")
                        ))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/core/users/login").permitAll()
                        .requestMatchers("/core/users/register").hasRole(ADMIN)
                        .requestMatchers("/core/users/checkLocalCredentials").hasAnyRole(ADMIN, BASIC, MANAGER)
                        .requestMatchers("/core/users/delete/**").hasAnyRole(ADMIN)
                        .requestMatchers("/core/users/update").hasAnyRole(ADMIN)
                        .requestMatchers("/core/users/getAll").hasAnyRole(ADMIN, BASIC, MANAGER)
                        .requestMatchers("/core/users/getAllForTeam/**").hasAnyRole(ADMIN, BASIC, MANAGER)
                        .requestMatchers("/core/teams/create").hasAnyRole(ADMIN)
                        .requestMatchers("/core/teams/getAll").hasAnyRole(ADMIN, BASIC, MANAGER)
                        .requestMatchers("/core/teams/update").hasAnyRole(ADMIN)
                        .requestMatchers("/core/teams/delete/**").hasAnyRole(ADMIN)
                        .requestMatchers("/core/jobTitles/getAll").hasAnyRole(ADMIN)
                        .requestMatchers("/core/sprints/**").hasAnyRole(MANAGER, BASIC)
                        .requestMatchers("/core/dictionary/**").hasAnyRole(MANAGER, BASIC)
                        .requestMatchers("/core/taskTypes/**").hasAnyRole(MANAGER, BASIC)
                        .requestMatchers("/core/tasks/**").hasAnyRole(BASIC, MANAGER)
                        .requestMatchers("/core/ws/**").permitAll()
                        .requestMatchers("core/admin/**").hasRole(ADMIN))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter,
                        org.springframework.security.web.access.intercept.AuthorizationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(ALLOWED_ORIGIN));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Upgrade",
                "Connection", "Origin", "Sec-WebSocket-Version", "Sec-WebSocket-Extensions", "Sec-WebSocket-Key"));
        configuration.addExposedHeader("Upgrade");
        configuration.addExposedHeader("Connection");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
