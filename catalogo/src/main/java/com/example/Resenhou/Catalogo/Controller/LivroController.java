package com.example.Resenhou.Catalogo.Controller;

import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Catalogo.Service.LivroService;
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
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    @Operation(
            summary = "Vai listar todos os livros do sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai mostrar todos os JSONs (listados) no sistema",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }),

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
    public List<Livro> listar(){
        return livroService.encontrarTodosLivros();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Vai buscar o livro com base no seu id"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "302",
                    description = "vai mostrar o livro encontrado(JSON)",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar o livro por meio do ID",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do livro.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public Livro listarPorId(@Parameter(description = "É o id que vai ser utilizado para a busca") @PathVariable Integer id){
        return livroService.encontrarLivroPorId(id);
    }


    @GetMapping("/buscar/titulo")
    @Operation(
            summary = "Vai buscar o livro com base no seu titulo"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302",
                    description="vai mostrar todos os livros encontrados pelo título",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Vai mostrar apenas isso aqui: []",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @RequestParam do titulo.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON ou caminho da requisição",
                    content = @Content

            )
    })
    public List<Livro> listarPorTitulo(@RequestParam String titulo){
        return livroService.encontrarLivroPorTitulo(titulo);
    }


    @GetMapping("/buscar/autor")
    @Operation(
            summary = "Vai listar os livros com base no autor"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "302",
                    description = "Vai mostrar o livro (JSON) com base no autor",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar o livro por Autor",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @RequestParam do autor.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON ou caminho da requisição",
                    content = @Content
            )

    })
    public ResponseEntity<Livro> listarPorAutor(@RequestParam Autor autor){
        return ResponseEntity.status(HttpStatus.FOUND).body(livroService.encontrarLivroPorAutor(autor));
    }


    @GetMapping("/buscar/editora")
    @Operation(
            summary = "Vai buscar os livros com base na editora"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "302",
                    description = "Vai mostrar o livro (JSON) com base na editora",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "Não foi possível encontrar o livro por Editora",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @RequestParam da Editora.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON ou caminho da requisição",
                    content = @Content
            )

    })
    public ResponseEntity<Livro> listarPorEditora(@RequestParam Editora editora){
        return ResponseEntity.status(HttpStatus.FOUND).body(livroService.encontrarLivroPorEditora(editora));
    }


    @PostMapping()
    @Operation(
            summary = "Vai salvar o livro"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Vai mostrar o JSON criado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do Livro.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Livro> salvarLivro(@RequestBody @Valid Livro livro){
        Livro novoLivro = livroService.salvarLivro(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Vai buscar o livro com base no seu id e depois ele será atualizado"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vai alterar o JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }),

            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do livro.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Livro> atualizarLivro(@Parameter(description = "É o id que vai ser utilizado para a busca")@PathVariable Integer id, @RequestBody @Valid Livro livro){
        Livro livroAtualizado = livroService.atualizarLivroPorId(id, livro);
        return ResponseEntity.ok().body(livroAtualizado);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai buscar o livro com base no seu id e aí irá deletá-lo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "O livro vai ser deletado com sucesso pela api",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class)) }
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
    public ResponseEntity<Void> deletarLivro(@Parameter(description = "É o id que vai ser utilizado para a busca")@PathVariable Integer id){
        livroService.deletarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }

}
