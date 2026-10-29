package com.example.Resenhou.Leitura.Service;


import com.example.Resenhou.Exception.ResourceNotFound;
import com.example.Resenhou.Exception.UserNotFoundException;
import com.example.Resenhou.Leitura.Model.Avaliacao;
import com.example.Resenhou.Leitura.Repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.zip.DataFormatException;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public static void validarDataAvaliacao(LocalDate dataAvaliacao) throws DataFormatException {
        if(dataAvaliacao.isAfter(LocalDate.now())) {
            throw new DataFormatException("Não pode ser posterior a data atual");
        }
    }

    public Avaliacao salvarAvaliacao(Avaliacao avaliacao){
        try {
            validarDataAvaliacao(avaliacao.getData());
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> encontrarTodasAvaliacoes(){
        return avaliacaoRepository.findAll();
    }

    public Avaliacao encontrarAvalicaoPorUsuario(Integer  usuarioId){
        return avaliacaoRepository.findByUsuarioId(usuarioId).orElseThrow(()-> new UserNotFoundException("Não foi possível encontrar o usuário por ID"));
    }

    public Avaliacao encontrarAvaliacaoPorId(Integer id){
        return avaliacaoRepository.findById(id).orElseThrow( () -> new ResourceNotFound("Não foi possível encontrar a avaliação por meio do ID"));
    }

    public Avaliacao encontrarAvaliacaoPorLivro(Integer livroId) {
        return avaliacaoRepository.findByLivroId(livroId).orElseThrow( () -> new ResourceNotFound("Não foi possível encontrar o livro por meio do ID"));
    }

    public Avaliacao atualizarAvaliacaoPorId(Integer idAvaliacao, Avaliacao DadosAvalicao){
        Avaliacao avaliacaoAtualizada = encontrarAvaliacaoPorId(idAvaliacao);
        avaliacaoAtualizada.setData(DadosAvalicao.getData());
        avaliacaoAtualizada.setNota(DadosAvalicao.getNota());
        avaliacaoAtualizada.setTexto(DadosAvalicao.getTexto());
        avaliacaoAtualizada.setLivroId(DadosAvalicao.getLivroId());
        avaliacaoAtualizada.setUsuarioId(DadosAvalicao.getUsuarioId());
        return salvarAvaliacao(avaliacaoAtualizada);
    }

    public void deletarAvaliacaoPorId(int idAvaliacao){
        encontrarAvaliacaoPorId(idAvaliacao);
        avaliacaoRepository.deleteById(idAvaliacao);
    }
}

