package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
@CrossOrigin(origins = "http://localhost:8080")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    public List<Autor> listar(){
        return autorService.encontrarTodosAutores();
    }

    @GetMapping("/{id}")
    public Autor listarPorId(@PathVariable Integer id){
        return autorService.encontrarAutorPorId(id);
    }

    @GetMapping("/buscar")
    public List<Autor> listarPorNome(@RequestParam String nome){
        return autorService.encontrarAutorPorNome(nome);
    }

    @PostMapping()
    public ResponseEntity<Autor> salvarAutor(@RequestBody @Valid Autor autor){
        Autor novoAutor = autorService.salvarAutor(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAutor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizarAutor(@PathVariable Integer id, @RequestBody @Valid Autor autor){
        Autor autorAtualizado = autorService.atualizarAutorPorId(id, autor);
        return ResponseEntity.ok().body(autorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Integer id){
        autorService.deletarAutorPorId(id);
        return ResponseEntity.noContent().build();
    }

}
