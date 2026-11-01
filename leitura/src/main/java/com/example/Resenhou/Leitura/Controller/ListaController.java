package com.example.Resenhou.Leitura.Controller;

import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Leitura.Model.Lista;
import com.example.Resenhou.Leitura.Service.ListaService;
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
@RequestMapping("/api/listas")
public class ListaController {

    @Autowired
    ListaService listaService;

    @GetMapping
    @Operation(
            summary = "Vai mostrar todas as listas do sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai mostrar todos os JSONs (listados) no sistema",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }),

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
    public List<Lista> listar(){
        return listaService.encontrarTodasListas();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Vai encontrar a lista por seu id"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "302",
                    description = "vai mostrar a lista encontrada(JSON)",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar a lista por meio do ID",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid da lista.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Lista> encontrarListaPorID(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.FOUND).body(listaService.encontrarListaPorId(id));
    }


    @GetMapping("/buscar/usuario")
    @Operation(
            summary = "Vai mostrar a lista com base no seu usuário"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Vai mostrar a lista (JSON) com base no id do usuário",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }),

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
    public ResponseEntity<Lista> listarPorUsuario(@Parameter(description = "O id do usuário utilizado para a busca")@RequestParam Integer usuarioId){
        return ResponseEntity.status(HttpStatus.FOUND).body(listaService.encontrarListasPorUsuario(usuarioId));
    }


    @GetMapping("/buscar/livro")
    @Operation(
            summary = "Vai mostrar a lista com base no livro designado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Vai mostrar a lista (JSON) com base no id do livro",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar a lista por meio do ID do livro",
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
    public ResponseEntity<Lista> listarPorLivro(@Parameter(description = "O id do livro utilizado para a busca")@RequestParam Integer livroId){
        return ResponseEntity.status(HttpStatus.FOUND).body(listaService.encontrarListaPorLivro(livroId));
    }


    @GetMapping("buscar/nome")
    @Operation(
            summary = "Vai mostrar a lista com base no seu nome"
    )
    @ApiResponses(value = {

            @ApiResponse(responseCode = "302",
                    description="vai mostrar todas as listas encontradas pelo nome",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Vai mostrar apenas isso aqui: []",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @RequestParam do nome.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON ou caminho da requisição",
                    content = @Content

            )
    })
    public List<Lista> listarPorNome(@Parameter(description = "O nome utilizado para a busca")@RequestParam String nome){
        return listaService.encontrarPorNome(nome);
    }


    @PostMapping()
    @Operation(
            summary = "Vai salvar a lista"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vai mostrar o JSON criado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do Lista.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Lista> salvarLista(@RequestBody @Valid Lista lista){
        Lista novaLista = listaService.salvarLista(lista);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaLista);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar a lista"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai alterar o JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }),

            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid da lista.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Lista> atualizarLista(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid Lista lista){
        Lista listaAtualizada = listaService.atualizarLista(id, lista);
        return ResponseEntity.ok().body(listaAtualizada);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar a lista"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "A lista vai ser deletada com sucesso pela api",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Lista.class)) }
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
    public ResponseEntity<Void> deletarLista(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        listaService.deletarListaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
