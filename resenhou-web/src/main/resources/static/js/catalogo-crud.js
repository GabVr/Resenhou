
/*
 * catalogo-crud.js
 * Formulário de sugestão de livros do Resenhou.
 *
 * Os dados são salvos temporariamente no localStorage.
 * A aprovação real será implementada no backend.
 */

const CHAVE_SUGESTOES = "resenhou-sugestoes-livros";

const modalCadastro = new bootstrap.Modal(
  document.querySelector("#editModal")
);

const formularioCadastro = document.querySelector("#editForm");
const camposCadastro = document.querySelector("#formFields");
const erroCadastro = document.querySelector("#formError");
const tituloModal = document.querySelector("#modalTitle");
const botaoNovoLivro = document.querySelector("#newBtn");


// Recupera os registros de autores e editoras do CRUD.
function carregarCadastros(tipo) {
  try {
    const dados = localStorage.getItem("resenhou-crud-v1-" + tipo);

    if (dados !== null) {
      return JSON.parse(dados);
    }
  } catch (erro) {
    console.error("Erro ao carregar " + tipo, erro);
  }

  // Dados iniciais usados na demonstração.
  if (tipo === "autores") {
    return [
      { id: 1, nome: "Itamar Vieira Junior" },
      { id: 2, nome: "Carla Madeira" },
      { id: 3, nome: "Han Kang" }
    ];
  }

  if (tipo === "editoras") {
    return [
      { id: 1, nome: "Todavia" },
      { id: 2, nome: "Record" }
    ];
  }

  return [];
}


// Recupera as sugestões já enviadas.
function carregarSugestoes() {
  try {
    return JSON.parse(
      localStorage.getItem(CHAVE_SUGESTOES) || "[]"
    );
  } catch (erro) {
    return [];
  }
}


// Salva as sugestões no navegador.
function salvarSugestoes(sugestoes) {
  localStorage.setItem(
    CHAVE_SUGESTOES,
    JSON.stringify(sugestoes)
  );
}


// Monta as opções dos autores.
function montarAutores() {
  const autores = carregarCadastros("autores");

  return autores.map((autor) => `
    <option value="${autor.id}">
      ${esc(autor.nome)}
    </option>
  `).join("");
}


// Monta as opções das editoras.
function montarEditoras() {
  const editoras = carregarCadastros("editoras");

  return editoras.map((editora) => `
    <option value="${editora.id}">
      ${esc(editora.nome)}
    </option>
  `).join("");
}


// Abre o formulário de sugestão.
function abrirCadastroLivro() {
  formularioCadastro.reset();
  erroCadastro.classList.add("d-none");

  tituloModal.textContent = "Sugerir livro";

  camposCadastro.innerHTML = `
    <div class="col-md-6">
      <label class="form-label" for="livroTitulo">
        Título
      </label>
      <input
        class="form-control"
        type="text"
        id="livroTitulo"
        name="titulo"
        required
      >
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroTituloOriginal">
        Título original
      </label>
      <input
        class="form-control"
        type="text"
        id="livroTituloOriginal"
        name="tituloOriginal"
        required
      >
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroIsbn">
        ISBN
      </label>
      <input
        class="form-control"
        type="text"
        id="livroIsbn"
        name="isbn"
        required
      >
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroData">
        Data de publicação
      </label>
      <input
        class="form-control"
        type="date"
        id="livroData"
        name="dataPublicacao"
        required
      >
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroPaginas">
        Número de páginas
      </label>
      <input
        class="form-control"
        type="number"
        id="livroPaginas"
        name="numeroPaginas"
        min="1"
        required
      >
    </div>

    <div class="col-md-6">
      <label class="form-label" for="livroEditora">
        Editora
      </label>
      <select
        class="form-select"
        id="livroEditora"
        name="editora"
        required
      >
        <option value="">Selecione...</option>
        ${montarEditoras()}
      </select>
    </div>

    <div class="col-12">
      <label class="form-label" for="livroAutores">
        Autores
      </label>
      <select
        class="form-select"
        id="livroAutores"
        name="autores"
        multiple
        size="4"
        required
      >
        ${montarAutores()}
      </select>
      <small class="text-muted">
        Segure Ctrl para selecionar mais de um autor.
      </small>
    </div>

    <div class="col-12">
      <label class="form-label" for="livroGeneros">
        Gêneros
      </label>
      <input
        class="form-control"
        type="text"
        id="livroGeneros"
        name="generos"
        placeholder="Romance, Ficção, Aventura..."
      >
      <small class="text-muted">
        Separe os gêneros por vírgula.
      </small>
    </div>

    <div class="col-12">
      <label class="form-label" for="livroSinopse">
        Sinopse
      </label>
      <textarea
        class="form-control"
        id="livroSinopse"
        name="sinopse"
        rows="4"
        required
      ></textarea>
    </div>
  `;

  modalCadastro.show();
}


// Evento do botão Sugerir livro.
botaoNovoLivro.addEventListener("click", abrirCadastroLivro);


// Evento de envio do formulário.
formularioCadastro.addEventListener("submit", (evento) => {
  evento.preventDefault();

  if (!formularioCadastro.reportValidity()) {
    return;
  }

  const form = formularioCadastro.elements;

  const autoresSelecionados = Array.from(
    form["autores"].selectedOptions
  ).map((opcao) => Number(opcao.value));

  if (autoresSelecionados.length === 0) {
    erroCadastro.textContent = "Selecione pelo menos um autor.";
    erroCadastro.classList.remove("d-none");
    return;
  }

  const sugestoes = carregarSugestoes();

  const isbn = form["isbn"].value.trim();

  // Impede sugestões duplicadas pelo ISBN.
  const existe = sugestoes.some(
    (livro) => livro.isbn === isbn
  );

  if (existe) {
    erroCadastro.textContent =
      "Já existe uma sugestão com esse ISBN.";
    erroCadastro.classList.remove("d-none");
    return;
  }

  const novoLivro = {
    id: Date.now(),
    nome: form["titulo"].value.trim(),
    titulo: form["titulo"].value.trim(),
    tituloOriginal: form["tituloOriginal"].value.trim(),
    isbn: isbn,
    dataPublicacao: form["dataPublicacao"].value,
    numeroPaginas: Number(form["numeroPaginas"].value),
    autores: autoresSelecionados,
    editora: Number(form["editora"].value),
    generos: form["generos"].value
      .split(",")
      .map((genero) => genero.trim())
      .filter(Boolean),
    sinopse: form["sinopse"].value.trim(),
    status: "PENDENTE"
  };

  sugestoes.push(novoLivro);
  salvarSugestoes(sugestoes);

  modalCadastro.hide();
  formularioCadastro.reset();

  alert(
    "Livro enviado para aprovação! " +
    "Sua sugestão foi registrada nesta demonstração."
  );
});
