package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Leitura.Service.DiarioService;
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
    public List<Diario> listar(){
        return diarioService.encontrarTodosDiarios();
    }

    @GetMapping("/{id}")
    public Diario encontrarDiarioPorID(@PathVariable Integer id){
        return diarioService.encontrarDiarioPorId(id);
    }

    @GetMapping("/buscar/usuario")
    public List<Diario> listarPorUsuario(@RequestParam Integer usuarioId){
        return diarioService.encontrarDiarioPorUsuario(usuarioId);
    }

    @GetMapping("/buscar/livro")
    public List<Diario> listarPorLivro(@RequestParam Integer livroId){
        return diarioService.encontrarDiarioPorLivro(livroId);
    }

    @PostMapping()
    public ResponseEntity<Diario> salvarDiario(@RequestBody @Valid Diario diario){
        Diario novoDiario = diarioService.salvarDiario(diario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDiario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Diario> atualizarDiario(@PathVariable Integer id, @RequestBody @Valid Diario diario){
        Diario diarioAtualizado = diarioService.atualizarDiario(id, diario);
        return ResponseEntity.ok().body(diarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDiario(@PathVariable Integer id){
        diarioService.deletarDiarioPorId(id);
        return ResponseEntity.noContent().build();
    }

}
