package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(r ->
                        r.requestMatchers(new AntPathRequestMatcher("/public/books", "GET"))
                                .permitAll())
                .authorizeHttpRequests(r ->
                        r.requestMatchers(new AntPathRequestMatcher("/public/**"))
                                .hasRole("client")
                                .anyRequest().authenticated())
                .authorizeHttpRequests(r ->
                        r.requestMatchers(new AntPathRequestMatcher("/private/**"))
                                .hasRole("employee")
                                .anyRequest().authenticated())
                .build();
    }
}
