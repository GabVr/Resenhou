package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Lista;
import com.example.Resenhou.Leitura.Service.ListaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listas")
public class ListaController {

    @Autowired
    ListaService listaService;

    @GetMapping
    public List<Lista> listar(){
        return listaService.encontrarTodasListas();
    }

    @GetMapping("/{id}")
    public Lista encontrarListaPorID(@PathVariable Integer id){
        return listaService.encontrarListaPorId(id);
    }

    @GetMapping("/buscar/usuario")
    public List<Lista> listarPorUsuario(@RequestParam Integer usuarioId){
        return listaService.encontrarListasPorUsuario(usuarioId);
    }

    @GetMapping("/buscar/livro")
    public List<Lista> listarPorLivro(@RequestParam Integer livroId){
        return listaService.encontrarListaPorLivro(livroId);
    }

    @GetMapping("buscar/nome")
    public List<Lista> listarPorNome(@RequestParam String nome){
        return listaService.encontrarPorNome(nome);
    }

    @PostMapping()
    public ResponseEntity<Lista> salvarLista(@RequestBody @Valid Lista lista){
        Lista novaLista = listaService.salvarLista(lista);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaLista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lista> atualizarLista(@PathVariable Integer id, @RequestBody @Valid Lista lista){
        Lista listaAtualizada = listaService.atualizarLista(id, lista);
        return ResponseEntity.ok().body(listaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLista(@PathVariable Integer id){
        listaService.deletarListaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
