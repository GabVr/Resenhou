package com.example.Resenhou.Leitura.Repository;

import com.example.Resenhou.Usuarios.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Lista extends JpaRepository<Lista, Integer> {

    @Override
    Optional<Lista> findById(Integer id);

    @Override
    List<Lista> findAll();

    @Override
    boolean existsById(Integer id);

    Lista deleteById(int id);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

}
