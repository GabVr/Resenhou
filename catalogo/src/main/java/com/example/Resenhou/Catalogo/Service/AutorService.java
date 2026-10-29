package com.example.Resenhou.Catalogo.Service;


import com.example.Resenhou.Catalogo.Model.Autor;
import com.example.Resenhou.Catalogo.Repository.AutorRepository;
import com.example.Resenhou.Exception.DataFormatException;
import com.example.Resenhou.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public static void validarDataNascimento(LocalDate dataNascimento) {
        if(dataNascimento.isAfter(LocalDate.now())) {
            throw new DataFormatException("Não pode ser posterior a data de nascimento");
        }
    }

    public Autor salvarAutor(Autor autor) {
        validarDataNascimento(autor.getDataNascimento());
        return autorRepository.save(autor);
    }

    public List<Autor> encontrarTodosAutores() {
        return autorRepository.findAll();
    }

    public Autor encontrarAutorPorId(Integer id) {
        return autorRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Não foi possível encontrar autor por ID"));
    }

    public List<Autor> encontrarAutorPorNome(String nomeAutor) {
        return autorRepository.findByNomeContainingIgnoreCase(nomeAutor);
    }

    public Autor atualizarAutorPorId(Integer id, Autor dadosAutor) {
        Autor autorAtualizado = encontrarAutorPorId(id);

        autorAtualizado.setNome(dadosAutor.getNome());
        autorAtualizado.setBiografia(dadosAutor.getBiografia());
        autorAtualizado.setNacionalidade(dadosAutor.getNacionalidade());
        autorAtualizado.setLivros(dadosAutor.getLivros());
        autorAtualizado.setDataNascimento(dadosAutor.getDataNascimento());

        return salvarAutor(autorAtualizado);
    }

    public void deletarAutorPorId(int id) {
        encontrarAutorPorId(id);
        autorRepository.deleteById(id);
    }
}
