package com.example.Resenhou.Catalogo.Repository;

import com.example.Resenhou.Catalogo.Model.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Integer> {

    @Override
    boolean existsById(Integer integer);

    @Override
    List<Editora> findAll();

    List<Editora> findByNomeContainingIgnoreCase(String nome);
    Optional <Editora> findById(Integer id);

    Editora deleteById(int integer);
}
