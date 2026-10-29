package com.example.Resenhou.Usuarios.Service;

import com.example.Resenhou.Exception.DataFormatException;
import com.example.Resenhou.Exception.NameErrorException;
import com.example.Resenhou.Exception.UserNotFoundException;
import com.example.Resenhou.Usuarios.Model.Usuario;
import com.example.Resenhou.Usuarios.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;



@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository usuarioRepository;


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

    public Usuario salvarUsuario(Usuario usuario){

        validarDataCadastro(usuario.getDataCadastro());
        usuario.setCpf(limparCpf(usuario.getCpf()));
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
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
        usuarioAtualizado.setSenha(DadosUsuario.getSenha());
        usuarioAtualizado.setCpf(DadosUsuario.getCpf());
        return salvarUsuario(usuarioAtualizado);
    }

    public void deletarUsuarioPorId(int idUsuario){
        encontrarUsuarioId(idUsuario);
        usuarioRepository.deleteById(idUsuario);
    }
}