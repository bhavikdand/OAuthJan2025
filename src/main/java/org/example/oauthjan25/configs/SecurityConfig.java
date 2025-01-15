package org.example.oauthjan25.configs;

import org.example.oauthjan25.handlers.OAuth2SuccessCallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private OAuth2SuccessCallbackHandler successCallbackHandler;

    @Bean
    public JwtAuthenticationFilter getJwtAuthFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth -> {
                    oauth.successHandler(successCallbackHandler);
                })
                .oauth2Client(Customizer.withDefaults())
                .addFilterBefore(getJwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
