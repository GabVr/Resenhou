package com.example.Resenhou.Usuarios.Controller;


import com.example.Resenhou.Usuarios.Model.Usuario;
import com.example.Resenhou.Usuarios.Service.UsuarioService;
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
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(
            summary = "Vai listar todos os usuários do sistema"
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Vai mostrar todos os JSONs (listados) no sistema",
                    content = { @Content(mediaType = "application/json",
                             schema = @Schema(implementation = Usuario.class)) }),

            @ApiResponse(
                    responseCode = "404",
                    description = "vai retornar apenas: []",
                    content = @Content
            ),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do usuário.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),


            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public List<Usuario> listar(){
        return usuarioService.encontrarTodosUsuarios();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Vai pegar o usuário utilizando o seu ID.")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "302",
                    description = "vai mostrar o usuário encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)) }),

            @ApiResponse(responseCode = "404",
                         description = "Não foi possível encontrar o usuário por meio do ID",
                         content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do usuário.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Usuario> listarUsuarioPorId(@Parameter(description = "o id que vai ser usado na busca")@PathVariable Integer id){

        return ResponseEntity.status(HttpStatus.FOUND).body(usuarioService.encontrarUsuarioId(id));
    }


    @GetMapping("/buscar")
    @Operation(
            summary = "Vai listar todos os usuários, mas pelo nome."
    )
    @ApiResponses(value = {

            @ApiResponse(responseCode = "302",
                        description="vai mostrar o usuário(os) encontrado",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = Usuario.class)) }),

            @ApiResponse(responseCode = "404",
                    description = "O nome desse usuário não foi encontrado.",
                    content = @Content),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do usuário.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Usuario> listarPorNome(@Parameter(description = "vai usar a variável nome para poder encontrar o usuário(os)")@RequestParam String nome){
        return ResponseEntity.status(HttpStatus.FOUND).body(usuarioService.encontrarUsuarioPorNome(nome));
    }

    @PostMapping()
    @Operation(
            summary = "Vai salvar o usuário."
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "201",
                    description = "Vai mostrar o JSON criado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)) }),


            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do usuário.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody @Valid Usuario usuario){
        Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Vai atualizar o usuário pelo seu id."
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Vai alterar o JSON",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)) }),

            @ApiResponse(
                    responseCode = "400",
                    description = "Vai depender do erro que foi analisado pelo @Valid do usuário.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    public ResponseEntity<Usuario> atualizarUsuario(@Parameter(description = "o id que vai ser usado na busca para atualizar")@PathVariable Integer id, @RequestBody @Valid Usuario usuario){
        Usuario usuarioAtualizado = usuarioService.atualizarUsuarioPorId(id, usuario);
        return ResponseEntity.ok().body(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Vai deletar o seu usuário pelo id."
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "204",
                    description = "O usuário vai ser deletado com sucesso pela api",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class)) }
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Bad request(Não deu para encontrar o usuário pelo id)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),

            @ApiResponse(
                    responseCode = "500",
                    description = "Vai ser um erro no servidor, sendo alguma condição inesperada no JSON",
                    content = @Content

            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarUsuario(@Parameter(description = "o id que vai ser usado na busca para o delete")@PathVariable Integer id){
        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }
}
