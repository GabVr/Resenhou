package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.QueroLer;
import com.example.Resenhou.Leitura.Service.QueroLerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quero-ler")
public class QueroLerController {

    @Autowired
    QueroLerService queroLerService;

    @GetMapping
    @Operation(
            summary = "Vai listar todos os livros que o usuário quer ler"
    )
    public List<QueroLer> listar(){
        return queroLerService.encontrarTodosQueroLer();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Vai buscar o livro que o usuário quer ler pelo id"
    )
    public ResponseEntity<QueroLer> encontrarQueroLerPorID(@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.FOUND).body(queroLerService.encontrarQueroLerPorId(id));
    }

    @GetMapping("/buscar/usuario")
    @Operation(
            summary = "Vai buscar os livros favoritados por meio do usuário"
    )
    public ResponseEntity<QueroLer> listarPorUsuario(@Parameter(description = "O id do usuário utilizado para a busca")@RequestParam Integer usuarioId){
        return ResponseEntity.status(HttpStatus.FOUND).body(queroLerService.encontrarPorUsuario(usuarioId));
    }

    @GetMapping("/buscar/livro")
    @Operation(
            summary = "Vai buscar livros que o usuário quer ler"
    )
    public ResponseEntity<QueroLer> listarPorLivro(@Parameter(description = "O id do livro utilizado para a busca")@RequestParam Integer livroId){
        return ResponseEntity.status(HttpStatus.FOUND).body(queroLerService.encontrarQueroLerPorLivro(livroId));
    }

    @PostMapping()
    @Operation(
            summary = "Vai salvar os favoritados"
    )
    public ResponseEntity<QueroLer> salvarQueroLer(@RequestBody @Valid QueroLer queroLer){
        QueroLer novoQueroLer = queroLerService.salvarQueroLer(queroLer);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoQueroLer);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar a lista com base no id"
    )
    public ResponseEntity<QueroLer> atualizarLista(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid QueroLer queroLer){
        QueroLer queroLerAtualizado = queroLerService.atualizarQueroLer(id, queroLer);
        return ResponseEntity.ok().body(queroLerAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar a lista com base no id"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarQueroLer(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        queroLerService.deletarQueroLerPorId(id);
        return ResponseEntity.noContent().build();
    }


}
