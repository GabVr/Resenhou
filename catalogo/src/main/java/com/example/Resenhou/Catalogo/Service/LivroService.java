package com.example.Resenhou.Catalogo.Service;

import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Catalogo.Repository.LivroRepository;
import com.example.Resenhou.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public static void validarDataPublicacao(LocalDate dataPublicacao) {
        if (dataPublicacao.isAfter(LocalDate.now())) {
            throw new RuntimeException("A data de publicação não pode ser depois da data atual");
        }
    }

    public Livro salvarLivro(Livro livro){
        validarDataPublicacao(livro.getDataPublicacao());
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
