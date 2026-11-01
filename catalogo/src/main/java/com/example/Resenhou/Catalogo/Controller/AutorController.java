package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(
            summary = "Vai buscar todos os autores"
    )
    public List<Autor> listar(){
        return autorService.encontrarTodosAutores();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Vai buscar o autor com base no seu id"
    )

    public Autor listarPorId(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        return autorService.encontrarAutorPorId(id);
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Vai buscar o autor pelo nome"
    )

    public List<Autor> listarPorNome(@Parameter(description = "Nome utilizado para a busca")@RequestParam String nome){
        return autorService.encontrarAutorPorNome(nome);
    }

    @PostMapping()
    @Operation(
            summary = "Vai salvar o autor"
    )

    public ResponseEntity<Autor> salvarAutor(@RequestBody @Valid Autor autor){
        Autor novoAutor = autorService.salvarAutor(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAutor);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar o autor pelo seu id"
    )

    public ResponseEntity<Autor> atualizarAutor(@Parameter(description = "O id para a atualização")@PathVariable Integer id, @RequestBody @Valid Autor autor){
        Autor autorAtualizado = autorService.atualizarAutorPorId(id, autor);
        return ResponseEntity.ok().body(autorAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai buscar o id do autor para poder deletar"
    )

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarAutor(@Parameter(description = "O id utilizado na busca do autor para o seu delete")@PathVariable Integer id){
        autorService.deletarAutorPorId(id);
        return ResponseEntity.noContent().build();
    }

}
