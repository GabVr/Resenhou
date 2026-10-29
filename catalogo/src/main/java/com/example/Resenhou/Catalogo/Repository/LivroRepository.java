package com.example.Resenhou.Catalogo.Repository;


import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {

    List<Livro> findByTituloContainingIgnoreCase(String Titulo);
    Optional<Livro> findByAutoresContainingIgnoreCase(Autor autor);
    Optional<Livro> findByEditoraContainingIgnoreCase(Editora editora);

    void deleteById(int id);

    boolean existsByNomeContainingIgnoreCase(String nome);

    @Override
    List<Livro> findAll();

    @Override
    boolean existsById(Integer id);

    @Override
    Optional<Livro> findById(Integer id);
}
