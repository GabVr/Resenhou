package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Service.EditoraService;
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
    public List<Editora> listar(){
        return editoraService.encontrarTodasEditoras();
    }

    @GetMapping("/{id}")
    public Editora listarPorId(@PathVariable Integer id){
        return editoraService.encontrarEditoraPorId(id);
    }

    @GetMapping("/buscar")
    public List<Editora> listarPorNome(@RequestParam String nome){
        return editoraService.encontrarEditoraPorNome(nome);
    }

    @PostMapping()
    public ResponseEntity<Editora> salvarEditora(@RequestBody @Valid Editora editora){
        Editora novaEditora = editoraService.salvarEditora(editora);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEditora);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editora> atualizarEditora(@PathVariable Integer id, @RequestBody @Valid Editora editora){
        Editora editoraAtualizada = editoraService.atualizarEditoraPorId(id, editora);
        return ResponseEntity.ok().body(editoraAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEditora(@PathVariable Integer id){
        editoraService.deletarEditoraPorId(id);
        return ResponseEntity.noContent().build();
    }

}
