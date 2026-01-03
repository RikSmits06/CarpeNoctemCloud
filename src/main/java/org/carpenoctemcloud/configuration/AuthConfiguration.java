package org.carpenoctemcloud.configuration;

import org.carpenoctemcloud.auth.CurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.WebApplicationContext;

/**
 * Beans and configuration for the authentication part of the server.
 */
@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    /**
     * How many hours until an auth token is no longer valid.
     */
    public static final int MAX_AGE_AUTH_TOKEN_IN_HOURS = 336;

    /**
     * The time in hours until an email confirmation token is no longer valid.
     */
    public static final int MAX_AGE_EMAIL_CONFIRMATION_IN_HOURS = 2;

    /**
     * The size in bytes of the auth token sent to the client.
     */
    public static final int AUTH_TOKEN_LENGTH = 256;

    /**
     * Creates a new AuthConfiguration but should not be done manually but through Spring Boot.
     */
    public AuthConfiguration() {
    }

    /**
     * The password algorithm we use to hash password.
     * Should not be changed randomly as it will break login for existing users.
     *
     * @return The encoder used for passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * The current user of the application.
     *
     * @return The context encapsulating the current user.
     */
    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
    public CurrentUserContext currentUserContext() {
        return new CurrentUserContext();
    }

    /**
     * We use this configuration to disable the standard login system because we are building our own.
     *
     * @param httpSecurity The {@link HttpSecurity} bean so we can build a {@link SecurityFilterChain}.
     * @return a {@link SecurityFilterChain} which permits all routes and disables csrf.
     * @throws Exception if building the filter chain goes wrong.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable).build();
    }
}
