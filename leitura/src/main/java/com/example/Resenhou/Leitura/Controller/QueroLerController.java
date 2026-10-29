package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.QueroLer;
import com.example.Resenhou.Leitura.Service.QueroLerService;
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
    public List<QueroLer> listar(){
        return queroLerService.encontrarTodosQueroLer();
    }

    @GetMapping("/{id}")
    public QueroLer encontrarQueroLerPorID(@PathVariable Integer id){
        return queroLerService.encontrarQueroLerPorId(id);
    }

    @GetMapping("/buscar/usuario")
    public ResponseEntity<QueroLer> listarPorUsuario(@RequestParam Integer usuarioId){
        return ResponseEntity.status(HttpStatus.FOUND).body(queroLerService.encontrarPorUsuario(usuarioId));
    }

    @GetMapping("/buscar/livro")
    public ResponseEntity<QueroLer> listarPorLivro(@RequestParam Integer livroId){
        return ResponseEntity.status(HttpStatus.FOUND).body(queroLerService.encontrarQueroLerPorLivro(livroId));
    }

    @PostMapping()
    public ResponseEntity<QueroLer> salvarQueroLer(@RequestBody @Valid QueroLer queroLer){
        QueroLer novoQueroLer = queroLerService.salvarQueroLer(queroLer);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoQueroLer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QueroLer> atualizarLista(@PathVariable Integer id, @RequestBody @Valid QueroLer queroLer){
        QueroLer queroLerAtualizado = queroLerService.atualizarQueroLer(id, queroLer);
        return ResponseEntity.ok().body(queroLerAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarQueroLer(@PathVariable Integer id){
        queroLerService.deletarQueroLerPorId(id);
        return ResponseEntity.noContent().build();
    }


}
