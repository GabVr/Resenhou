package com.example.Resenhou.ResenhouWeb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()
                ).authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/static/**"

                        ).permitAll()

                        // Vai ser liberado para todos
                        .requestMatchers(
                                "/catalogo/**",
                                "/diario/**",
                                "/autores/**",
                                "/editoras/**"
                                "/index/**",
                                "/listas/**",
                                "/noticias/**",
                                "/queroter/**"
                        ).permitAll()

                        // Ao ser autenticado, vai ser levado diretamente para o perfil
                        .requestMatchers(
                                "/perfil"
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin( form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/perfil", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );
        return http.build();

    }
}
