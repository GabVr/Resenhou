package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Catalogo.Service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    @Operation(
            summary = "Vai listar todos os livros do sistema"
    )
    public List<Livro> listar(){
        return livroService.encontrarTodosLivros();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Vai buscar o livro com base no seu id"
    )
    public Livro listarPorId(@Parameter(description = "É o id que vai ser utilizado para a busca") @PathVariable Integer id){
        return livroService.encontrarLivroPorId(id);
    }

    @GetMapping("/buscar/titulo")
    @Operation(
            summary = "Vai buscar o livro com base no seu titulo"
    )
    public List<Livro> listarPorTitulo(@RequestParam String titulo){
        return livroService.encontrarLivroPorTitulo(titulo);
    }

    @GetMapping("/buscar/autor")
    @Operation(
            summary = "Vai listar os livros com base no autor"
    )
    public ResponseEntity<Livro> listarPorAutor(@RequestParam Autor autor){
        return ResponseEntity.status(HttpStatus.FOUND).body(livroService.encontrarLivroPorAutor(autor));
    }

    @GetMapping("/buscar/editora")
    @Operation(
            summary = "Vai buscar os livros com base na editora"
    )
    public ResponseEntity<Livro> listarPorEditora(@RequestParam Editora editora){
        return ResponseEntity.status(HttpStatus.FOUND).body(livroService.encontrarLivroPorEditora(editora));
    }

    @PostMapping()
    @Operation(
            summary = "Vai salvar o livro"
    )
    public ResponseEntity<Livro> salvarLivro(@RequestBody @Valid Livro livro){
        Livro novoLivro = livroService.salvarLivro(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Vai buscar o livro com base no seu id e depois ele será atualizado"
    )
    public ResponseEntity<Livro> atualizarLivro(@Parameter(description = "É o id que vai ser utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid Livro livro){
        Livro livroAtualizado = livroService.atualizarLivroPorId(id, livro);
        return ResponseEntity.ok().body(livroAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai buscar o livro com base no seu id e aí irá deletá-lo"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarLivro(@Parameter(description = "É o id que vai ser utilizado para a busca")@PathVariable Integer id){
        livroService.deletarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }

}
