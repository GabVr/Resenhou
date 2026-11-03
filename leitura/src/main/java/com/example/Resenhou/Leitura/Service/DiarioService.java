package com.example.Resenhou.Leitura.Service;

import com.example.Resenhou.Exception.ResourceNotFound;
import com.example.Resenhou.Leitura.Config.UsuarioAtual;
import com.example.Resenhou.Leitura.Model.Diario;
import com.example.Resenhou.Leitura.Repository.DiarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;


@Service
public class DiarioService {

    @Autowired
    private DiarioRepository diarioRepository;

    @Autowired
    private UsuarioAtual usuarioAtual;

    @Autowired
    private CatalogoClient catalogoClient;

    public static void validarDataInicio(LocalDate dataInicio) {
        if (dataInicio == null || dataInicio.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe uma data de início válida, até a data atual"
            );
        }
    }

    public static void validarDataFinal(
            LocalDate dataInicio,
            LocalDate dataFinal
    ) {
        if (dataFinal == null) {
            return; // Leitura em andamento.
        }

        if (dataFinal.isAfter(LocalDate.now()) ||
                dataFinal.isBefore(dataInicio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O término deve estar entre a data de início e a data atual"
            );
        }
    }

    private void validarLeitura(Diario diario) {
        validarDataInicio(diario.getDataInicio());
        validarDataFinal(diario.getDataInicio(), diario.getDataTermino());

        if (diario.getPaginasLidas() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As páginas lidas não podem ser negativas"
            );
        }

        if (diario.getLivroId() == null || diario.getLivroId() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe um livro válido"
            );
        }

        var livro = catalogoClient.buscarLivro(diario.getLivroId());
        int totalPaginas = livro.numeroPaginas();

        if (diario.getDataTermino() != null) {
            diario.setPaginasLidas(totalPaginas);
        } else if (diario.getPaginasLidas() > totalPaginas) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As páginas lidas não podem ultrapassar o total do livro"
            );
        }
    }

    @Transactional
    public Diario salvarDiario(Diario diario) {
        diario.setId(null);
        diario.setUsuarioId(usuarioAtual.getId());

        validarLeitura(diario);

        return diarioRepository.save(diario);
    }

    public List<Diario> encontrarTodosDiarios() {
        return diarioRepository.findAllByUsuarioId(usuarioAtual.getId());
    }

    public List<Diario> encontrarDiarioPorUsuario(Integer usuarioId) {
        if (!usuarioAtual.getId().equals(usuarioId)) {
            throw new ResourceNotFound("Leituras não encontradas");
        }

        return encontrarTodosDiarios();
    }

    public Diario encontrarDiarioPorId(Integer id) {
        return diarioRepository
                .findByIdAndUsuarioId(id, usuarioAtual.getId())
                .orElseThrow(() ->
                        new ResourceNotFound("Leitura não encontrada"));
    }

    public List<Diario> encontrarDiarioPorLivro(Integer livroId) {
        return diarioRepository.findAllByUsuarioIdAndLivroId(
                usuarioAtual.getId(),
                livroId
        );
    }

    @Transactional
    public Diario atualizarDiario(Integer idDiario, Diario dados) {
        Diario diario = encontrarDiarioPorId(idDiario);

        validarLeitura(dados);

        diario.setLivroId(dados.getLivroId());
        diario.setRelido(dados.isRelido());
        diario.setDataInicio(dados.getDataInicio());
        diario.setDataTermino(dados.getDataTermino());
        diario.setPaginasLidas(dados.getPaginasLidas());

        return diarioRepository.save(diario);
    }

    public void deletarDiarioPorId(int idDiario){
        encontrarDiarioPorId(idDiario);
        diarioRepository.deleteById(idDiario);
    }
}
