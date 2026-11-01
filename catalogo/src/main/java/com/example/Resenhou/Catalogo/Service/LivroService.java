package com.example.Resenhou.Catalogo.Service;

import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Catalogo.Repository.LivroRepository;
import com.example.Resenhou.Catalogo.Model.Genero;
import com.example.Resenhou.Catalogo.Repository.GeneroRepository;
import com.example.Resenhou.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Resenhou.Catalogo.Repository.EditoraRepository;
import org.springframework.stereotype.Service;
import com.example.Resenhou.Catalogo.Repository.AutorRepository;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;
import java.util.List;


@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private EditoraRepository editoraRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private GeneroRepository generoRepository;

    public static void validarDataPublicacao(LocalDate dataPublicacao) {
        if (dataPublicacao == null || dataPublicacao.isAfter(LocalDate.now())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Informe uma data de publicação válida, até a data atual"
            );
        }
    }

    public Livro salvarLivro(Livro livro) {
        validarDataPublicacao(livro.getDataPublicacao());
        if (livro.getEditora() == null ||
                livro.getEditora().getId() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Informe a editora"
            );
        }

        Editora editora = editoraRepository
                .findById(livro.getEditora().getId())
                .orElseThrow(() ->
                        new ResourceNotFound("Editora não encontrada"));

        livro.setEditora(editora);

        if (livro.getAutores() == null || livro.getAutores().isEmpty()) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Informe pelo menos um autor"
            );
        }
        Set<Autor> autoresEncontrados = new HashSet<>();

        for (Autor referencia : livro.getAutores()) {
            if (referencia == null || referencia.getId() == null) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "Informe o ID do autor"
                );
            }

            Autor autor = autorRepository.findById(referencia.getId())
                    .orElseThrow(() ->
                            new ResourceNotFound("Autor não encontrado"));

            autoresEncontrados.add(autor);
        }

        livro.setAutores(autoresEncontrados);
        Set<Genero> generosEncontrados = new HashSet<>();

        if (livro.getGeneros() != null) {
            for (Genero referencia : livro.getGeneros()) {
                if (referencia == null || referencia.getId() == null) {
                    throw new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.BAD_REQUEST,
                            "Informe o ID do gênero"
                    );
                }

                Genero genero = generoRepository.findById(referencia.getId())
                        .orElseThrow(() ->
                                new ResourceNotFound("Gênero não encontrado"));

                generosEncontrados.add(genero);
            }
        }

        livro.setGeneros(generosEncontrados);
        return livroRepository.save(livro);
    }

    public List<Livro> encontrarTodosLivros(){
        return livroRepository.findAll();
    }

    public Livro encontrarLivroPorId(Integer id){
        return livroRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Não foi possível encontrar o livro por ID"));
    }

    public List<Livro> encontrarLivroPorTitulo(String titulo){
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Livro encontrarLivroPorAutor(Autor autor){
        return livroRepository.findByAutoresContainingIgnoreCase(autor).orElseThrow(() -> new ResourceNotFound("Não foi possível encontrar o livro por Autor"));
    }

    public Livro encontrarLivroPorEditora(Editora editora){
        return livroRepository.findByEditoraContainingIgnoreCase(editora).orElseThrow(() -> new ResourceNotFound("Não foi possível encontrar o livro por Editora"));
    }

    public Livro atualizarLivroPorId(Integer id, Livro dadosLivro){
        Livro livroAtualizado = encontrarLivroPorId(id);
        livroAtualizado.setNome(dadosLivro.getNome());
        livroAtualizado.setSinopse(dadosLivro.getSinopse());
        livroAtualizado.setNumeroPaginas(dadosLivro.getNumeroPaginas());
        livroAtualizado.setIsbn(dadosLivro.getIsbn());
        livroAtualizado.setTituloOriginal(dadosLivro.getTituloOriginal());
        livroAtualizado.setTitulo(dadosLivro.getTitulo());
        livroAtualizado.setGeneros(dadosLivro.getGeneros());
        livroAtualizado.setDataPublicacao(dadosLivro.getDataPublicacao());
        livroAtualizado.setAutores(dadosLivro.getAutores());
        livroAtualizado.setEditora(dadosLivro.getEditora());
        return salvarLivro(livroAtualizado);
    }

    public void deletarLivroPorId(int id){
        encontrarLivroPorId(id);
        livroRepository.deleteById(id);
    }
}
