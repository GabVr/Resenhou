package com.example.Resenhou.Leitura.Service;

import com.example.Resenhou.Exception.DataFormatException;
import com.example.Resenhou.Leitura.Config.UsuarioAtual;
import com.example.Resenhou.Exception.ResourceNotFound;
import com.example.Resenhou.Leitura.Model.Lista;
import com.example.Resenhou.Leitura.Repository.ListaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Resenhou.Leitura.Model.ItemLista;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.HashSet;

import java.time.LocalDate;
import java.util.List;


@Service
public class ListaService {

    @Autowired
    private ListaRepository listaRepository;

    @Autowired
    private UsuarioAtual usuarioAtual;

    @Autowired
    private CatalogoClient catalogoClient;

    public static void validarDataCriacao(LocalDate dataCriacao){
        if(dataCriacao.isAfter(LocalDate.now())) {
            throw new DataFormatException("Não pode ser posterior a data atual");
        }
    }

    @Transactional
    public Lista salvarLista(Lista lista) {
        lista.setId(null);
        lista.setUsuarioId(usuarioAtual.getId());
        lista.setDataCriacao(LocalDate.now());
        lista.setLivroId(null);

        preencherItens(lista, lista.getItens());

        return listaRepository.save(lista);
    }

    public List<Lista> encontrarTodasListas() {
        return listaRepository.findAllByUsuarioId(usuarioAtual.getId());
    }

    public Lista encontrarListasPorUsuario(Integer usuarioId) {
        if (!usuarioAtual.getId().equals(usuarioId)) {
            throw new ResourceNotFound("Lista não encontrada");
        }

        return encontrarTodasListas().stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFound("Lista não encontrada"));
    }

    public List<Lista> encontrarPorNome(String nome) {
        return listaRepository
                .findByPublicaTrueAndNomeContainingIgnoreCase(nome.trim());
    }

    public Lista encontrarListaPorId(Integer id) {
        return listaRepository
                .findByIdAndUsuarioId(id, usuarioAtual.getId())
                .orElseThrow(() ->
                        new ResourceNotFound("Lista não encontrada"));
    }

    public Lista visualizarListaPorId(Integer id) {
        Lista lista = listaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound("Lista não encontrada"));

        boolean pertenceAoUsuario =
                lista.getUsuarioId().equals(usuarioAtual.getId());

        if (!lista.isPublica() && !pertenceAoUsuario) {
            throw new ResourceNotFound("Lista não encontrada");
        }

        return lista;
    }

    @Transactional(readOnly = true)
    public Lista encontrarListaPorLivro(Integer livroId) {
        return encontrarTodasListas().stream()
                .filter(lista ->
                        java.util.Objects.equals(lista.getLivroId(), livroId)
                        || lista.getItens().stream().anyMatch(item ->
                                java.util.Objects.equals(
                                        item.getLivroId(), livroId)))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFound("Lista não encontrada"));
    }

    @Transactional
    public Lista atualizarLista(Integer idLista, Lista dados) {
        Lista lista = encontrarListaPorId(idLista);

        lista.setNome(dados.getNome());
        lista.setDescricao(dados.getDescricao());
        lista.setPublica(dados.isPublica());
        lista.setVisualicacaoPerfil(dados.isVisualicacaoPerfil());

        preencherItens(lista, dados.getItens());

        return listaRepository.save(lista);
    }

    @Transactional
    public void deletarListaPorId(int idLista){
        encontrarListaPorId(idLista);
        listaRepository.deleteById(idLista);
    }

    private void preencherItens(Lista lista, List<ItemLista> itensRecebidos) {
        List<ItemLista> novosItens = new ArrayList<>();
        HashSet<Integer> livrosAdicionados = new HashSet<>();

        if (itensRecebidos != null) {
            for (ItemLista recebido : itensRecebidos) {
                if (recebido == null ||
                        recebido.getLivroId() == null ||
                        recebido.getLivroId() <= 0) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Informe um livro válido"
                    );
                }

                if (!livrosAdicionados.add(recebido.getLivroId())) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "O mesmo livro não pode aparecer duas vezes na lista"
                    );
                }
                catalogoClient.validarLivro(recebido.getLivroId());

                ItemLista item = new ItemLista();
                item.setLivroId(recebido.getLivroId());
                item.setUsuarioId(lista.getUsuarioId());
                item.setLista(lista);
                item.setOrdemExibicao(novosItens.size());

                novosItens.add(item);
            }
        }

        lista.getItens().clear();
        lista.getItens().addAll(novosItens);
    }
}
