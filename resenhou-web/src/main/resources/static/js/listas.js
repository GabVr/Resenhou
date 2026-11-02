// Elementos e estado
const areaListas = document.querySelector("#listasGrid");
const mensagem = document.querySelector("#mensagemListas");
const formulario = document.querySelector("#editForm");
const erroFormulario = document.querySelector("#erroLista");
const opcoesLivros = document.querySelector("#opcoesLivrosLista");
const salvar = document.querySelector("#salvarLista");
const novaLista = document.querySelector("#novaLista");

const modalFormulario = new bootstrap.Modal("#editModal");
const modalExcluir = new bootstrap.Modal("#excluirListaModal");
const confirmarExcluir = document.querySelector("#confirmarExcluirLista");
const erroExcluir = document.querySelector("#erroExcluirLista");

let listas = [];
let livros = [];
let idsProprios = new Set();
let editandoId = null;
let excluindoId = null;
let abrindoFormulario = false;
let numeroConsulta = 0;

// Requisições
async function requisicao(url, opcoes = {}) {
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

function cabecalhosCsrf() {
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

async function carregarListas(nomePublico = null) {
  const consulta = ++numeroConsulta;
  mensagem.textContent = "Carregando listas...";

  try {
    const resposta = await requisicao("/api/listas");
    const minhas = await resposta.json();

    let resultados = minhas;

    if (nomePublico !== null) {
      const respostaPublicas = await requisicao(
        "/api/listas/buscar/nome?nome=" + encodeURIComponent(nomePublico)
      );
      resultados = await respostaPublicas.json();
    }

    // Uma consulta antiga não substitui a mais recente.
    if (consulta !== numeroConsulta) return;

    idsProprios = new Set(minhas.map((lista) => Number(lista.id)));
    listas = resultados;

    renderizarListas();

    mensagem.textContent = nomePublico === null
      ? `${listas.length} lista(s) na sua conta`
      : `${listas.length} lista(s) pública(s) encontrada(s)`;
  } catch (erro) {
    if (consulta !== numeroConsulta) return;
    mensagem.textContent = erro.message;
  }
}

async function carregarLivros() {
  const resposta = await requisicao("/api/livros");
  livros = await resposta.json();
}

// Renderização
function escaparHtml(valor) {
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

function idsLivrosDaLista(lista) {
  const ids = (lista.itens || []).map((item) => Number(item.livroId));

  // Compatibilidade com registros antigos que tinham um único livro.
  if (!ids.length && lista.livroId) {
    ids.push(Number(lista.livroId));
  }

  return ids;
}

function tituloLivro(id) {
  return livros.find((livro) => Number(livro.id) === Number(id))?.titulo
    || `Livro #${id}`;
}

function renderizarListas() {
  areaListas.innerHTML = listas.map((lista) => {
    const propria = idsProprios.has(Number(lista.id));
    const ids = idsLivrosDaLista(lista);

    const capas = ids.slice(0, 3).map((id) => `
      <div class="cover cover-terra">
        <span class="cover-kicker">RESENHOU</span>
        <strong>${escaparHtml(tituloLivro(id))}</strong>
      </div>
    `).join("");

    return `
      <div class="col-md-6">
        <article class="list-card h-100">
          <div class="cover-stack">
            ${capas || '<p class="p-3 text-muted">Nenhum livro adicionado.</p>'}
          </div>

          <div class="list-copy">
            <div>
              <span>${lista.publica ? "PÚBLICA" : "PRIVADA"}</span>
              <h2>${escaparHtml(lista.nome)}</h2>
              <p>${escaparHtml(lista.descricao)}</p>
              <small>${ids.length} livro(s)</small>

              <ul class="mt-3">
                ${ids.map((id) => `
                  <li>${escaparHtml(tituloLivro(id))}</li>
                `).join("")}
              </ul>
            </div>
          </div>

          ${propria ? `
            <div class="d-flex flex-wrap gap-2 p-3">
              <button type="button" class="btn btn-outline-moss"
                      data-action="editar" data-id="${Number(lista.id)}">
                Editar lista e livros
              </button>
              <button type="button" class="btn btn-outline-danger"
                      data-action="excluir" data-id="${Number(lista.id)}">
                Excluir
              </button>
            </div>
          ` : ""}
        </article>
      </div>
    `;
  }).join("");

  if (!listas.length) {
    areaListas.innerHTML =
      '<p class="text-muted">Nenhuma lista encontrada.</p>';
  }
}

async function abrirFormulario(id = null) {
  if (abrindoFormulario) return;

  abrindoFormulario = true;
  novaLista.disabled = true;

  try {
    await carregarLivros();

    const lista = id === null
      ? null
      : listas.find((item) => Number(item.id) === id);

    if (id !== null && (!lista || !idsProprios.has(id))) {
      throw new Error("Não foi possível editar essa lista.");
    }

    editandoId = id;
    formulario.reset();

    formulario.elements.nome.value = lista?.nome || "";
    formulario.elements.descricao.value = lista?.descricao || "";
    formulario.elements.publica.checked = Boolean(lista?.publica);
    formulario.elements.visualicacaoPerfil.checked =
      Boolean(lista?.visualicacaoPerfil);

    document.querySelector("#tituloLista").textContent =
      lista ? "Editar lista" : "Criar lista";

    const selecionados = new Set(lista ? idsLivrosDaLista(lista) : []);

    opcoesLivros.innerHTML = livros.map((livro) => `
      <label class="d-flex align-items-center gap-2 mb-2">
        <input class="form-check-input m-0" type="checkbox"
               name="livroIds" value="${Number(livro.id)}"
               ${selecionados.has(Number(livro.id)) ? "checked" : ""}>
        <span>${escaparHtml(livro.titulo)}</span>
      </label>
    `).join("") || '<p class="text-muted">Nenhum livro no catálogo.</p>';

    erroFormulario.classList.add("d-none");
    modalFormulario.show();
  } catch (erro) {
    mensagem.textContent = erro.message;
  } finally {
    abrindoFormulario = false;
    novaLista.disabled = false;
  }
}

// Eventos
novaLista.addEventListener("click", () => abrirFormulario());

document.querySelector("#minhasListas").addEventListener("click", () => {
  document.querySelector("#nomeListaBusca").value = "";
  carregarListas();
});

document.querySelector("#buscaListas").addEventListener("submit", (evento) => {
  evento.preventDefault();
  const nome = document.querySelector("#nomeListaBusca").value.trim();
  carregarListas(nome);
});

document.querySelector("#buscaLivrosLista").addEventListener("input", (evento) => {
  const busca = evento.target.value.trim().toLocaleLowerCase("pt-BR");

  opcoesLivros.querySelectorAll("label").forEach((rotulo) => {
    rotulo.classList.toggle(
      "d-none",
      !rotulo.textContent.toLocaleLowerCase("pt-BR").includes(busca)
    );
  });
});

areaListas.addEventListener("click", (evento) => {
  const botao = evento.target.closest("button[data-action]");
  if (!botao) return;

  const id = Number(botao.dataset.id);
  if (!idsProprios.has(id)) return;

  if (botao.dataset.action === "editar") {
    abrirFormulario(id);
  } else if (botao.dataset.action === "excluir") {
    const lista = listas.find((item) => Number(item.id) === id);
    if (!lista) return;

    excluindoId = id;
    document.querySelector("#descricaoExcluirLista").textContent =
      `Deseja excluir "${lista.nome}"?`;

    erroExcluir.classList.add("d-none");
    modalExcluir.show();
  }
});

formulario.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  if (!formulario.reportValidity()) return;

  const dados = {
    nome: formulario.elements.nome.value.trim(),
    descricao: formulario.elements.descricao.value.trim(),
    publica: formulario.elements.publica.checked,
    visualicacaoPerfil: formulario.elements.visualicacaoPerfil.checked,
    livroIds: Array.from(
      opcoesLivros.querySelectorAll('input[name="livroIds"]:checked')
    ).map((opcao) => Number(opcao.value)),
  };

  salvar.disabled = true;
  erroFormulario.classList.add("d-none");

  try {
    await requisicao(
      editandoId === null ? "/api/listas" : `/api/listas/${editandoId}`,
      {
        method: editandoId === null ? "POST" : "PUT",
        headers: cabecalhosCsrf(),
        body: JSON.stringify(dados),
      }
    );
  } catch (erro) {
    erroFormulario.textContent = erro.message;
    erroFormulario.classList.remove("d-none");
    return;
  } finally {
    salvar.disabled = false;
  }

  modalFormulario.hide();
  await carregarListas();
});

confirmarExcluir.addEventListener("click", async () => {
  if (excluindoId === null) return;

  confirmarExcluir.disabled = true;
  erroExcluir.classList.add("d-none");

  try {
    await requisicao(`/api/listas/${excluindoId}`, {
      method: "DELETE",
      headers: cabecalhosCsrf(),
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
  await carregarListas();
});

// Inicialização
async function iniciarListas() {
  try {
    await carregarLivros();
    await carregarListas();
  } catch (erro) {
    mensagem.textContent = erro.message;
  }
}

iniciarListas();