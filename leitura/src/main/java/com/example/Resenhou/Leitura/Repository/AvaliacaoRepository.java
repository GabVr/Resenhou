package com.example.Resenhou.Leitura.Repository;

import com.example.Resenhou.Leitura.Model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    @Override
    Optional<Avaliacao> findById(Integer id);

    boolean existsById(Integer id);

    @Override
    List<Avaliacao> findAll();

   List<Avaliacao> findByNota(int nota);


   List<Avaliacao> findByUsuarioId(Integer usuarioId);

    List<Avaliacao> findByLivroId(Integer livroId);

   Avaliacao deleteById(int integer);
}
