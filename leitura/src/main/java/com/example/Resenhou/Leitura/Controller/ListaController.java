package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Lista;
import com.example.Resenhou.Leitura.Service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
public class ListaController {

    @Autowired
    ListaService listaService;

    @GetMapping
    @Operation(
            summary = "Vai mostrar todas as listas do sistema"
    )
    public List<Lista> listar(){
        return listaService.encontrarTodasListas();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Vai encontrar a lista por seu id"
    )
    public ResponseEntity<Lista> encontrarListaPorID(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.FOUND).body(listaService.encontrarListaPorId(id));
    }

    @GetMapping("/buscar/usuario")
    @Operation(
            summary = "Vai mostrar a lista com base no seu usuário"
    )
    public ResponseEntity<Lista> listarPorUsuario(@Parameter(description = "O id do usuário utilizado para a busca")@RequestParam Integer usuarioId){
        return ResponseEntity.status(HttpStatus.FOUND).body(listaService.encontrarListasPorUsuario(usuarioId));
    }

    @GetMapping("/buscar/livro")
    @Operation(
            summary = "Vai mostrar a lista com base no livro designado"
    )
    public ResponseEntity<Lista> listarPorLivro(@Parameter(description = "O id do livro utilizado para a busca")@RequestParam Integer livroId){
        return ResponseEntity.status(HttpStatus.FOUND).body(listaService.encontrarListaPorLivro(livroId));
    }

    @GetMapping("buscar/nome")
    @Operation(
            summary = "Vai mostrar a lista com base no seu nome"
    )
    public List<Lista> listarPorNome(@Parameter(description = "O nome utilizado para a busca")@RequestParam String nome){
        return listaService.encontrarPorNome(nome);
    }

    @PostMapping()
    @Operation(
            summary = "Vai salvar a lista"
    )
    public ResponseEntity<Lista> salvarLista(@RequestBody @Valid Lista lista){
        Lista novaLista = listaService.salvarLista(lista);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaLista);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar a lista"
    )
    public ResponseEntity<Lista> atualizarLista(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid Lista lista){
        Lista listaAtualizada = listaService.atualizarLista(id, lista);
        return ResponseEntity.ok().body(listaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar a lista"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarLista(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        listaService.deletarListaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
