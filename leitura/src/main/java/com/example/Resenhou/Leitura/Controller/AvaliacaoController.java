package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Avaliacao;
import com.example.Resenhou.Leitura.Service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    public List<Avaliacao> listar(){
        return avaliacaoService.encontrarTodasAvaliacoes();
    }

    @GetMapping("/{id}")
    public Avaliacao listarPorId(@PathVariable Integer id){
        return avaliacaoService.encontrarAvaliacaoPorId(id);
    }

    @GetMapping("/buscar/usuario")
    public List<Avaliacao> listarPorUsuario(@RequestParam Integer usuarioId){
        return avaliacaoService.encontrarAvalicaoPorUsuario(usuarioId);
    }

    @GetMapping("/buscar/livro")
    public List<Avaliacao> listarPorLivro(@RequestParam Integer livroId){
        return avaliacaoService.encontrarAvaliacaoPorLivro(livroId);
    }


    @PostMapping()
    public ResponseEntity<Avaliacao> salvarAvaliacao(@RequestBody @Valid Avaliacao avaliacao){
        Avaliacao novaAvaliacao = avaliacaoService.salvarAvaliacao(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizarAvaliacao(@PathVariable Integer id, @RequestBody @Valid Avaliacao avaliacao){
        Avaliacao avaliacaoAtualizada = avaliacaoService.atualizarAvaliacaoPorId(id, avaliacao);
        return ResponseEntity.ok().body(avaliacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable Integer id){
        avaliacaoService.deletarAvaliacaoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
