// Elementos e estado
const nomePerfil = document.querySelector("#nomePerfil");
const usernamePerfil = document.querySelector("#usernamePerfil");
const biografiaPerfil = document.querySelector("#biografiaPerfil");
const mensagemPerfil = document.querySelector("#mensagemPerfil");

const formularioPerfil = document.querySelector("#perfilForm");
const editarPerfil = document.querySelector("#editarPerfil");
const salvarPerfil = document.querySelector("#salvarPerfil");
const erroPerfil = document.querySelector("#erroPerfil");

const modalPerfil = new bootstrap.Modal("#perfilModal");

let perfil = null;

// Requisições
async function requisicaoPerfil(opcoes = {}) {
  const resposta = await fetch("/api/perfil", opcoes);

  if (resposta.redirected || resposta.status === 401) {
    throw new Error("Sua sessão expirou. Faça login novamente.");
  }

  if (!resposta.ok) {
    throw new Error(
      "Não foi possível concluir a operação: HTTP " + resposta.status
    );
  }

  return resposta.json();
}

function escaparTextoPerfil(valor) {
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

async function carregarListasPerfil() {
  const area = document.querySelector("#listasPerfil");
  const mensagem = document.querySelector("#mensagemListasPerfil");

  try {
    const resposta = await fetch("/api/listas");

    if (resposta.redirected || resposta.status === 401) {
      throw new Error("Faça login novamente para consultar suas listas.");
    }

    if (!resposta.ok) {
      throw new Error("Não foi possível carregar suas listas.");
    }

    const listas = await resposta.json();

    mensagem.textContent = listas.length
      ? `${listas.length} lista(s) na sua conta`
      : "Você ainda não criou nenhuma lista.";

    area.innerHTML = listas.map((lista) => {
      const quantidade = lista.itens?.length ||
        (lista.livroId ? 1 : 0);

      return `
        <div class="col-md-6 col-lg-4">
          <article class="card h-100">
            <div class="card-body">
              <span class="badge text-bg-light mb-2">
                ${lista.publica ? "Pública" : "Privada"}
              </span>

              <h3 class="h5">${escaparTextoPerfil(lista.nome)}</h3>
              <p>${escaparTextoPerfil(lista.descricao)}</p>
              <small class="text-muted">${quantidade} livro(s)</small>
            </div>
          </article>
        </div>
      `;
    }).join("");
  } catch (erro) {
    mensagem.textContent = erro.message;
    area.innerHTML = "";
  }
}

function cabecalhosPerfil() {
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

// Renderização
function mostrarPerfil() {
  nomePerfil.textContent = perfil.nome;
  usernamePerfil.textContent = "@" + perfil.username;
  biografiaPerfil.textContent =
    perfil.biografia || "Você ainda não adicionou uma biografia.";

  const partes = perfil.nome.trim().split(/\s+/);
  const iniciais = (
    partes[0].charAt(0) +
    (partes.length > 1 ? partes[partes.length - 1].charAt(0) : "")
  ).toLocaleUpperCase("pt-BR");

  document.querySelector("#avatarPerfil").textContent = iniciais;
  document.querySelector("#avatarMenu").textContent = iniciais;

  editarPerfil.disabled = false;
}

// Eventos
editarPerfil.addEventListener("click", () => {
  if (!perfil) return;

  formularioPerfil.elements.nome.value = perfil.nome;
  formularioPerfil.elements.biografia.value = perfil.biografia || "";

  erroPerfil.classList.add("d-none");
  mensagemPerfil.textContent = "";
  modalPerfil.show();
});

formularioPerfil.addEventListener("submit", async (evento) => {
  evento.preventDefault();

  if (!formularioPerfil.reportValidity()) return;

  const nome = formularioPerfil.elements.nome.value.trim();
  const biografia = formularioPerfil.elements.biografia.value.trim();

  if (!nome) {
    erroPerfil.textContent = "Informe seu nome.";
    erroPerfil.classList.remove("d-none");
    return;
  }

  salvarPerfil.disabled = true;
  erroPerfil.classList.add("d-none");

  try {
    perfil = await requisicaoPerfil({
      method: "PUT",
      headers: cabecalhosPerfil(),
      body: JSON.stringify({
        nome,
        biografia: biografia || null,
      }),
    });

    mostrarPerfil();
    modalPerfil.hide();
    mensagemPerfil.textContent = "Perfil atualizado com sucesso.";
  } catch (erro) {
    erroPerfil.textContent = erro.message;
    erroPerfil.classList.remove("d-none");
  } finally {
    salvarPerfil.disabled = false;
  }
});

// Inicialização
async function carregarPerfil() {
  try {
    perfil = await requisicaoPerfil();
    mostrarPerfil();
  } catch (erro) {
    nomePerfil.textContent = "Meu perfil";
    mensagemPerfil.textContent = erro.message;
  }
}

carregarPerfil();
carregarListasPerfil();