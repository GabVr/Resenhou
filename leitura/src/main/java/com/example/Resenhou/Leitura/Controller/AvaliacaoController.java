package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Avaliacao;
import com.example.Resenhou.Leitura.Service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    @Operation(
            summary = "Vai mostrar todas as avaliações do sistema"
    )
    public List<Avaliacao> listar(){
        return avaliacaoService.encontrarTodasAvaliacoes();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Vai mostra a avaliação por meio do seu id"
    )
    public ResponseEntity<Avaliacao> listarPorId(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
       return ResponseEntity.status(HttpStatus.FOUND).body(avaliacaoService.encontrarAvaliacaoPorId(id));
    }

    @GetMapping("/buscar/usuario")
    @Operation(
            summary = "Vai mostrar a avaliação com base no seu usuário"
    )
    public ResponseEntity<Avaliacao> listarPorUsuario(@Parameter(description = "O id do usuário utilizado para a busca")@RequestParam Integer usuarioId){
        return ResponseEntity.status(HttpStatus.FOUND).body(avaliacaoService.encontrarAvalicaoPorUsuario(usuarioId));
    }

    @GetMapping("/buscar/livro")
    @Operation(
            summary = "Vai mostrar as avaliações do livro"
    )
    public ResponseEntity<Avaliacao> listarPorLivro(@Parameter(description = "O id do livro utilizado para a busca")@RequestParam Integer livroId){
        return ResponseEntity.status(HttpStatus.FOUND).body(avaliacaoService.encontrarAvaliacaoPorLivro(livroId));
    }


    @PostMapping()
    @Operation(
            summary = "Vai salvar as avaliações"
    )
    public ResponseEntity<Avaliacao> salvarAvaliacao(@RequestBody @Valid Avaliacao avaliacao){
        Avaliacao novaAvaliacao = avaliacaoService.salvarAvaliacao(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar a avaliação com base no id de busca"
    )
    public ResponseEntity<Avaliacao> atualizarAvaliacao(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid Avaliacao avaliacao){
        Avaliacao avaliacaoAtualizada = avaliacaoService.atualizarAvaliacaoPorId(id, avaliacao);
        return ResponseEntity.ok().body(avaliacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar uma avaliação com base no id de busca"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarAvaliacao(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        avaliacaoService.deletarAvaliacaoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
