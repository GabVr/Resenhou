package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Leitura.Service.DiarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diarios")
public class DiarioController {

    @Autowired
    DiarioService diarioService;

    @GetMapping
    @Operation(
            summary = "Vai mostrar todos os diários cadastrados no sistema"
    )
    public List<Diario> listar(){
        return diarioService.encontrarTodosDiarios();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Vai encontrar o diário por meio do id de busca"
    )
    public ResponseEntity<Diario> encontrarDiarioPorID(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.FOUND).body(diarioService.encontrarDiarioPorId(id));
    }

    @GetMapping("/buscar/usuario")
    @Operation(
            summary = "Vai encontrar o diário com base no seu usuário"
    )
    public ResponseEntity<Diario> listarPorUsuario(@Parameter(description = "O id do usuário utilizado para a busca")@RequestParam Integer usuarioId){
        return ResponseEntity.status(HttpStatus.FOUND).body(diarioService.encontrarDiarioPorUsuario(usuarioId));
    }

    @GetMapping("/buscar/livro")
    @Operation(
            summary = "Vai encontrar o diário por meio do id do livro respectivamente"
    )
    public ResponseEntity<Diario> listarPorLivro(@Parameter(description = "O id do livro utilizado para a busca")@RequestParam Integer livroId){
        return ResponseEntity.status(HttpStatus.FOUND).body(diarioService.encontrarDiarioPorLivro(livroId));
    }

    @PostMapping()
    @Operation(
            summary = "Vai salvar o diário"
    )
    public ResponseEntity<Diario> salvarDiario(@RequestBody @Valid Diario diario){
        Diario novoDiario = diarioService.salvarDiario(diario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDiario);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar o diário"
    )
    public ResponseEntity<Diario> atualizarDiario(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid Diario diario){
        Diario diarioAtualizado = diarioService.atualizarDiario(id, diario);
        return ResponseEntity.ok().body(diarioAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar o diário"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarDiario(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        diarioService.deletarDiarioPorId(id);
        return ResponseEntity.noContent().build();
    }

}
