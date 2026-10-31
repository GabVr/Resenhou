package com.example.Resenhou.Catalogo.Repository;
import com.example.Resenhou.Catalogo.Model.Genero;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GeneroRepository extends JpaRepository<Genero, Integer> {
    Optional<Genero> findByNomeIgnoreCase(String nome);
}
