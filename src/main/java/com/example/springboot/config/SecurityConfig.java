package com.example.springboot.config;


import com.example.springboot.models.entities.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.springboot.models.entities.UserRole.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // for @PreAuthorize()
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/**", "v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/users/{id}/delete").hasAnyAuthority(ADMIN.getAuthority())
                        .requestMatchers("/api/projects/recent").hasAnyAuthority(ADMIN.getAuthority())
                        .requestMatchers("/admin/**").hasAnyAuthority(ADMIN.getAuthority())
                        .anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults());
                        .formLogin(formLogin -> formLogin
//                                .loginPage("/login")
                                .defaultSuccessUrl("/api/users/all")
                                .permitAll());

        return http.build();
    }

}
