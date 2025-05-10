package com.gmail.seminyden.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.*;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${app.keycloak.client.id}")
    private String clientId;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeExchange(
                        authorizeExchange -> authorizeExchange
                                .pathMatchers(HttpMethod.GET, "/resources/**", "/songs/**").hasAnyRole("USER", "ADMIN")
                                .pathMatchers(HttpMethod.DELETE, "/resources/**", "/songs/**").hasRole("ADMIN")
                                .pathMatchers(HttpMethod.POST, "/resources/**", "/songs/**").hasRole("ADMIN")
                                .pathMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                                .anyExchange()
                                .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        return new ReactiveJwtAuthenticationConverterAdapter(new KeycloakJwtAuthConverter());
    }

    public class KeycloakJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {
            return new JwtAuthenticationToken(jwt, extractRoles(jwt));
        }

        private List<GrantedAuthority> extractRoles(Jwt jwt) {
            List<GrantedAuthority> authorities = new ArrayList<>();

            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess != null && resourceAccess.containsKey(clientId)) {
                Map<String, Object> client = (Map<String, Object>) resourceAccess.get(clientId);
                if (client != null && client.containsKey("roles")) {
                    List<String> roles = (List<String>) client.get("roles");
                    for (String role : roles) {
                        System.out.println("Role: ROLE_" + role);
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                }
            }

            return authorities;
        }
    }
}