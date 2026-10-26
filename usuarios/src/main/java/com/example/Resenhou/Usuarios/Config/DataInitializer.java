package com.example.Resenhou.Usuarios.Config;

import com.example.Resenhou.Usuarios.Model.Usuario;
import com.example.Resenhou.Usuarios.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Value("${INITIAL_USER_NAME}")
    private String nome;

    @Value("${INITIAL_USER_USERNAME}")
    private String username;

    @Value("${INITIAL_USER_EMAIL}")
    private String email;

    @Value("${INITIAL_USER_PASSWORD}")
    private String senha;

    @Value("${INITIAL_USER_CPF}")
    private String cpf;

    @Value("${INITIAL_USER_BIO}")
    private String biografia;

    @Bean
    CommandLineRunner inicializarDados(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (usuarioRepository.count() == 0) {

                Usuario usuario = new Usuario();

                usuario.setNome(nome);
                usuario.setUsername(username);
                usuario.setEmail(email);
                usuario.setSenha(passwordEncoder.encode(senha));
                usuario.setDataCadastro(LocalDate.now());
                usuario.setBiografia(biografia);
                usuario.setCpf(cpf);

                usuarioRepository.save(usuario);

                System.out.println("Usuário inicial criado com sucesso!");
                System.out.println("Username: " + username);
            }
        };
    }
}