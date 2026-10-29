package com.example.Resenhou.Usuarios.Controller;


import com.example.Resenhou.Usuarios.Model.Usuario;
import com.example.Resenhou.Usuarios.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.encontrarTodosUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario listarUsuarioPorId(@PathVariable Integer id){
        return usuarioService.encontrarUsuarioId(id);
    }


    @GetMapping("/buscar")
    public ResponseEntity<Usuario> listarPorNome(@RequestParam String nome){
        return ResponseEntity.status(HttpStatus.FOUND).body(usuarioService.encontrarUsuarioPorNome(nome));
    }

    @PostMapping()
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody @Valid Usuario usuario){
        Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Integer id, @RequestBody @Valid Usuario usuario){
        Usuario usuarioAtualizado = usuarioService.atualizarUsuarioPorId(id, usuario);
        return ResponseEntity.ok().body(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id){
        usuarioService.deletarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }
}
