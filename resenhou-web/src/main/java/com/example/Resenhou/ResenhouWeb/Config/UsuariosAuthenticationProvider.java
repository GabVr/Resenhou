package com.example.Resenhou.ResenhouWeb.Config;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UsuariosAuthenticationProvider implements AuthenticationProvider {
    private final RestClient usuarios;

    public UsuariosAuthenticationProvider(@Value("${services.usuarios.url}") String url) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(10));
        usuarios = RestClient.builder().baseUrl(url).requestFactory(factory).build();
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
            var response = usuarios.post().uri("/auth/authenticate")
                    .body(Map.of("username", authentication.getName(),
                            "password", authentication.getCredentials().toString()))
                    .retrieve().body(AuthResponse.class);
            if (response == null || response.accessToken() == null || response.accessToken().isBlank()) {
                throw new AuthenticationServiceException("Resposta de autenticação inválida");
            }
            var result = UsernamePasswordAuthenticationToken.authenticated(
                    authentication.getName(), null, List.of());
            // Transferido para a sessão pelo handler de sucesso.
            result.setDetails(response.accessToken());
            return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public record AuthResponse(String accessToken) {}
}
