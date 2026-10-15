package com.example.Resenhou.Catalogo.Service;

import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Catalogo.Repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

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

    public Livro encontrarTodosLivrosPorId(Integer id){
        return livroRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Não foi possível encontrar o livro por ID"));
    }

    public List<Livro> encontrarLivroPorTitulo(String titulo){
        return livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> encontrarLivroPorAutor(String autor){
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }

    public List<Livro> encontrarLivroPorEditora(String editora){
        return livroRepository.findByEditoraContainingIgnoreCase(editora);
    }

    public Livro atualizarLivroPorId(Integer id, Livro dadosLivro){
        Livro livroAtualizado = encontrarTodosLivrosPorId(id);
        livroAtualizado.setTitulo(dadosLivro.getTitulo());
        livroAtualizado.setGenero(dadosLivro.getGenero());
        livroAtualizado.setDataPublicacao(dadosLivro.getDataPublicacao());
        livroAtualizado.setAutores(dadosLivro.getAutores());
        livroAtualizado.setEditora(dadosLivro.getEditora());
        return salvarLivro(livroAtualizado);
    }

    public Livro deletarLivroPorId(int id){
        encontrarTodosLivrosPorId(id);
        return livroRepository.deleteById(id);
    }
}
