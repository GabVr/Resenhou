package com.example.Resenhou.Leitura.Service;

import com.example.Resenhou.Exception.DataFormatException;
import com.example.Resenhou.Exception.ResourceNotFound;
import com.example.Resenhou.Exception.UserNotFoundException;
import com.example.Resenhou.Leitura.Model.Lista;
import com.example.Resenhou.Leitura.Repository.ListaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class ListaService {

    @Autowired
    private ListaRepository listaRepository;

    public static void validarDataCriacao(LocalDate dataCriacao){
        if(dataCriacao.isAfter(LocalDate.now())) {
            throw new DataFormatException("Não pode ser posterior a data atual");
        }
    }

    public Lista salvarLista(Lista lista){
        validarDataCriacao(lista.getDataCriacao());
        return listaRepository.save(lista);
    }

    public List<Lista> encontrarTodasListas(){
        return listaRepository.findAll();
    }

    public Lista encontrarListasPorUsuario(Integer  usuarioId){
        return listaRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new UserNotFoundException("Não foi possível encontrar a lista por meio do ID do usuario"));
    }

    public List<Lista> encontrarPorNome(String nome){
        return listaRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Lista encontrarListaPorId(Integer id){
        return listaRepository.findById(id).orElseThrow( () -> new ResourceNotFound("Não foi possível encontrar a lista por meio do ID"));
    }

    public Lista encontrarListaPorLivro(Integer livroId) {
        return listaRepository.findByLivroId(livroId)
                .orElseThrow(() -> new ResourceNotFound("Não foi possível encontrar a lista por meio do ID do livro"));
    }

    public Lista atualizarLista(Integer idLista, Lista lista){
        Lista listaAtualizada = encontrarListaPorId(idLista);
        listaAtualizada.setNome(lista.getNome());
        listaAtualizada.setDescricao(lista.getDescricao());
        listaAtualizada.setItens(lista.getItens());
        listaAtualizada.setPublica(lista.isPublica());
        listaAtualizada.setDataCriacao(lista.getDataCriacao());
        return salvarLista(listaAtualizada);
    }

    public void deletarListaPorId(int idLista){
        encontrarListaPorId(idLista);
        listaRepository.deleteById(idLista);
    }
}
