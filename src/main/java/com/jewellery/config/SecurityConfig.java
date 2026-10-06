package com.jewellery.config;

import com.jewellery.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> {

            System.out.println("LOGIN ATTEMPT: " + username);

            return userRepository.findByEmail(username)
                    .map(user -> {

                        System.out.println(
                                "USER FOUND: " + user.getEmail()
                        );

                        System.out.println(
                                "ROLE: " + user.getRole()
                        );

                        return org.springframework.security.core.userdetails.User
                                .withUsername(user.getEmail())
                                .password(user.getPassword())
                                .roles(user.getRole())
                                .build();
                    })
                    .orElseThrow(() -> {

                        System.out.println(
                                "USER NOT FOUND: " + username
                        );

                        return new UsernameNotFoundException(
                                "User not found: " + username
                        );
                    });
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/forgot-password",
                                "/reset-password",
                                "/css/**",
                                "/images/**"
                        ).permitAll()

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .usernameParameter("username")

                        .passwordParameter("password")

                        .defaultSuccessUrl("/", true)

                        .failureUrl("/login?error=true")

                        .permitAll()
                )

                .logout(logout -> logout

                        .logoutSuccessUrl("/")

                        .permitAll()
                );

        return http.build();
    }
}