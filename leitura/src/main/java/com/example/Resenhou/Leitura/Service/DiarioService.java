package com.example.Resenhou.Leitura.Service;

import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Leitura.Repository.DiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DiarioService {

    @Autowired
    private DiarioRepository diarioRepository;

    public static void validarDataInicio(LocalDate dataInicio){
        if(dataInicio.isAfter(LocalDate.now())) {
            throw new RuntimeException("Não pode ser posterior a data atual");
        }
    }

    public static void validarDataFinal(LocalDate dataInicio, LocalDate dataFinal){
        if(dataFinal.isAfter(LocalDate.now())) {
            throw new RuntimeException("Não pode ser posterior a data atual");
        }

        if(dataFinal.isBefore(dataInicio)) {
            throw new RuntimeException("Não pode ser anterior a data de início");
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

    public List<Diario> encontrarDiarioPorUsuario(Integer  usuarioId){
        return diarioRepository.findByUsuarioId(usuarioId);
    }

    public Diario encontrarDiarioPorId(Integer id){
        return diarioRepository.findById(id).orElseThrow( () -> new NoSuchElementException("Não foi possível encontrar o diário por meio do ID"));
    }

    public List<Diario> encontrarDiarioPorLivro(Integer livroId) {
        return diarioRepository.findByLivroId(livroId);
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
