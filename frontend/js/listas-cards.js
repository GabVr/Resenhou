/* CRUD de listas em cards, com vários livros por lista e sem login.
   Usa livros de resenhou-crud-v1-livros (mesma chave do CRUD de livros).
   Cada lista guarda itens: [{livroId, ordemExibicao}]. */
const LIST_KEY = "resenhou-crud-v1-listas";
const BOOK_KEY = "resenhou-crud-v1-livros";
const DEMO_BOOKS = [
  { id: 1, titulo: "Torto Arado" },
  { id: 2, titulo: "Tudo é Rio" },
  { id: 3, titulo: "A Vegetariana" },
  { id: 4, titulo: "Cem Anos de Solidão" },
  { id: 5, titulo: "O Avesso da Pele" },
  { id: 6, titulo: "Pachinko" },
];
const DEMO_LISTS = [
  {
    id: 1,
    nome: "Livros favoritos",
    descricao: "Histórias que ficaram comigo muito depois da última página.",
    dataCriacao: "2026-10-20",
    publica: true,
    usuarioId: 1,
    visualicacaoPerfil: true,
    itens: [
      { livroId: 1, ordemExibicao: 1 },
      { livroId: 2, ordemExibicao: 2 },
      { livroId: 3, ordemExibicao: 3 },
    ],
  },
  {
    id: 2,
    nome: "Para ler em dias de chuva",
    descricao: "Livros para acompanhar uma tarde tranquila.",
    dataCriacao: "2026-10-21",
    publica: true,
    usuarioId: 1,
    visualicacaoPerfil: true,
    itens: [
      { livroId: 4, ordemExibicao: 1 },
      { livroId: 5, ordemExibicao: 2 },
    ],
  },
  {
    id: 3,
    nome: "Presentes para pessoas queridas",
    descricao: "Ideias de livros para presentear.",
    dataCriacao: "2026-10-22",
    publica: false,
    usuarioId: 1,
    visualicacaoPerfil: false,
    itens: [{ livroId: 6, ordemExibicao: 1 }],
  },
];
const escapeHtml = (v) =>
  String(v ?? "").replace(
    /[&<>"']/g,
    (c) =>
      ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[
        c
      ],
  );
function readArray(key, fallback) {
  try {
    const raw = localStorage.getItem(key);
    const parsed = raw === null ? structuredClone(fallback) : JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : structuredClone(fallback);
  } catch {
    return structuredClone(fallback);
  }
}
function allBooks() {
  return readArray(BOOK_KEY, DEMO_BOOKS);
}
function normalizeList(list) {
  // Compatibilidade com a versão anterior que tinha apenas livroId.
  const items = Array.isArray(list.itens)
    ? list.itens
    : list.livroId
      ? [{ livroId: Number(list.livroId), ordemExibicao: 1 }]
      : [];
  const seen = new Set();
  return {
    ...list,
    itens: items
      .map((item, i) => ({
        livroId: Number(typeof item === "number" ? item : item.livroId),
        ordemExibicao: i + 1,
      }))
      .filter(
        (item) =>
          Number.isInteger(item.livroId) &&
          item.livroId > 0 &&
          !seen.has(item.livroId) &&
          seen.add(item.livroId),
      ),
  };
}
function allLists() {
  return readArray(LIST_KEY, DEMO_LISTS).map(normalizeList);
}
function saveLists(data) {
  localStorage.setItem(LIST_KEY, JSON.stringify(data));
}
const cards = document.querySelector("#listCards");
const search = document.querySelector("#search");
const editForm = document.querySelector("#editForm");
const editModal = new bootstrap.Modal(document.querySelector("#editModal"));
const deleteModal = new bootstrap.Modal(document.querySelector("#deleteModal"));
const palettes = [
  "terra",
  "olive",
  "blue",
  "gold",
  "wine",
  "navy",
  "coral",
  "cream",
];
let editingId = null,
  deletingId = null;
function bookTitle(id) {
  const b = allBooks().find((x) => Number(x.id) === Number(id));
  return b?.titulo || b?.nome || `Livro #${id}`;
}
function bookCover(id, index) {
  return `<div class="cover cover-${palettes[index % palettes.length]}"><span class="cover-kicker">RESENHOU</span><strong>${escapeHtml(bookTitle(id))}</strong><small>MINHA ESTANTE</small></div>`;
}
function render() {
  const q = search.value.trim().toLocaleLowerCase("pt-BR");
  const results = allLists().filter((l) =>
    [l.nome, l.descricao, ...l.itens.map((x) => bookTitle(x.livroId))]
      .join(" ")
      .toLocaleLowerCase("pt-BR")
      .includes(q),
  );
  cards.innerHTML = results
    .map((list, index) => {
      const ids = list.itens.map((x) => x.livroId);
      const stack = ids.length
        ? ids
            .slice(0, 3)
            .map((id, i) => bookCover(id, index + i))
            .join("")
        : '<div class="empty-cover">Sua próxima leitura começa aqui</div>';
      const titles = ids
        .map(
          (id) => `<span class="book-chip">${escapeHtml(bookTitle(id))}</span>`,
        )
        .join("");
      return `<div class="col-md-6 col-xl-4"><article class="list-card h-100">
      <div class="cover-stack">${stack}</div>
      <div class="list-copy"><div class="list-details"><span>${list.publica ? "◎ PÚBLICA" : "🔒 PRIVADA"}</span><h2>${escapeHtml(list.nome)}</h2><p>${escapeHtml(list.descricao || "Sem descrição")}</p>
      <div class="list-card-meta">${ids.length} ${ids.length === 1 ? "livro" : "livros"} · ${list.visualicacaoPerfil ? "Visível no perfil" : "Oculta no perfil"}</div>
      <div class="list-book-chips">${titles || '<span class="text-muted">Nenhum livro adicionado</span>'}</div></div></div>
      <div class="list-card-actions"><button class="btn btn-outline-moss" data-action="edit" data-id="${Number(list.id)}">Editar lista / livros</button><button class="btn btn-outline-danger" data-action="delete" data-id="${Number(list.id)}">Excluir</button></div>
    </article></div>`;
    })
    .join("");
  document.querySelector("#count").textContent = `${results.length} lista(s)`;
  document
    .querySelector("#empty")
    .classList.toggle("d-none", results.length !== 0);
}
function drawBookOptions() {
  const query = document
    .querySelector("#bookSearch")
    .value.trim()
    .toLocaleLowerCase("pt-BR");
  const picker = document.querySelector("#bookOptions");
  const checked = new Set(
    [...picker.querySelectorAll("input:checked")].map((el) => Number(el.value)),
  );
  // Preserva as seleções mesmo quando um livro está oculto pelo filtro.
  selectedBooks = new Set([...selectedBooks, ...checked]);
  const available = allBooks().filter((b) =>
    String(b.titulo || b.nome || "")
      .toLocaleLowerCase("pt-BR")
      .includes(query),
  );
  picker.innerHTML =
    available
      .map(
        (b) =>
          `<label class="book-option"><input class="form-check-input" type="checkbox" value="${Number(b.id)}" ${selectedBooks.has(Number(b.id)) ? "checked" : ""}><span>${escapeHtml(b.titulo || b.nome)}</span></label>`,
      )
      .join("") || '<p class="text-muted m-2">Nenhum livro encontrado.</p>';
}
let selectedBooks = new Set();
function openForm(id) {
  editingId = id;
  const record =
    id === null ? null : allLists().find((x) => Number(x.id) === id);
  selectedBooks = new Set(record?.itens.map((x) => x.livroId) || []);
  editForm.reset();
  editForm.elements.nome.value = record?.nome || "";
  editForm.elements.descricao.value = record?.descricao || "";
  editForm.elements.dataCriacao.value =
    record?.dataCriacao || new Date().toISOString().slice(0, 10);
  editForm.elements.usuarioId.value = record?.usuarioId || 1;
  editForm.elements.publica.checked = Boolean(record?.publica);
  editForm.elements.visualicacaoPerfil.checked = Boolean(
    record?.visualicacaoPerfil,
  );
  document.querySelector("#bookSearch").value = "";
  document.querySelector("#modalTitle").textContent = record
    ? "Editar lista"
    : "Criar nova lista";
  document.querySelector("#formError").classList.add("d-none");
  drawBookOptions();
  editModal.show();
}
document
  .querySelector("#newBtn")
  .addEventListener("click", () => openForm(null));
search.addEventListener("input", render);
document
  .querySelector("#bookSearch")
  .addEventListener("input", drawBookOptions);
document.querySelector("#bookOptions").addEventListener("change", (e) => {
  if (!e.target.matches('input[type="checkbox"]')) return;
  const id = Number(e.target.value);
  if (e.target.checked) selectedBooks.add(id);
  else selectedBooks.delete(id);
});
cards.addEventListener("click", (e) => {
  const button = e.target.closest("button[data-action]");
  if (!button) return;
  const id = Number(button.dataset.id);
  if (button.dataset.action === "edit") openForm(id);
  else {
    deletingId = id;
    const record = allLists().find((x) => Number(x.id) === id);
    document.querySelector("#deleteDescription").textContent =
      `Deseja excluir “${record?.nome || ""}” e remover todos os livros dessa lista? Os livros continuarão no catálogo.`;
    deleteModal.show();
  }
});
document.querySelector("#confirmDelete").addEventListener("click", () => {
  if (deletingId === null) return;
  saveLists(allLists().filter((x) => Number(x.id) !== deletingId));
  deletingId = null;
  deleteModal.hide();
  render();
});
editForm.addEventListener("submit", (e) => {
  e.preventDefault();
  if (!editForm.reportValidity()) return;
  const f = editForm.elements;
  const data = {
    nome: f.nome.value.trim(),
    descricao: f.descricao.value.trim(),
    dataCriacao: f.dataCriacao.value,
    usuarioId: Number(f.usuarioId.value),
    publica: f.publica.checked,
    visualicacaoPerfil: f.visualicacaoPerfil.checked,
    itens: [...selectedBooks].map((livroId, i) => ({
      livroId,
      ordemExibicao: i + 1,
    })),
  };
  if (!data.nome || !data.descricao) return;
  const records = allLists();
  if (editingId === null) {
    data.id = Math.max(0, ...records.map((x) => Number(x.id) || 0)) + 1;
    records.push(data);
  } else {
    const index = records.findIndex((x) => Number(x.id) === editingId);
    if (index < 0) return;
    records[index] = { ...records[index], ...data };
    delete records[index].livroId;
  }
  saveLists(records);
  editModal.hide();
  render();
});
render();
