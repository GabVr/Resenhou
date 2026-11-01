package com.example.Resenhou.Usuarios.Controller;

import com.example.Resenhou.Usuarios.Service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(
            AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Vai fazer a autenticação do usuário e aí gerar o token"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai mostrar o token: ",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized (Não autorizado)",
                    content = @Content
            )
    })
    public AuthResponse authenticate(@RequestBody AuthRequest request) {

        String token = authenticationService.authenticate(
                request.username(),
                request.password()
        );

        return new AuthResponse(token);
    }

    public record AuthRequest(String username, String password) {}

    public record AuthResponse(String accessToken) {}
}