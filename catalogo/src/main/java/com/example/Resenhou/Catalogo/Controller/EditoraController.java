package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Service.EditoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editoras")
public class EditoraController {

    @Autowired
    private EditoraService editoraService;


    @GetMapping
    @Operation(
            summary = "Vai listar todas as editoras"
    )
    public List<Editora> listar(){
        return editoraService.encontrarTodasEditoras();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Vai buscar a editora pelo seu id"
    )
    public Editora listarPorId(@Parameter(description = "É o id utilizado para a busca") @PathVariable Integer id){
        return editoraService.encontrarEditoraPorId(id);
    }

    @GetMapping("/buscar")
    public List<Editora> listarPorNome(@Parameter(description = "É o nome utilizado para a busca")@RequestParam String nome){
        return editoraService.encontrarEditoraPorNome(nome);
    }

    @PostMapping()
    @Operation(
            summary = "Vai salvar a editora"
    )
    public ResponseEntity<Editora> salvarEditora(@RequestBody @Valid Editora editora){
        Editora novaEditora = editoraService.salvarEditora(editora);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEditora);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar a editora com base no id de busca"
    )
    public ResponseEntity<Editora> atualizarEditora(@Parameter(description = "É o id utilizado para a busca da editora")@PathVariable Integer id, @RequestBody @Valid Editora editora){
        Editora editoraAtualizada = editoraService.atualizarEditoraPorId(id, editora);
        return ResponseEntity.ok().body(editoraAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar a editora com base no id de busca"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarEditora(@Parameter(description = "É o id utilizado para a busca da editora para deletar")@PathVariable Integer id){
        editoraService.deletarEditoraPorId(id);
        return ResponseEntity.noContent().build();
    }

}
