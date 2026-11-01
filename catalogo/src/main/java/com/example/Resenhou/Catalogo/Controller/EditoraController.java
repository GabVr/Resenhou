package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Catalogo.Service.EditoraService;
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
@RequestMapping("/api/editoras")
public class EditoraController {

    @Autowired
    private EditoraService editoraService;


    @GetMapping
    @Operation(
            summary = "Vai listar todas as editoras"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai mostrar todos os JSONs (listados) no sistema",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Editora.class)) }),

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
    public List<Editora> listar(){
        return editoraService.encontrarTodasEditoras();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Vai buscar a editora pelo seu id"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "302",
                    description = "vai mostrar a editora encontrada(JSON)",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Editora.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar a editora por meio do ID",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid da editora.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public Editora listarPorId(@Parameter(description = "É o id utilizado para a busca") @PathVariable Integer id){
        return editoraService.encontrarEditoraPorId(id);
    }


    @GetMapping("/buscar")
    @Operation(
            summary = "Vai buscar a editora pelo seu nome"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302",
                    description="vai mostrar todas as editoras encontradas pelo título",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }),

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
    public List<Editora> listarPorNome(@Parameter(description = "É o nome utilizado para a busca")@RequestParam String nome){
        return editoraService.encontrarEditoraPorNome(nome);
    }

    @PostMapping()
    @Operation(
            summary = "Vai salvar a editora"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vai mostrar o JSON criado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Editora.class)) }),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid da Editora.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Editora> salvarEditora(@RequestBody @Valid Editora editora){
        Editora novaEditora = editoraService.salvarEditora(editora);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEditora);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar a editora com base no id de busca"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai alterar o JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Editora.class)) }),

            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid da editora.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Editora> atualizarEditora(@Parameter(description = "É o id utilizado para a busca da editora")@PathVariable Integer id, @RequestBody @Valid Editora editora){
        Editora editoraAtualizada = editoraService.atualizarEditoraPorId(id, editora);
        return ResponseEntity.ok().body(editoraAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar a editora com base no id de busca"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "A editora vai ser deletada com sucesso pela api",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Editora.class)) }
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
    public ResponseEntity<Void> deletarEditora(@Parameter(description = "É o id utilizado para a busca da editora para deletar")@PathVariable Integer id){
        editoraService.deletarEditoraPorId(id);
        return ResponseEntity.noContent().build();
    }

}
