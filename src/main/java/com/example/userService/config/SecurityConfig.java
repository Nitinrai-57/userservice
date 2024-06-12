package com.example.userService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.net.http.HttpRequest;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain defaultSercurityFilterChain(HttpSecurity http) throws Exception{
        http.
                authorizeHttpRequests((autorize)->autorize.anyRequest().permitAll())
                .csrf().disable()
                .cors().disable();


                return http.build();

    }
}
