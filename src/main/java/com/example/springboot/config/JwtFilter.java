//package com.example.springboot.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.IOException;
//import io.jsonwebtoken.security.Keys;
//import jakarta.persistence.Column;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Collections;
//
//@Component
//public class JwtFilter extends UsernamePasswordAuthenticationFilter {
//    private final String jwtSecret = "your_jwt_secret";  // Use a secret key or a private key for signing JWTs
//    private final Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());  // Secure key for HMAC
//
//    @Override
//    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        String token = request.getHeader("Authorization");
//
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//
//            try {
//                // Parse JWT and extract claims
//                Claims claims = Jwts.parserBuilder()
//                        .setSigningKey(secretKey)
//                        .build()
//                        .parseClaimsJws(token)
//                        .getBody();
//
//                String username = claims.getSubject();
//
//                // If username is valid, set authentication in SecurityContext
//                if (username != null) {
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
//
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            } catch (Exception e) {
//                // Log the exception or handle invalid token cases here
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
//            }
//        }
//
//        // Continue the filter chain
//        chain.doFilter(request, response);
//    }
//}
