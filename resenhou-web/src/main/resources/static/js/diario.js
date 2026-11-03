// Elementos e estado
const areaDiario = document.querySelector("#diarioLista");
const mensagemDiario = document.querySelector("#mensagemDiario");
const filtroDiario = document.querySelector("#filtroDiario");

const formularioLeitura = document.querySelector("#editForm");
const erroLeitura = document.querySelector("#erroLeitura");
const botaoSalvar = document.querySelector("#salvarLeitura");
const botaoNova = document.querySelector("#novaLeitura");

const modalLeitura = new bootstrap.Modal("#editModal");
const modalExcluir = new bootstrap.Modal("#excluirLeituraModal");
const confirmarExcluir = document.querySelector("#confirmarExcluirLeitura");
const erroExcluir = document.querySelector("#erroExcluirLeitura");

let leituras = [];
let livros = [];
let editandoId = null;
let excluindoId = null;
let abrindoFormulario = false;

// Requisições
async function requisicaoDiario(url, opcoes = {}) {
  const resposta = await fetch(url, opcoes);

  if (resposta.redirected || resposta.status === 401) {
    throw new Error("Faça login novamente para continuar.");
  }

  if (!resposta.ok) {
    throw new Error(
      "Não foi possível concluir a operação: HTTP " + resposta.status
    );
  }

  return resposta;
}

function cabecalhosDiario() {
  const token = document.querySelector('meta[name="_csrf"]')?.content;
  const nome = document.querySelector('meta[name="_csrf_header"]')?.content;

  if (!token || !nome) {
    throw new Error("Recarregue a página antes de salvar.");
  }

  return {
    "Content-Type": "application/json",
    [nome]: token,
  };
}

async function carregarLivrosDiario() {
  const resposta = await requisicaoDiario("/api/livros");
  livros = await resposta.json();
}

async function carregarLeituras() {
  const resposta = await requisicaoDiario("/api/diarios");
  leituras = await resposta.json();
  renderizarDiario();
}

// Renderização
function escaparDiario(valor) {
  const caracteres = {
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#39;",
  };

  return String(valor ?? "").replace(
    /[&<>"']/g,
    (caractere) => caracteres[caractere]
  );
}

function encontrarLivro(id) {
  return livros.find((livro) => Number(livro.id) === Number(id));
}

function formatarDataDiario(data) {
  if (!data) return "—";

  const [ano, mes, dia] = data.split("-");
  return `${dia}/${mes}/${ano}`;
}

function dataAtualDiario() {
  const hoje = new Date();

  return [
    hoje.getFullYear(),
    String(hoje.getMonth() + 1).padStart(2, "0"),
    String(hoje.getDate()).padStart(2, "0"),
  ].join("-");
}

function renderizarDiario() {
  const filtro = filtroDiario.value;

  const resultados = leituras.filter((leitura) => {
    if (filtro === "andamento") return !leitura.dataTermino;
    if (filtro === "concluidas") return Boolean(leitura.dataTermino);
    return true;
  });

  areaDiario.innerHTML = resultados.map((leitura) => {
    const livro = encontrarLivro(leitura.livroId);
    const titulo = livro?.titulo || `Livro #${leitura.livroId}`;
    const total = Number(livro?.numeroPaginas) || 0;

    const porcentagem = total > 0
      ? Math.min(100, Math.round((leitura.paginasLidas / total) * 100))
      : null;

    return `
      <article class="diary-card">
        <div class="w-100">
          <span class="eyebrow">
            ${leitura.dataTermino ? "CONCLUÍDA" : "EM ANDAMENTO"}
          </span>

          <h2>${escaparDiario(titulo)}</h2>
          <p>
            ${leitura.paginasLidas}
            ${total ? `de ${total}` : ""} páginas lidas
          </p>

          ${porcentagem === null ? "" : `
            <div class="progress mb-3"
                 role="progressbar"
                 aria-label="Progresso da leitura"
                 aria-valuenow="${porcentagem}"
                 aria-valuemin="0" aria-valuemax="100">
              <div class="progress-bar"
                   style="width: ${porcentagem}%"></div>
            </div>
          `}

          <p>
            Início: ${formatarDataDiario(leitura.dataInicio)}
            · Término: ${formatarDataDiario(leitura.dataTermino)}
            ${leitura.relido ? " · Releitura" : ""}
          </p>

          <div class="d-flex flex-wrap gap-2">
            <button class="btn btn-outline-moss" type="button"
                    data-action="editar" data-id="${Number(leitura.id)}">
              Editar / atualizar progresso
            </button>

            <button class="btn btn-outline-danger" type="button"
                    data-action="excluir" data-id="${Number(leitura.id)}">
              Excluir
            </button>
          </div>
        </div>
      </article>
    `;
  }).join("");

  mensagemDiario.textContent = resultados.length
    ? `${resultados.length} leitura(s)`
    : "Nenhuma leitura encontrada.";
}

async function abrirLeitura(id = null) {
  if (abrindoFormulario) return;

  abrindoFormulario = true;
  botaoNova.disabled = true;

  try {
    await carregarLivrosDiario();

    const leitura = id === null
      ? null
      : leituras.find((item) => Number(item.id) === id);

    if (id !== null && !leitura) {
      throw new Error("Leitura não encontrada. Recarregue a página.");
    }

    if (!livros.length && leitura === null) {
      throw new Error("Cadastre um livro no catálogo antes de iniciar.");
    }

    editandoId = id;
    formularioLeitura.reset();

    const campos = formularioLeitura.elements;

    campos.livroId.innerHTML =
      '<option value="">Selecione...</option>' +
      livros.map((livro) => `
        <option value="${Number(livro.id)}">
          ${escaparDiario(livro.titulo)}
        </option>
      `).join("");

    campos.livroId.value = leitura?.livroId || "";
    campos.dataInicio.value =
      leitura?.dataInicio || dataAtualDiario();
    campos.dataTermino.value = leitura?.dataTermino || "";
    campos.paginasLidas.value = leitura?.paginasLidas ?? 0;
    campos.relido.checked = Boolean(leitura?.relido);

    campos.dataInicio.max = dataAtualDiario();
    campos.dataTermino.max = dataAtualDiario();

    document.querySelector("#tituloLeitura").textContent =
      leitura ? "Editar leitura" : "Adicionar leitura";

    erroLeitura.classList.add("d-none");
    atualizarPaginasConclusao();
    modalLeitura.show();
  } catch (erro) {
    mensagemDiario.textContent = erro.message;
  } finally {
    abrindoFormulario = false;
    botaoNova.disabled = false;
  }
}

// Eventos
botaoNova.addEventListener("click", () => abrirLeitura());
filtroDiario.addEventListener("change", renderizarDiario);

areaDiario.addEventListener("click", (evento) => {
  const botao = evento.target.closest("button[data-action]");
  if (!botao) return;

  const id = Number(botao.dataset.id);

  if (botao.dataset.action === "editar") {
    abrirLeitura(id);
  } else if (botao.dataset.action === "excluir") {
    const leitura = leituras.find((item) => Number(item.id) === id);
    if (!leitura) return;

    excluindoId = id;

    document.querySelector("#descricaoExcluirLeitura").textContent =
      `Deseja excluir a leitura de "${
        encontrarLivro(leitura.livroId)?.titulo || "Livro #" + leitura.livroId
      }"?`;

    erroExcluir.classList.add("d-none");
    modalExcluir.show();
  }
});

formularioLeitura.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  atualizarPaginasConclusao();
  if (!formularioLeitura.reportValidity()) return;

  const campos = formularioLeitura.elements;

  const dados = {
    livroId: Number(campos.livroId.value),
    dataInicio: campos.dataInicio.value,
    dataTermino: campos.dataTermino.value || null,
    paginasLidas: Number(campos.paginasLidas.value),
    relido: campos.relido.checked,
  };

  erroLeitura.classList.add("d-none");

  if (dados.dataTermino && dados.dataTermino < dados.dataInicio) {
    erroLeitura.textContent =
      "A data de término não pode ser anterior ao início.";
    erroLeitura.classList.remove("d-none");
    return;
  }

  const livro = encontrarLivro(dados.livroId);

  if (livro && dados.paginasLidas > livro.numeroPaginas) {
    erroLeitura.textContent =
      "As páginas lidas não podem ultrapassar o total do livro.";
    erroLeitura.classList.remove("d-none");
    return;
  }

  botaoSalvar.disabled = true;

  try {
    await requisicaoDiario(
      editandoId === null ? "/api/diarios" : `/api/diarios/${editandoId}`,
      {
        method: editandoId === null ? "POST" : "PUT",
        headers: cabecalhosDiario(),
        body: JSON.stringify(dados),
      }
    );
  } catch (erro) {
    erroLeitura.textContent = erro.message;
    erroLeitura.classList.remove("d-none");
    return;
  } finally {
    botaoSalvar.disabled = false;
  }

  modalLeitura.hide();

  try {
    await carregarLeituras();
  } catch (erro) {
    mensagemDiario.textContent =
      "Leitura salva. Recarregue a página para atualizar o diário.";
  }
});

confirmarExcluir.addEventListener("click", async () => {
  if (excluindoId === null) return;

  confirmarExcluir.disabled = true;
  erroExcluir.classList.add("d-none");

  try {
    await requisicaoDiario(`/api/diarios/${excluindoId}`, {
      method: "DELETE",
      headers: cabecalhosDiario(),
    });
  } catch (erro) {
    erroExcluir.textContent = erro.message;
    erroExcluir.classList.remove("d-none");
    return;
  } finally {
    confirmarExcluir.disabled = false;
  }

  excluindoId = null;
  modalExcluir.hide();

  try {
    await carregarLeituras();
  } catch (erro) {
    mensagemDiario.textContent =
      "Leitura excluída. Recarregue a página para atualizar o diário.";
  }
});

function atualizarPaginasConclusao() {
  const campos = formularioLeitura.elements;
  const livro = encontrarLivro(campos.livroId.value);
  const concluida = Boolean(campos.dataTermino.value);
  const total = Number(livro?.numeroPaginas);

  campos.paginasLidas.readOnly = concluida && total > 0;

  if (concluida && total > 0) {
    campos.paginasLidas.value = total;
  }
}

formularioLeitura.elements.dataTermino.addEventListener(
  "change",
  atualizarPaginasConclusao
);

formularioLeitura.elements.livroId.addEventListener(
  "change",
  atualizarPaginasConclusao
);

// Inicialização
async function iniciarDiario() {
  mensagemDiario.textContent = "Carregando leituras...";

  try {
    await carregarLivrosDiario();
    await carregarLeituras();
  } catch (erro) {
    mensagemDiario.textContent = erro.message;
  }
}

iniciarDiario();