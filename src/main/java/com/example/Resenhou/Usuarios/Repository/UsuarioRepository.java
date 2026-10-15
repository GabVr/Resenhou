package com.example.Resenhou.Usuarios.Repository;

import com.example.Resenhou.Usuarios.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


    @Override
    Optional<Usuario> findById(Integer id);

    @Override
    List<Usuario> findAll();

    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    boolean existsByNomeContainingIgnoreCase(String nome);
    List<Usuario> findByUsuarioContainingIgnoreCase(String username);

    @Override
    boolean existsById(Integer id);

    Usuario deleteById(int id);
}
