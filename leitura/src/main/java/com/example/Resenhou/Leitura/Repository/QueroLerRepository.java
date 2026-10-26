package com.example.Resenhou.Leitura.Repository;

import com.example.Resenhou.Leitura.Model.QueroLer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueroLerRepository extends JpaRepository<QueroLer, Integer> {

    @Override
    Optional<QueroLer> findById(Integer id);

    @Override
    List<QueroLer> findAll();

    @Override
    boolean existsById(Integer id);

    QueroLerRepository deleteById(int id);
    List<QueroLer> findByUsuarioId(Integer usuarioId);

    List<QueroLer> findByLivroId(Integer livroId);



}
