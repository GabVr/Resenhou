package com.example.Resenhou.Catalogo.Repository;

import com.example.Resenhou.Catalogo.Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {

    List<Autor> findByNomeContainingIgnoreCase(String nome);
    boolean existsByNomeContainingIgnoreCase(String nome);

    boolean existsById(Integer id);

    @Override
    Optional<Autor> findById(Integer id);

    @Override
    List<Autor> findAll();

    @Override
    void deleteById(Integer id);
}
