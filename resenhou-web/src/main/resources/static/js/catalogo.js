// Elementos e estado
const formularioFiltros = document.querySelector("#catalogFilters");
const campoBusca = document.querySelector("#catalogSearch");
const campoGenero = document.querySelector("#catalogGenre");
const areaResultados = document.querySelector("#catalogResults");
const contador = document.querySelector("#resultCount");

const formularioLivro = document.querySelector("#editForm");
const camposLivro = document.querySelector("#formFields");
const erroLivro = document.querySelector("#formError");
const botaoSalvar = formularioLivro.querySelector('[type="submit"]');
const botaoNovo = document.querySelector("#newBtn");
const modalLivro = new bootstrap.Modal("#editModal");

const modalExcluir = new bootstrap.Modal("#deleteModal");
const confirmarExclusao = document.querySelector("#confirmDelete");
const descricaoExclusao = document.querySelector("#deleteDescription");

let livros = [];
let autoresDisponiveis = [];
let editorasDisponiveis = [];
let livroEmEdicao = null;
let livroParaExcluir = null;
let carregandoFormulario = false;
let generosDisponiveis = [];

// Requisições
async function requisicao(url, opcoes = {}) {
  const resposta = await fetch(url, opcoes);

  if (resposta.status === 401 || resposta.redirected) {
    throw new Error("Sua sessão expirou. Faça login novamente.");
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
    throw new Error("Recarregue a página para obter o token de segurança.");
  }

  return { [nome]: token };
}

async function buscarLivros() {
  const resposta = await requisicao("/api/livros");
  livros = await resposta.json();

  preencherGeneros();
  filtrar();
}

async function buscarCadastros() {
  const [respostaAutores, respostaEditoras, respostaGeneros] =
    await Promise.all([
      requisicao("/api/autores"),
      requisicao("/api/editoras"),
      requisicao("/api/generos"),
    ]);

  [autoresDisponiveis, editorasDisponiveis, generosDisponiveis] =
    await Promise.all([
      respostaAutores.json(),
      respostaEditoras.json(),
      respostaGeneros.json(),
    ]);
}

async function salvarLivro(livro) {
  const editando = livroEmEdicao !== null;
  const url = editando
    ? `/api/livros/${livroEmEdicao}`
    : "/api/livros";

  await requisicao(url, {
    method: editando ? "PUT" : "POST",
    headers: {
      "Content-Type": "application/json",
      ...cabecalhosCsrf(),
    },
    body: JSON.stringify(livro),
  });
}

async function excluirLivro(id) {
  await requisicao(`/api/livros/${id}`, {
    method: "DELETE",
    headers: cabecalhosCsrf(),
  });
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

function preencherGeneros() {
  const selecionado = campoGenero.value;

  const generos = [
    ...new Set(
      livros.flatMap((livro) =>
        (livro.generos || []).map((genero) => genero.nome)
      )
    ),
  ].filter(Boolean);

  campoGenero.innerHTML = '<option value="">Todos os gêneros</option>';

  generos
    .sort((a, b) => a.localeCompare(b, "pt-BR"))
    .forEach((genero) => {
      const opcao = document.createElement("option");
      opcao.value = genero;
      opcao.textContent = genero;
      campoGenero.appendChild(opcao);
    });

  campoGenero.value = generos.includes(selecionado) ? selecionado : "";
}

function criarCardLivro(livro) {
  const nomesAutores = (livro.autores || [])
    .map((autor) => autor.nome)
    .join(", ");

  return `
    <div class="col">
      <article class="book-card" data-book-id="${Number(livro.id)}">
        <div class="cover-wrap">
          <div class="cover cover-terra">
            <span class="cover-kicker">
              ${escaparHtml(livro.editora?.nome || "")}
            </span>
            <strong>${escaparHtml(livro.titulo)}</strong>
            <small>${escaparHtml(nomesAutores)}</small>
          </div>
        </div>

        <h3>${escaparHtml(livro.titulo)}</h3>
        <p>${escaparHtml(nomesAutores || "Autor não informado")}</p>

        <div class="d-flex flex-wrap gap-2 mt-3">
          <button type="button" class="btn btn-outline-moss btn-sm"
                  data-action="editar" data-id="${Number(livro.id)}">
            Editar
          </button>
          <button type="button" class="btn btn-outline-danger btn-sm"
                  data-action="excluir" data-id="${Number(livro.id)}">
            Excluir
          </button>
        </div>
      </article>
    </div>
  `;
}

function filtrar() {
  const texto = campoBusca.value.trim().toLocaleLowerCase("pt-BR");
  const genero = campoGenero.value;

  const encontrados = livros.filter((livro) => {
    const autores = (livro.autores || [])
      .map((autor) => autor.nome)
      .join(" ");

    const textoLivro = `${livro.titulo || ""} ${autores}`
      .toLocaleLowerCase("pt-BR");

    return (
      textoLivro.includes(texto) &&
      (!genero ||
        (livro.generos || []).some((item) => item.nome === genero))
    );
  });

  areaResultados.innerHTML = encontrados.length
    ? encontrados.map(criarCardLivro).join("")
    : '<p class="text-muted">Nenhum livro encontrado.</p>';

  contador.textContent = `${encontrados.length} livros encontrados`;
}

function montarFormulario(livro) {
  camposLivro.innerHTML = `
    <div class="col-md-6">
      <label class="form-label" for="livroNome">Nome</label>
      <input class="form-control" id="livroNome" name="nome" required>
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroTitulo">Título</label>
      <input class="form-control" id="livroTitulo" name="titulo" required>
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroOriginal">Título original</label>
      <input class="form-control" id="livroOriginal"
             name="tituloOriginal" required>
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroIsbn">ISBN</label>
      <input class="form-control" id="livroIsbn" name="isbn" required>
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroPublicacao">Publicação</label>
      <input class="form-control" id="livroPublicacao"
             name="dataPublicacao" type="date" required>
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroPaginas">Número de páginas</label>
      <input class="form-control" id="livroPaginas"
             name="numeroPaginas" type="number" min="1" step="1" required>
    </div>

    <div class="col-12">
      <label class="form-label" for="livroEditora">Editora</label>
      <select class="form-select" id="livroEditora" name="editora" required>
        <option value="">Selecione...</option>
        ${editorasDisponiveis.map((editora) => `
          <option value="${Number(editora.id)}">
            ${escaparHtml(editora.nome)}
          </option>
        `).join("")}
      </select>
    </div>

    <div class="col-12">
      <label class="form-label" for="livroAutores">Autores</label>
      <select class="form-select" id="livroAutores"
              name="autores" multiple size="4" required>
        ${autoresDisponiveis.map((autor) => `
          <option value="${Number(autor.id)}">
            ${escaparHtml(autor.nome)}
          </option>
        `).join("")}
      </select>
      <small class="text-muted">
        Segure Ctrl ou Cmd para selecionar vários autores.
      </small>
    </div>

    <div class="col-12">
      <label class="form-label" for="buscaGeneros">Gêneros e temas</label>
      <input class="form-control mb-2" id="buscaGeneros"
            type="search" placeholder="Buscar gênero ou tema...">

      <div id="opcoesGeneros"
          class="border rounded p-3"
          style="max-height: 220px; overflow-y: auto;">
        ${generosDisponiveis.map((genero) => `
          <label class="d-flex align-items-center gap-2 mb-2">
            <input class="form-check-input m-0"
                  type="checkbox" name="generos"
                  value="${Number(genero.id)}">
            <span>${escaparHtml(genero.nome)}</span>
          </label>
        `).join("")}
      </div>
    </div>

    <div class="col-12">
      <label class="form-label" for="livroSinopse">Sinopse</label>
      <textarea class="form-control" id="livroSinopse"
                name="sinopse" rows="4" required></textarea>
    </div>
  `;

  const campos = formularioLivro.elements;

  if (livro) {
    
    campos.nome.value = livro.nome || "";
    campos.titulo.value = livro.titulo || "";
    campos.tituloOriginal.value = livro.tituloOriginal || "";
    campos.isbn.value = livro.isbn || "";
    campos.dataPublicacao.value = livro.dataPublicacao || "";
    campos.numeroPaginas.value = livro.numeroPaginas || "";
    campos.editora.value = livro.editora?.id || "";
    campos.sinopse.value = livro.sinopse || "";

    const idsAutores = new Set(
      (livro.autores || []).map((autor) => Number(autor.id))
    );

    Array.from(campos.autores.options).forEach((opcao) => {
      opcao.selected = idsAutores.has(Number(opcao.value));
    });

    const idsGeneros = new Set(
      (livro.generos || []).map((genero) => Number(genero.id))
    );

    camposLivro
      .querySelectorAll('input[name="generos"]')
      .forEach((opcao) => {
        opcao.checked = idsGeneros.has(Number(opcao.value));
      });
    }
}

async function abrirFormulario(id = null) {
  if (carregandoFormulario) return;

  carregandoFormulario = true;
  botaoNovo.disabled = true;

  try {
    await buscarCadastros();

    if (!autoresDisponiveis.length || !editorasDisponiveis.length) {
      throw new Error(
        "Cadastre pelo menos um autor e uma editora antes do livro."
      );
    }

    const livro = id === null
      ? null
      : livros.find((item) => item.id === id);

    if (id !== null && !livro) {
      throw new Error("Livro não encontrado. Recarregue a página.");
    }

    livroEmEdicao = id;
    montarFormulario(livro);

    document.querySelector("#modalTitle").textContent =
      livro ? "Editar livro" : "Cadastrar livro";

    erroLivro.classList.add("d-none");
    modalLivro.show();
  } catch (erro) {
    contador.textContent = erro.message;
  } finally {
    carregandoFormulario = false;
    botaoNovo.disabled = false;
  }
}

// Eventos
formularioFiltros.addEventListener("submit", (evento) => {
  evento.preventDefault();
  filtrar();
});

botaoNovo.addEventListener("click", () => abrirFormulario());

areaResultados.addEventListener("click", (evento) => {
  const botao = evento.target.closest("button[data-action]");
  if (!botao) return;

  const id = Number(botao.dataset.id);

  if (botao.dataset.action === "editar") {
    abrirFormulario(id);
  } else if (botao.dataset.action === "excluir") {
    const livro = livros.find((item) => item.id === id);
    if (!livro) return;

    livroParaExcluir = id;
    descricaoExclusao.textContent =
      `Deseja excluir "${livro.titulo}"?`;
    modalExcluir.show();
  }
});

formularioLivro.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  if (!formularioLivro.reportValidity()) return;

  const campos = formularioLivro.elements;


  const livro = {
    nome: campos.nome.value.trim(),
    titulo: campos.titulo.value.trim(),
    tituloOriginal: campos.tituloOriginal.value.trim(),
    isbn: campos.isbn.value.trim(),
    dataPublicacao: campos.dataPublicacao.value,
    numeroPaginas: Number(campos.numeroPaginas.value),
    sinopse: campos.sinopse.value.trim(),
    editora: { id: Number(campos.editora.value) },
    autores: Array.from(campos.autores.selectedOptions).map((opcao) => ({
      id: Number(opcao.value),
    })),
    generos: Array.from(
      camposLivro.querySelectorAll('input[name="generos"]:checked')
    ).map((opcao) => ({
      id: Number(opcao.value),
    })),
  };

  erroLivro.classList.add("d-none");
  botaoSalvar.disabled = true;

  try {
    await salvarLivro(livro);
  } catch (erro) {
    erroLivro.textContent = erro.message;
    erroLivro.classList.remove("d-none");
    return;
  } finally {
    botaoSalvar.disabled = false;
  }

  modalLivro.hide();

  try {
    await buscarLivros();
  } catch (erro) {
    contador.textContent =
      "Livro salvo. Recarregue a página para atualizar o catálogo.";
    console.error(erro);
  }
});

confirmarExclusao.addEventListener("click", async () => {
  if (livroParaExcluir === null) return;

  confirmarExclusao.disabled = true;

  try {
    await excluirLivro(livroParaExcluir);
  } catch (erro) {
    descricaoExclusao.textContent = erro.message;
    return;
  } finally {
    confirmarExclusao.disabled = false;
  }

  livroParaExcluir = null;
  modalExcluir.hide();

  try {
    await buscarLivros();
  } catch (erro) {
    contador.textContent =
      "Livro excluído. Recarregue a página para atualizar o catálogo.";
    console.error(erro);
  }
});

camposLivro.addEventListener("input", (evento) => {
  if (evento.target.id !== "buscaGeneros") return;

  const texto = evento.target.value.trim().toLocaleLowerCase("pt-BR");

  camposLivro.querySelectorAll("#opcoesGeneros label").forEach((rotulo) => {
    const corresponde = rotulo.textContent
      .toLocaleLowerCase("pt-BR")
      .includes(texto);

    rotulo.classList.toggle("d-none", !corresponde);
  });
});

// Inicialização
const buscaDaUrl = new URLSearchParams(location.search).get("busca");

if (buscaDaUrl) {
  campoBusca.value = buscaDaUrl;
}

contador.textContent = "Carregando livros...";

buscarLivros().catch((erro) => {
  contador.textContent = erro.message;
  console.error(erro);
});