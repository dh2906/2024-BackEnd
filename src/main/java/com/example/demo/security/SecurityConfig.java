
package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(
                (auth) -> auth.disable()
        );

        http.formLogin(
                (auth) -> auth.disable()
        );

        http.httpBasic(
                (auth) -> auth.disable()
        );

        http.authorizeHttpRequests(
                (auth) -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/main", "/posts/**", "/articles/**", "/members/**").hasRole("USER")
                        .requestMatchers("/boards/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(
                new JwtFilter(jwtUtil),
                LoginFilter.class
        );

        //AuthenticationManager()와 JWTUtil 인수 전달
        http.addFilterAt(
                new LoginFilter(
                        authenticationManager(authenticationConfiguration),
                        jwtUtil
                ),
                UsernamePasswordAuthenticationFilter.class
        );

        http.sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }
}
