package com.example.Resenhou.Leitura.Repository;

import com.example.Resenhou.Leitura.Model.Lista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListaRepository extends JpaRepository<Lista, Integer> {

    @Override
    Optional<Lista> findById(Integer id);

    @Override
    List<Lista> findAll();

    @Override
    boolean existsById(Integer id);

    ListaRepository deleteById(int id);

    List<Lista> findByUsuarioId(Integer usuarioId);

    List<Lista> findByLivroId(Integer livroId);
    List<Lista> findByNomeContainingIgnoreCase(String nome);

}
