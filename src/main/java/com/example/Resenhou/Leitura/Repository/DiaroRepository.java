package com.example.Resenhou.Leitura.Repository;

import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Usuarios.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaroRepository extends JpaRepository<Diario, Integer> {

    @Override
    Optional<Diario> findById(Integer id);

    @Override
    List<Diario> findAll();

    @Override
    boolean existsById(Integer id);

    Diario deleteById(int id);

    List<Diario> findByUsuarioId(Usuario usuario);
    List<Diario> findByLivroId(Livro livro);
}