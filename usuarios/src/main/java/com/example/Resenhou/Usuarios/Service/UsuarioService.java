package com.example.Resenhou.Usuarios.Service;

import com.example.Resenhou.Exception.DataFormatException;
import com.example.Resenhou.Exception.EmailNotFoundException;
import com.example.Resenhou.Exception.NameErrorException;
import com.example.Resenhou.Exception.UserNotFoundException;
import com.example.Resenhou.Usuarios.DTO.AtualizarSenhaDTO;
import com.example.Resenhou.Usuarios.Model.Usuario;
import com.example.Resenhou.Usuarios.Repository.UsuarioRepository;
import com.resend.core.exception.ResendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void validarDataCadastro(LocalDate dataCadastro) {
        if(dataCadastro.isAfter(LocalDate.now())){
            throw new DataFormatException("Não pode ser posterior a data atual");
        }
    }

    private String limparCpf(String cpf) {
        if (cpf == null) {
            return null;
        }

        return cpf.replaceAll("\\D", "");
    }


    public Usuario salvarUsuario(Usuario usuario) {

        validarDataCadastro(usuario.getDataCadastro());

        usuario.setCpf(limparCpf(usuario.getCpf()));
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario cadastrarUsuario(Usuario usuario) {

        Usuario novoUsuario = salvarUsuario(usuario);

        try {
            String mensagem = """
                <h2>Olá, %s!</h2>
                <p>Seja muito bem-vindo(a) ao <strong>Resenhou</strong>!</p>
                <p>Sua conta foi criada com sucesso.</p>
                <p>Agora você pode descobrir livros, compartilhar avaliações
                e organizar suas histórias favoritas.</p>
                <p>Esperamos que aproveite nossa comunidade!</p>
                <br>
                <p><strong>Equipe Resenhou</strong></p>
                """.formatted(novoUsuario.getNome());

            emailService.enviarEmail(
                    novoUsuario.getEmail(),
                    "Bem-vindo ao Resenhou!",
                    mensagem
            );

        } catch (ResendException e) {
            logger.error("Não foi possível enviar o e-mail de boas-vindas.", e);
        }

        return novoUsuario;
    }

    public Usuario encontrarUsuarioPorNome(String nomeUsuario){
        return usuarioRepository.findByNomeContainingIgnoreCase(nomeUsuario).orElseThrow(() -> new NameErrorException("O nome desse usuário não foi encontrado."));
    }

    public Usuario encontrarUsuarioId(Integer idUsuario){
    return usuarioRepository.findById(idUsuario).orElseThrow( () -> new UserNotFoundException("Não foi possível encontrar o usuário por meio do ID"));
    }

    public List<Usuario> encontrarTodosUsuarios(){
        return usuarioRepository.findAll();
    }

    public Usuario atualizarUsuarioPorId(Integer idUsuario, Usuario DadosUsuario){
        Usuario usuarioAtualizado = encontrarUsuarioId(idUsuario);
        usuarioAtualizado.setNome(DadosUsuario.getUsername());
        usuarioAtualizado.setEmail(DadosUsuario.getEmail());
        usuarioAtualizado.setCpf(DadosUsuario.getCpf());
        return salvarUsuario(usuarioAtualizado);
    }

    public void atualizarSenha(AtualizarSenhaDTO dados) {

        Usuario usuario = usuarioRepository.findByEmail(dados.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "Não encontramos uma conta com esse e-mail."
                ));

        if (!passwordEncoder.matches(
                dados.getSenhaAtual(),
                usuario.getSenha()
        )) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        if (dados.getSenhaAtual().equals(dados.getNovaSenha())) {
            throw new IllegalArgumentException(
                    "A nova senha não pode ser igual à senha atual."
            );
        }

        usuario.setSenha(passwordEncoder.encode(dados.getNovaSenha()));

        usuarioRepository.save(usuario);

        try {
            String mensagem = """
                <h2>Olá, %s!</h2>
                <p>Sua senha do Resenhou foi atualizada com sucesso.</p>
                <p>Se você não realizou essa alteração, entre em contato conosco.</p>
                <p><strong>Equipe Resenhou</strong></p>
                """.formatted(usuario.getNome());

            emailService.enviarEmail(
                    usuario.getEmail(),
                    "Sua senha foi atualizada!",
                    mensagem
            );

        } catch (ResendException e) {
            logger.error("Não foi possível enviar o e-mail de confirmação.", e);
        }
    }

    public void deletarUsuarioPorId(int idUsuario){
        encontrarUsuarioId(idUsuario);
        usuarioRepository.deleteById(idUsuario);
    }
}