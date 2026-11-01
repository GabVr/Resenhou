package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Avaliacao;
import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Leitura.Model.Lista;
import com.example.Resenhou.Leitura.Service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai mostrar todos os JSONs (listados) no sistema",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class)) }),

            @ApiResponse(
                    responseCode = "404",
                    description = "vai retornar apenas: []",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no caminho da requisição",
                    content = @Content

            )
    })
    public List<Avaliacao> listar(){
        return avaliacaoService.encontrarTodasAvaliacoes();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Vai mostra a avaliação por meio do seu id"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "302",
                    description = "vai mostrar a avaliação encontrada (JSON)",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar a avaliação por meio do ID",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid da avaliação.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Avaliacao> listarPorId(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
       return ResponseEntity.status(HttpStatus.FOUND).body(avaliacaoService.encontrarAvaliacaoPorId(id));
    }


    @GetMapping("/buscar/usuario")
    @Operation(
            summary = "Vai mostrar a avaliação com base no seu usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Vai mostrar a avaliação (JSON) com base no id do autor",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar o usuário por ID",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @RequestParam do id do usuário.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON ou caminho da requisição",
                    content = @Content
            )
    })
    public ResponseEntity<Avaliacao> listarPorUsuario(@Parameter(description = "O id do usuário utilizado para a busca")@RequestParam Integer usuarioId){
        return ResponseEntity.status(HttpStatus.FOUND).body(avaliacaoService.encontrarAvalicaoPorUsuario(usuarioId));
    }


    @GetMapping("/buscar/livro")
    @Operation(
            summary = "Vai mostrar as avaliações do livro"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Vai mostrar a avaliação (JSON) com base no id do livro",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar a avaliação por meio do ID do livro",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @RequestParam do id do livro.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON ou caminho da requisição",
                    content = @Content
            )
    })
    public ResponseEntity<Avaliacao> listarPorLivro(@Parameter(description = "O id do livro utilizado para a busca")@RequestParam Integer livroId){
        return ResponseEntity.status(HttpStatus.FOUND).body(avaliacaoService.encontrarAvaliacaoPorLivro(livroId));
    }


    @PostMapping()
    @Operation(
            summary = "Vai salvar as avaliações"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vai mostrar o JSON criado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class)) }),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid da avaliação.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Avaliacao> salvarAvaliacao(@RequestBody @Valid Avaliacao avaliacao){
        Avaliacao novaAvaliacao = avaliacaoService.salvarAvaliacao(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar a avaliação com base no id de busca"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai alterar o JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class)) }),

            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid da avaliação.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Avaliacao> atualizarAvaliacao(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid Avaliacao avaliacao){
        Avaliacao avaliacaoAtualizada = avaliacaoService.atualizarAvaliacaoPorId(id, avaliacao);
        return ResponseEntity.ok().body(avaliacaoAtualizada);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar uma avaliação com base no id de busca"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "A avaliação vai ser deletada com sucesso pela api",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Avaliacao.class)) }
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Bad request, vai mostrar isso aqui: []",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarAvaliacao(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        avaliacaoService.deletarAvaliacaoPorId(id);
        return ResponseEntity.noContent().build();
    }
}
