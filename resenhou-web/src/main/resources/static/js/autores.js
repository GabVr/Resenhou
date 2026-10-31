// Elementos da página e estado
const tabelaAutores = document.querySelector("#tableBody");
const buscaAutores = document.querySelector("#search");
const contadorAutores = document.querySelector("#count");
const formularioAutor = document.querySelector("#editForm");
const erroAutor = document.querySelector("#formError");
const modalAutor = new bootstrap.Modal("#editModal");
const botaoSalvarAutor = formularioAutor.querySelector('[type="submit"]');
let autores = [];
let autorEmEdicao = null;
let autorParaExcluir = null;
const modalExcluir = new bootstrap.Modal("#deleteModal");
const confirmarExclusao = document.querySelector("#confirmDelete");

// Requisições: os dados de autores são persistidos pela API.
async function buscarAutores() {
  const resposta = await fetch("/api/autores");
  if (!resposta.ok) {
    throw new Error("Não foi possível carregar os autores: HTTP " + resposta.status);
  }
  autores = await resposta.json();
  renderizarAutores();
}

async function salvarAutor(autor) {
  const csrf = document.querySelector('meta[name="_csrf"]').content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
  const editando = autorEmEdicao !== null;
  const url = editando
    ? `/api/autores/${autorEmEdicao}`
    : "/api/autores";

  const resposta = await fetch(url, {
    method: editando ? "PUT" : "POST",
    headers: {
      "Content-Type": "application/json",
      [csrfHeader]: csrf,
    },
    body: JSON.stringify(autor),
  });

  if (!resposta.ok) {
    throw new Error("Não foi possível salvar: HTTP " + resposta.status);
  }
}

async function excluirAutor(id) {
  const csrf = document.querySelector('meta[name="_csrf"]').content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

  const resposta = await fetch(`/api/autores/${id}`, {
    method: "DELETE",
    headers: {
      [csrfHeader]: csrf,
    },
  });

  if (!resposta.ok) {
    throw new Error("Não foi possível excluir: HTTP " + resposta.status);
  }
}

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
    (c) => caracteres[c]
  );
}

function renderizarAutores() {
  const busca = buscaAutores.value.trim().toLocaleLowerCase("pt-BR");
  const encontrados = autores.filter((autor) =>
    [autor.nome, autor.dataNascimento, autor.nacionalidade, autor.biografia]
      .join(" ").toLocaleLowerCase("pt-BR").includes(busca),
  );

  document.querySelector("#tableHead").innerHTML =
    '<tr><th>Nome</th><th>Nascimento</th><th>Nacionalidade</th><th class="text-end">Ações</th></tr>';
  tabelaAutores.innerHTML = encontrados.map((autor) => `
    <tr>
      <td class="crud-primary">${escaparHtml(autor.nome)}</td>
      <td>${escaparHtml(autor.dataNascimento)}</td>
      <td>${escaparHtml(autor.nacionalidade)}</td>
      <td><div class="crud-actions">
      <button class="crud-action" data-action="editar" data-id="${autor.id}">Editar</button>
      <button class="crud-action delete" data-action="excluir" data-id="${autor.id}">Excluir</button>
      </div></td>
    </tr>`).join("");
  contadorAutores.textContent = `${encontrados.length} registro(s)`;
  document.querySelector("#empty").classList.toggle("d-none", encontrados.length > 0);
}

function abrirCadastroAutor() {
  autorEmEdicao = null;
  document.querySelector("#modalTitle").textContent = "Cadastrar autor";
  document.querySelector("#formFields").innerHTML = `
    <div class="col-md-6">
      <label class="form-label" for="autorNome">Nome</label>
      <input class="form-control" id="autorNome" name="nome" required>
    </div>
    <div class="col-md-6">
      <label class="form-label" for="autorNascimento">Data de nascimento</label>
      <input class="form-control" id="autorNascimento" name="dataNascimento" type="date" required>
    </div>
    <div class="col-md-6">
      <label class="form-label" for="autorNacionalidade">Nacionalidade</label>
      <input class="form-control" id="autorNacionalidade" name="nacionalidade" required>
    </div>
    <div class="col-12">
      <label class="form-label" for="autorBiografia">Biografia</label>
      <textarea class="form-control" id="autorBiografia" name="biografia" rows="3" required></textarea>
    </div>`;
  erroAutor.classList.add("d-none");
  modalAutor.show();
}

function abrirEdicaoAutor(id) {
  const autor = autores.find((item) => item.id === id);
  if (!autor) return;

  abrirCadastroAutor();
  autorEmEdicao = id;

  document.querySelector("#modalTitle").textContent = "Editar autor";

  const campos = formularioAutor.elements;
  campos.nome.value = autor.nome;
  campos.dataNascimento.value = autor.dataNascimento;
  campos.nacionalidade.value = autor.nacionalidade;
  campos.biografia.value = autor.biografia;
}

// Eventos
document.querySelector("#newBtn").addEventListener("click", abrirCadastroAutor);
buscaAutores.addEventListener("input", renderizarAutores);
tabelaAutores.addEventListener("click", (evento) => {
  const botao = evento.target.closest("[data-action]");
  if (!botao) return;

  const id = Number(botao.dataset.id);

  if (botao.dataset.action === "editar") {
    abrirEdicaoAutor(id);
  } else if (botao.dataset.action === "excluir") {
    autorParaExcluir = id;

    const autor = autores.find((item) => item.id === id);
    document.querySelector("#deleteDescription").textContent =
      `Deseja excluir o autor "${autor?.nome || ""}"?`;

    modalExcluir.show();
  }
});

confirmarExclusao.addEventListener("click", async () => {
  if (autorParaExcluir === null) return;

  confirmarExclusao.disabled = true;

  try {
    await excluirAutor(autorParaExcluir);
  } catch (erro) {
    document.querySelector("#deleteDescription").textContent = erro.message;
    return;
  } finally {
    confirmarExclusao.disabled = false;
  }

  autorParaExcluir = null;
  modalExcluir.hide();

  try {
    await buscarAutores();
  } catch (erro) {
    contadorAutores.textContent =
      "Autor excluído. Recarregue a página para atualizar a tabela.";
    console.error(erro);
  }
});

formularioAutor.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  if (!formularioAutor.reportValidity()) return;
  const campos = formularioAutor.elements;
  const autor = {
    nome: campos.nome.value.trim(),
    dataNascimento: campos.dataNascimento.value,
    nacionalidade: campos.nacionalidade.value.trim(),
    biografia: campos.biografia.value.trim(),
  };

  erroAutor.classList.add("d-none");
  botaoSalvarAutor.disabled = true;
  try {
    await salvarAutor(autor);
  } catch (erro) {
    erroAutor.textContent = erro.message;
    erroAutor.classList.remove("d-none");
    return;
  } finally {
    botaoSalvarAutor.disabled = false;
  }

  modalAutor.hide();
  try {
    await buscarAutores();
  } catch (erro) {
    contadorAutores.textContent = "Autor salvo, mas não foi possível atualizar a tabela. Recarregue a página.";
    console.error(erro);
  }
});



// Inicialização
buscarAutores().catch((erro) => {
  contadorAutores.textContent = erro.message;
  console.error(erro);
});
