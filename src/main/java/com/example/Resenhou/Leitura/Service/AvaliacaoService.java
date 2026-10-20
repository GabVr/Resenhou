package com.example.Resenhou.Leitura.Service;

import com.example.Resenhou.Catalogo.Model.Livro;
import com.example.Resenhou.Leitura.Model.Avaliacao;
import com.example.Resenhou.Leitura.Repository.AvaliacaoRepository;
import com.example.Resenhou.Usuarios.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public static void validarDataAvaliacao(LocalDate dataAvaliacao){
        if(dataAvaliacao.isAfter(LocalDate.now())) {
            throw new RuntimeException("Não pode ser posterior a data atual");
        }
    }

    public Avaliacao salvarAvaliacao(Avaliacao avaliacao){
        validarDataAvaliacao(avaliacao.getData());
        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> encontrarTodasAvaliacoes(){
        return avaliacaoRepository.findAll();
    }

    public List<Avaliacao> encontrarAvalicaoPorUsuario(Usuario usuario){
        return avaliacaoRepository.findByUsuarioId(usuario);
    }

    public Avaliacao encontrarAvaliacaoPorId(Integer id){
        return avaliacaoRepository.findById(id).orElseThrow( () -> new NoSuchElementException("Não foi possível encontrar a avalição por meio do ID"));
    }

    public List<Avaliacao> encontrarAvaliacaoPorLivro(Livro livro) {
        return avaliacaoRepository.findByLivroId(livro);
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

