package com.nttdata.ejercicio.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para pruebas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/usuarios/crear",
                    "/swagger-ui/**", 
                    "/v3/api-docs/**", 
                    "/swagger-ui.html", 
                    "/api-docs/**").permitAll() // Permitir acceso sin autenticación
                .anyRequest().authenticated()
            );
        return http.build();
    }

}
