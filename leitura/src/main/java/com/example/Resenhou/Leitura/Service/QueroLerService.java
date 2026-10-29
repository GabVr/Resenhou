package com.example.Resenhou.Leitura.Service;

import com.example.Resenhou.Exception.ResourceNotFound;
import com.example.Resenhou.Exception.UserNotFoundException;
import com.example.Resenhou.Leitura.Model.QueroLer;
import com.example.Resenhou.Leitura.Repository.QueroLerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;



@Service
public class QueroLerService {

    @Autowired
    private QueroLerRepository queroLerRepository;

    public static void validarDataAdicao(LocalDate dataAdicao){
        if(dataAdicao.isAfter(LocalDate.now())) {
            throw new RuntimeException("Não pode ser posterior a data atual");
        }
    }

    public QueroLer salvarQueroLer(QueroLer queroLer){
        validarDataAdicao(queroLer.getDataAdicao());
        return queroLerRepository.save(queroLer);
    }

    public List<QueroLer> encontrarTodosQueroLer(){
        return queroLerRepository.findAll();
    }

    public QueroLer encontrarPorUsuario(Integer  usuarioId) {
        return queroLerRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new UserNotFoundException("Não foi possível encontrar por meio do ID do usuario"));
    }

    public QueroLer encontrarQueroLerPorId(Integer id){
        return queroLerRepository.findById(id).orElseThrow( () -> new ResourceNotFound("Não foi possível encontrar o quero ler por meio do ID"));
    }

    public QueroLer encontrarQueroLerPorLivro(Integer livroId) {
        return queroLerRepository.findByLivroId(livroId).orElseThrow(() -> new ResourceNotFound("Não foi possível encontrar por meio do ID do livro"));
    }

    public QueroLer atualizarQueroLer(Integer idQueroLer, QueroLer queroLer){
        QueroLer queroLerAtualizado = encontrarQueroLerPorId(idQueroLer);
        queroLerAtualizado.setDataAdicao(queroLer.getDataAdicao());
        queroLerAtualizado.setLivroId(queroLer.getLivroId());
        return salvarQueroLer(queroLerAtualizado);
    }

    public void deletarQueroLerPorId(int idQueroLer){
        encontrarQueroLerPorId(idQueroLer);
        queroLerRepository.deleteById(idQueroLer);
    }


}
