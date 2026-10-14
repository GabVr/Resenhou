package com.example.Resenhou.Leitura.Repository;

import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Leitura.Model.Avaliacao;
import com.example.Resenhou.Usuarios.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
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

   List<Avaliacao> findByUsuario(Usuario usuario);
   List<Avaliacao> findByLivro(Livro livro);

   Avaliacao deleteById(int integer);
}
