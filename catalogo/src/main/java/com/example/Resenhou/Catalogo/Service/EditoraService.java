package com.example.Resenhou.Catalogo.Service;


import com.example.Resenhou.Catalogo.Model.Editora;
import com.example.Resenhou.Catalogo.Repository.EditoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EditoraService {

    @Autowired
    private EditoraRepository editoraRepository;

    public Editora salvarEditora(Editora editora){
        return editoraRepository.save(editora);
    }

    public List<Editora> encontrarTodasEditoras(){
        return editoraRepository.findAll();
    }

    public Editora encontrarEditoraPorId(Integer idEditora){
        return  editoraRepository.findById(idEditora).orElseThrow(() -> new NoSuchElementException("Editora inexistente"));
    }

    public List<Editora> encontrarEditoraPorNome(String nomeEditora){
        return editoraRepository.findByNomeContainingIgnoreCase(nomeEditora);
    }

    public Editora atualizarEditoraPorId(Integer idEditora,Editora dadosEditora){
        Editora editoraAtualizada =  encontrarEditoraPorId(idEditora);
        editoraAtualizada.setNome(dadosEditora.getNome());
        editoraAtualizada.setPais(dadosEditora.getPais());
        editoraAtualizada.setSite(dadosEditora.getSite());
        editoraAtualizada.setLivros(dadosEditora.getLivros());
        return salvarEditora(editoraAtualizada);
    }

    public Editora deletarEditoraPorId(int idEditora){
        encontrarEditoraPorId(idEditora);
        return editoraRepository.deleteById(idEditora);
    }
}
