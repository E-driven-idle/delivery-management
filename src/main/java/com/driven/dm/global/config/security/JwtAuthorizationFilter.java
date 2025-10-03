package com.driven.dm.global.config.security;

import com.driven.dm.global.config.security.jwt.JwtTokenProvider;
import com.driven.dm.user.domain.entity.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtTokenProvider.resolveToken(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            UUID id = jwtTokenProvider.getId(token);
            String username = jwtTokenProvider.getUsername(token);
            String role = jwtTokenProvider.getRole(token);
            UserRole userRole = UserRole.valueOf(role);
            SecurityUser securityUser = new SecurityUser(id, username, null, userRole);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                securityUser, null, securityUser.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}

