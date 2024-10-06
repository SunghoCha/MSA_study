package com.sh.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.userservice.security.AuthenticationFilter;
import com.sh.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final TokenProperties tokenProperties;

    // objectMapper의 재사용성이나 생명주기가 authenticationFilter와 다르므로 filter에 생성자 주입안하고 config에서 의존성주입해줌
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationFilter authenticationFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(tokenProperties, userService, objectMapper);
        authenticationFilter.setAuthenticationManager(authenticationManager);

        return authenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // builder 꺼내서 쓰는거보단 configuration으로 하는게 좋지만 userService 세팅하려면 어쩔 수 없는듯
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(userService);
        return managerBuilder.build();
    }
}
