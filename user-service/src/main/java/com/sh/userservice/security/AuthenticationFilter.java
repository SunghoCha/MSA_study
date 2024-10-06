package com.sh.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.userservice.config.TokenProperties;
import com.sh.userservice.dto.UserDto;
import com.sh.userservice.service.UserService;
import com.sh.userservice.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProperties tokenProperties;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public AuthenticationFilter(TokenProperties tokenProperties, UserService userService, ObjectMapper objectMapper) {
        this.tokenProperties = tokenProperties;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin requestLogin = objectMapper.readValue(request.getReader(), RequestLogin.class);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(requestLogin.getEmail(), requestLogin.getPassword());
            return getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new RuntimeException("Invalid login request format", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(username);

        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getExpirationTime()))
                .signWith(Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes()), SignatureAlgorithm.HS256)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getUserId());
    }
}
