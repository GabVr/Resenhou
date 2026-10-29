package com.example.Resenhou.Leitura.Service;

import com.example.Resenhou.Exception.DataFormatException;
import com.example.Resenhou.Exception.ResourceNotFound;
import com.example.Resenhou.Exception.UserNotFoundException;
import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Leitura.Repository.DiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;


@Service
public class DiarioService {

    @Autowired
    private DiarioRepository diarioRepository;

    public static void validarDataInicio(LocalDate dataInicio){
        if(dataInicio.isAfter(LocalDate.now())) {
            throw new DataFormatException("Não pode ser posterior a data atual");
        }
    }

    public static void validarDataFinal(LocalDate dataInicio, LocalDate dataFinal){
        if(dataFinal.isAfter(LocalDate.now())) {
            throw new DataFormatException("Não pode ser posterior a data atual");
        }

        if(dataFinal.isBefore(dataInicio)) {
            throw new DataFormatException("Não pode ser anterior a data de início");
        }
    }

    public Diario salvarDiario(Diario diario){
        validarDataInicio(diario.getDataInicio());
        validarDataFinal(diario.getDataInicio(), diario.getDataTermino());
        return diarioRepository.save(diario);
    }

    public List<Diario> encontrarTodosDiarios(){
        return diarioRepository.findAll();
    }

    public Diario encontrarDiarioPorUsuario(Integer  usuarioId){
        return diarioRepository.findByUsuarioId(usuarioId).orElseThrow(() -> new UserNotFoundException("Não foi possível encontrar nenhum diário por meio do ID do usuário."));
    }

    public Diario encontrarDiarioPorId(Integer id){
        return diarioRepository.findById(id).orElseThrow( () -> new ResourceNotFound("Não foi possível encontrar o diário por meio do ID"));
    }

    public Diario encontrarDiarioPorLivro(Integer livroId) {
        return diarioRepository.findByLivroId(livroId).orElseThrow(() -> new ResourceNotFound("Não foi possível encontrar diário por meio do ID do livro."));
    }

    public Diario atualizarDiario(Integer idDiario, Diario diario){
        Diario diarioAtualizado = encontrarDiarioPorId(idDiario);
        diarioAtualizado.setLivroId(diario.getLivroId());
        diarioAtualizado.setUsuarioId(diario.getUsuarioId());
        diarioAtualizado.setRelido(diario.isRelido());
        diarioAtualizado.setDataInicio(diario.getDataInicio());
        diarioAtualizado.setDataTermino(diario.getDataTermino());
        diarioAtualizado.setPaginasLidas(diario.getPaginasLidas());
        return salvarDiario(diarioAtualizado);
    }

    public void deletarDiarioPorId(int idDiario){
        encontrarDiarioPorId(idDiario);
        diarioRepository.deleteById(idDiario);
    }
}
