package com.example.Resenhou.Leitura.Repository;


import com.example.Resenhou.Leitura.Model.Diario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiarioRepository extends JpaRepository<Diario, Integer> {


    Optional<Diario> findById(Integer id);


    List<Diario> findAll();


    boolean existsById(Integer id);

    void deleteById(int id);

    Optional<Diario> findByUsuarioId(Integer usuarioId);


    Optional<Diario> findByLivroId(Integer livroId);
}