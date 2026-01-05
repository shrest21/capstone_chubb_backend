package com.inventoryapp.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        HttpCookie jwtCookie = exchange.getRequest()
                .getCookies()
                .getFirst("jwt");

        if (jwtCookie == null) {
            System.out.println("No JWT cookie found");
            return chain.filter(exchange);
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwtCookie.getValue())
                    .getBody();

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            System.out.println("JWT validated - User: " + email + ", Role: " + role);

            // Adding ROLE_ prefix for Spring Security
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (Exception e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            return chain.filter(exchange);
        }
    }
}