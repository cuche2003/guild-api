package com.nat.guildapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Profile("development")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class DevSecurityConfig {
    private final BaseAuthenticationEntryPoint authenticationEntryPoint;
    @Value("${okta.oauth2.audience}")
    private String audience;
    @Value("${okta.oauth2.issuer}")
    private String issuer;

    public DevSecurityConfig(BaseAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("actuator", "/actuator/*").permitAll()
                    .requestMatchers("/api/v1/*").authenticated()
                    .anyRequest().permitAll()
            )
            .cors((cors) -> {
            })
            .oauth2ResourceServer((oauth2) -> oauth2
                .jwt(jwtConfigurer ->
                    jwtConfigurer
                        .decoder(jwtDecoder()))
            )
            .exceptionHandling(exceptionHandler ->
                exceptionHandler.authenticationEntryPoint(authenticationEntryPoint)
            )
            .build();
    }

    private JwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);

        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }
}