package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Service.AutorService;
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
@RequestMapping("/api/autores")
@CrossOrigin(origins = "http://localhost:8080")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    @Operation(
            summary = "Vai buscar todos os autores"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai mostrar todos os JSONs (listados) no sistema",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class)) }),

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
    public List<Autor> listar(){
        return autorService.encontrarTodosAutores();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Vai buscar o autor com base no seu id"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "302",
                    description = "vai mostrar o autor encontrado(JSON)",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar o autor por meio do ID",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do autor.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public Autor listarPorId(@Parameter(description = "O id utilizado para a busca")@PathVariable Integer id){
        return autorService.encontrarAutorPorId(id);
    }


    @GetMapping("/buscar")
    @Operation(
            summary = "Vai buscar o autor pelo nome"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302",
                    description="vai mostrar todos os autores encontradas pelo nome",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class)) }),

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
    public List<Autor> listarPorNome(@Parameter(description = "Nome utilizado para a busca")@RequestParam String nome){
        return autorService.encontrarAutorPorNome(nome);
    }


    @PostMapping()
    @Operation(
            summary = "Vai salvar o autor"
    )
    @ApiResponses(value = {  @ApiResponse(
            responseCode = "201",
            description = "Vai mostrar o JSON criado",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Autor.class)) }),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do autor.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )})
    public ResponseEntity<Autor> salvarAutor(@RequestBody @Valid Autor autor){
        Autor novoAutor = autorService.salvarAutor(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAutor);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar o autor pelo seu id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai alterar o JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class)) }),

            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do autor.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Autor> atualizarAutor(@Parameter(description = "O id para a atualização")@PathVariable Integer id, @RequestBody @Valid Autor autor){
        Autor autorAtualizado = autorService.atualizarAutorPorId(id, autor);
        return ResponseEntity.ok().body(autorAtualizado);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai buscar o id do autor para poder deletar"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "O autor vai ser deletado com sucesso pela api",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Autor.class)) }
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
    public ResponseEntity<Void> deletarAutor(@Parameter(description = "O id utilizado na busca do autor para o seu delete")@PathVariable Integer id){
        autorService.deletarAutorPorId(id);
        return ResponseEntity.noContent().build();
    }

}
