package com.example.Resenhou.ResenhouWeb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            UsuariosAuthenticationProvider authenticationProvider) throws Exception {

        http
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/login",
                                "/atualizar-senha",
                                "/cadastro-senha",
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
                                "/editoras/**",
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
                        .loginProcessingUrl("/auth/login")
                        .successHandler((request, response, authentication) -> {
                            request.getSession().setAttribute("accessToken", authentication.getDetails());
                            ((org.springframework.security.authentication.AbstractAuthenticationToken)
                                    authentication).setDetails(null);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"redirect\":\"" + request.getContextPath() + "/perfil\"}");
                        })
                        .failureHandler((request, response, exception) -> response.setStatus(401))
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
