package com.driven.dm.global.config.security;

import com.driven.dm.global.config.security.jwt.JwtTokenProvider;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.global.exception.ErrorResponse;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String LOGIN_PROCESS_URL = "/api/v1/auth/sign-in";

    public AuthenticationFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl(LOGIN_PROCESS_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException {
        try {
            FormLoginRequest loginRequest =
                objectMapper.readValue(request.getInputStream(), FormLoginRequest.class);

            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken
                .unauthenticated(loginRequest.username(), loginRequest.password());

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw AppException.of(UserErrorCode.USER_UNAUTHORIZED);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain, Authentication authResult) {
        UUID id = ((SecurityUser) authResult.getPrincipal()).getId();
        String username = ((SecurityUser) authResult.getPrincipal()).getUsername();
        UserRole role = ((SecurityUser) authResult.getPrincipal()).getRole();

        String token = jwtTokenProvider.createToken(id, username, role);
        response.addHeader(AUTHORIZATION_HEADER, token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {

        response.setStatus(UserErrorCode.USER_UNAUTHORIZED.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.from(UserErrorCode.USER_UNAUTHORIZED);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
