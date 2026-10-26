package com.example.Resenhou.Usuarios.Service;


import com.example.Resenhou.Usuarios.Config.UserAuthenticated;
import com.example.Resenhou.Usuarios.Repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return  usuarioRepository.findByUsernameIgnoreCase(username)
                .map(UserAuthenticated::new)
                .orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
