package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Catalogo.Service.LivroService;
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
    public List<Livro> listar(){
        return livroService.encontrarTodosLivros();
    }

    @GetMapping("/{id}")
    public Livro listarPorId(@PathVariable Integer id){
        return livroService.encontrarLivroPorId(id);
    }

    @GetMapping("/buscar/titulo")
    public List<Livro> listarPorTitulo(@RequestParam String titulo){
        return livroService.encontrarLivroPorTitulo(titulo);
    }

    @GetMapping("/buscar/autor")
    public List<Livro> listarPorAutor(@RequestParam String autor){
        return livroService.encontrarLivroPorAutor(autor);
    }

    @GetMapping("/buscar/editora")
    public List<Livro> listarPorEditora(@RequestParam String editora){
        return livroService.encontrarLivroPorEditora(editora);
    }

    @PostMapping()
    public ResponseEntity<Livro> salvarLivro(@RequestBody @Valid Livro livro){
        Livro novoLivro = livroService.salvarLivro(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Integer id, @RequestBody @Valid Livro livro){
        Livro livroAtualizado = livroService.atualizarLivroPorId(id, livro);
        return ResponseEntity.ok().body(livroAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Integer id){
        livroService.deletarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }

}
