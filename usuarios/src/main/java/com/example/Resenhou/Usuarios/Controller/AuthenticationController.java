package com.example.Resenhou.Usuarios.Controller;

import com.example.Resenhou.Usuarios.Service.AuthenticationService;
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