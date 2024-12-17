package org.example.pracainzynierska.functions;

import lombok.RequiredArgsConstructor;
import org.example.pracainzynierska.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@CrossOrigin
public class Security {

    private final UserService userService;
    private final PasswordEncodeSecurity passwordEncodeSecurity;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/", "/login", "/register", "/users")
                            .permitAll()
                            .anyRequest().authenticated();
                })
                .cors(cors -> cors.configurationSource(request -> {
                    // Tutaj konfiguracja CORS, jeśli chcesz to skonfigurować bezpośrednio w Security
                    var corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                    corsConfig.setAllowCredentials(true);
                    corsConfig.setAllowedHeaders(Collections.singletonList("*"));
                    return corsConfig;
                }))
                .formLogin(l -> {
                        l.loginPage("/login")
                        .defaultSuccessUrl("http://localhost:3000/main", true)
                        .failureUrl("http://localhost:3000/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll();
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(passwordEncodeSecurity.encoder());

        return authenticationManagerBuilder.build();
    }
}
