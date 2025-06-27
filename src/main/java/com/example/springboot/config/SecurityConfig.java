package com.example.springboot.config;


import com.example.springboot.models.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.springboot.models.entities.UserRole.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // for @PreAuthorize()
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/v1/**").permitAll()
                        .anyRequest().authenticated());
        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
//                        .requestMatchers("/users/**", "v3/api-docs/**", "/swagger-ui/**").permitAll()
//                        .requestMatchers("/login", "/register").permitAll()
//                        .requestMatchers("/users/{id}/delete").hasAnyAuthority(ADMIN.getAuthority())
//                        .requestMatchers("/api/projects/recent").hasAnyAuthority(ADMIN.getAuthority())
//                        .requestMatchers("/admin/**").hasAnyAuthority(ADMIN.getAuthority())
//                        .anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults());
//                        .formLogin(formLogin -> formLogin
//                                .loginPage("/login")
//                                .defaultSuccessUrl("/api/users/all")
//                                .permitAll());
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
