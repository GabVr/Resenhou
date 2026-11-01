package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Leitura.Model.Lista;
import com.example.Resenhou.Leitura.Service.DiarioService;
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
@RequestMapping("/api/diarios")
public class DiarioController {

    @Autowired
    DiarioService diarioService;

    @GetMapping
    @Operation(
            summary = "Vai mostrar todos os diários cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai mostrar todos os JSONs (listados) no sistema",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Diario.class)) }),

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
    public List<Diario> listar(){
        return diarioService.encontrarTodosDiarios();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Vai encontrar o diário por meio do id de busca"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "302",
                    description = "vai mostrar o diario encontrado(JSON)",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Diario.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar o diario por meio do ID",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do diario.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Diario> encontrarDiarioPorID(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.FOUND).body(diarioService.encontrarDiarioPorId(id));
    }


    @GetMapping("/buscar/usuario")
    @Operation(
            summary = "Vai encontrar o diário com base no seu usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Vai mostrar o diario (JSON) com base no id do usuário",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Diario.class)) }),

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
    public ResponseEntity<Diario> listarPorUsuario(@Parameter(description = "O id do usuário utilizado para a busca")@RequestParam Integer usuarioId){
        return ResponseEntity.status(HttpStatus.FOUND).body(diarioService.encontrarDiarioPorUsuario(usuarioId));
    }


    @GetMapping("/buscar/livro")
    @Operation(
            summary = "Vai encontrar o diário por meio do id do livro respectivamente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Vai mostrar o diario (JSON) com base no id do livro",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar o diario por meio do ID do livro",
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
    public ResponseEntity<Diario> listarPorLivro(@Parameter(description = "O id do livro utilizado para a busca")@RequestParam Integer livroId){
        return ResponseEntity.status(HttpStatus.FOUND).body(diarioService.encontrarDiarioPorLivro(livroId));
    }


    @PostMapping()
    @Operation(
            summary = "Vai salvar o diário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vai mostrar o JSON criado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Diario.class)) }),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do Diario.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Diario> salvarDiario(@RequestBody @Valid Diario diario){
        Diario novoDiario = diarioService.salvarDiario(diario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDiario);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar o diário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai alterar o JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Diario.class)) }),

            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do diario .",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Diario> atualizarDiario(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid Diario diario){
        Diario diarioAtualizado = diarioService.atualizarDiario(id, diario);
        return ResponseEntity.ok().body(diarioAtualizado);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar o diário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "O diario vai ser deletado com sucesso pela api",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Diario.class)) }
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
    public ResponseEntity<Void> deletarDiario(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        diarioService.deletarDiarioPorId(id);
        return ResponseEntity.noContent().build();
    }

}
