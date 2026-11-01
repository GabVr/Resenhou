// Elementos da página e estado
const tabelaEditoras = document.querySelector("#tableBody");
const buscaEditoras = document.querySelector("#search");
const contadorEditoras = document.querySelector("#count");
const formularioEditora = document.querySelector("#editForm");
const erroEditora = document.querySelector("#formError");
const modalEditora = new bootstrap.Modal("#editModal");
const botaoSalvarEditora = formularioEditora.querySelector('[type="submit"]');
let editoras = [];
let editoraEmEdicao = null;
let editoraParaExcluir = null;
const modalExcluir = new bootstrap.Modal("#deleteModal");
const confirmarExclusao = document.querySelector("#confirmDelete");

// Requisições: os dados de editoras são persistidos pela API.
async function buscarEditoras() {
  const resposta = await fetch("/api/editoras");
  if (!resposta.ok) {
    throw new Error("Não foi possível carregar as editoras: HTTP " + resposta.status);
  }
  editoras = await resposta.json();
  renderizarEditoras();
}

async function salvarEditora(editora) {
  const csrf = document.querySelector('meta[name="_csrf"]').content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
  const editando = editoraEmEdicao !== null;
  const url = editando
    ? `/api/editoras/${editoraEmEdicao}`
    : "/api/editoras";

  const resposta = await fetch(url, {
    method: editando ? "PUT" : "POST",
    headers: {
      "Content-Type": "application/json",
      [csrfHeader]: csrf,
    },
    body: JSON.stringify(editora),
  });

  if (!resposta.ok) {
    throw new Error("Não foi possível salvar: HTTP " + resposta.status);
  }
}

async function excluirEditora(id) {
  const csrf = document.querySelector('meta[name="_csrf"]').content;
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

  const resposta = await fetch(`/api/editoras/${id}`, {
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

function renderizarEditoras() {
  const busca = buscaEditoras.value.trim().toLocaleLowerCase("pt-BR");
  const encontrados = editoras.filter((editora) =>
    [editora.nome, editora.pais, editora.site]
      .join(" ").toLocaleLowerCase("pt-BR").includes(busca),
  );

  document.querySelector("#tableHead").innerHTML =
    '<tr><th>Nome</th><th>Pais</th><th>Site</th><th class="text-end">Ações</th></tr>';
  tabelaEditoras.innerHTML = encontrados.map((editora) => `
    <tr>
      <td class="crud-primary">${escaparHtml(editora.nome)}</td>
      <td>${escaparHtml(editora.pais)}</td>
      <td>${escaparHtml(editora.site)}</td>
      <td><div class="crud-actions">
      <button class="crud-action" data-action="editar" data-id="${editora.id}">Editar</button>
      <button class="crud-action delete" data-action="excluir" data-id="${editora.id}">Excluir</button>
      </div></td>
    </tr>`).join("");
  contadorEditoras.textContent = `${encontrados.length} registro(s)`;
  document.querySelector("#empty").classList.toggle("d-none", encontrados.length > 0);
}

function abrirCadastroEditora() {
  editoraEmEdicao = null;
  document.querySelector("#modalTitle").textContent = "Cadastrar editora";
  document.querySelector("#formFields").innerHTML = `
    <div class="col-md-6">
      <label class="form-label" for="editoraNome">Nome</label>
      <input class="form-control" id="editoraNome" name="nome" required>
    </div>
    <div class="col-md-6">
      <label class="form-label" for="editoraPais">País</label>
      <input class="form-control" id="editoraPais" name="pais" required>
    </div>
    <div class="col-md-6">
      <label class="form-label" for="editoraSite">Site</label>
      <input class="form-control" id="editoraSite" name="site" required>
    </div>`;
  erroEditora.classList.add("d-none");
  modalEditora.show();
}

function abrirEdicaoEditora(id) {
  const editora = editoras.find((item) => item.id === id);
  if (!editora) return;

  abrirCadastroEditora();
  editoraEmEdicao = id;

  document.querySelector("#modalTitle").textContent = "Editar editora";

  const campos = formularioEditora.elements;
  campos.nome.value = editora.nome;
  campos.pais.value = editora.pais;
  campos.site.value = editora.site;
}

// Eventos
document.querySelector("#newBtn").addEventListener("click", abrirCadastroEditora);
buscaEditoras.addEventListener("input", renderizarEditoras);
tabelaEditoras.addEventListener("click", (evento) => {
  const botao = evento.target.closest("[data-action]");
  if (!botao) return;

  const id = Number(botao.dataset.id);

  if (botao.dataset.action === "editar") {
    abrirEdicaoEditora(id);
  } else if (botao.dataset.action === "excluir") {
    editoraParaExcluir = id;

    const editora = editoras.find((item) => item.id === id);
    document.querySelector("#deleteDescription").textContent =
      `Deseja excluir a editora "${editora?.nome || ""}"?`;

    modalExcluir.show();
  }
});

confirmarExclusao.addEventListener("click", async () => {
  if (editoraParaExcluir === null) return;

  confirmarExclusao.disabled = true;

  try {
    await excluirEditora(editoraParaExcluir);
  } catch (erro) {
    document.querySelector("#deleteDescription").textContent = erro.message;
    return;
  } finally {
    confirmarExclusao.disabled = false;
  }

  editoraParaExcluir = null;
  modalExcluir.hide();

  try {
    await buscarEditoras();
  } catch (erro) {
    contadorEditoras.textContent =
      "Editora excluída. Recarregue a página para atualizar a tabela.";
    console.error(erro);
  }
});

formularioEditora.addEventListener("submit", async (evento) => {
  evento.preventDefault();
  if (!formularioEditora.reportValidity()) return;
  const campos = formularioEditora.elements;
  const editora = {
    nome: campos.nome.value.trim(),
    pais: campos.pais.value.trim(),
    site: campos.site.value.trim(),
  };

  erroEditora.classList.add("d-none");
  botaoSalvarEditora.disabled = true;
  try {
    await salvarEditora(editora);
  } catch (erro) {
    erroEditora.textContent = erro.message;
    erroEditora.classList.remove("d-none");
    return;
  } finally {
    botaoSalvarEditora.disabled = false;
  }

  modalEditora.hide();
  try {
    await buscarEditoras();
  } catch (erro) {
    contadorEditoras.textContent = "Editora salva, mas não foi possível atualizar a tabela. Recarregue a página.";
    console.error(erro);
  }
});

// Inicialização
buscarEditoras().catch((erro) => {
  contadorEditoras.textContent = erro.message;
  console.error(erro);
});
