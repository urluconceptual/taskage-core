package com.taskage.core.config.security;

import com.taskage.core.exception.CrossOriginException;
import com.taskage.core.exception.UnauthorizedUserException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Order(1)
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Value("${xsrf.header.value:ValidToken}")
    private String validXsrfToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String xsrfToken = request.getHeader("x-xsrf-token");
            String token = jwtProvider.resolveToken(authorizationHeader);

            if (!validXsrfToken.equals(xsrfToken)) {
                throw new CrossOriginException();
            }

            if (authorizationHeader != null && !token.isBlank() && jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.doAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (UnauthorizedUserException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token in filter!");
        }
    }
}
