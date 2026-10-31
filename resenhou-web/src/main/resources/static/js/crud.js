const STORE = "resenhou-crud-v1-";
const seed = {
  autores: [
    {
      id: 1,
      nome: "Itamar Vieira Junior",
      dataNascimento: "1979-11-06",
      nacionalidade: "Brasileira",
      biografia: "Escritor brasileiro, autor de Torto Arado.",
    },
    {
      id: 2,
      nome: "Carla Madeira",
      dataNascimento: "1964-01-01",
      nacionalidade: "Brasileira",
      biografia: "Escritora brasileira.",
    },
    {
      id: 3,
      nome: "Han Kang",
      dataNascimento: "1970-11-27",
      nacionalidade: "Sul-coreana",
      biografia: "Escritora sul-coreana.",
    },
  ],

  editoras: [
    {
      id: 1,
      nome: "Todavia",
      pais: "Brasil",
      site: "https://todavialivros.com.br",
    },
    {
      id: 2,
      nome: "Record",
      pais: "Brasil",
      site: "https://www.record.com.br",
    },
  ],

  livros: [
    {
      id: 1,
      nome: "Torto Arado",
      titulo: "Torto Arado",
      sinopse: "A história de duas irmãs no sertão baiano.",
      dataPublicacao: "2019-01-01",
      numeroPaginas: 264,
      isbn: "9786580309313",
      tituloOriginal: "Torto Arado",
      autores: [1],
      editora: 1,
      generos: ["Literatura brasileira"],
    },
    {
      id: 2,
      nome: "Tudo é Rio",
      titulo: "Tudo é Rio",
      sinopse: "Um romance sobre amor, perda e recomeços.",
      dataPublicacao: "2021-01-01",
      numeroPaginas: 210,
      isbn: "9786555871784",
      tituloOriginal: "Tudo é Rio",
      autores: [2],
      editora: 2,
      generos: ["Romance"],
    },
    {
      id: 3,
      nome: "A Vegetariana",
      titulo: "A Vegetariana",
      sinopse: "Uma história sobre autonomia e transformação.",
      dataPublicacao: "2018-01-01",
      numeroPaginas: 176,
      isbn: "9788588808836",
      tituloOriginal: "채식주의자",
      autores: [3],
      editora: 1,
      generos: ["Ficção contemporânea"],
    },
  ],

  listas: [
    {
      id: 1,
      nome: "Meus favoritos",
      descricao: "Livros que marcaram minha vida.",
      dataCriacao: "2026-10-20",
      publica: true,
      usuarioId: 1,
      livroId: 1,
      visualicacaoPerfil: true,
    },
    {
      id: 2,
      nome: "Para ler depois",
      descricao: "Próximas leituras.",
      dataCriacao: "2026-10-21",
      publica: false,
      usuarioId: 1,
      livroId: 2,
      visualicacaoPerfil: false,
    },
  ],
};

function loadRecords(type) {
  try {
    const value = localStorage.getItem(STORE + type);
    return value === null ? structuredClone(seed[type]) : JSON.parse(value);
  } catch {
    return structuredClone(seed[type]);
  }
}

function saveRecords(type, data) {
  localStorage.setItem(STORE + type, JSON.stringify(data));
}

function safe(value) {
  return String(value ?? "").replace(
    /[&<>"']/g,
    (ch) =>
      ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[
        ch
      ],
  );
}

function getName(type, id) {
  return (
    loadRecords(type).find((r) => Number(r.id) === Number(id))?.nome ||
    "Não encontrado"
  );
}

const type = document.body.dataset.entity;
const definitions = {
  autores: {
    label: "autor",
    columns: ["Nome", "Nascimento", "Nacionalidade"],
    fields: [
      { key: "nome", label: "Nome", required: true },
      {
        key: "dataNascimento",
        label: "Data de nascimento",
        type: "date",
        required: true,
      },
      { key: "nacionalidade", label: "Nacionalidade", required: true },
      {
        key: "biografia",
        label: "Biografia",
        type: "textarea",
        required: true,
        wide: true,
      },
    ],
    cells: (r) => [r.nome, r.dataNascimento, r.nacionalidade],
  },

  editoras: {
    label: "editora",
    columns: ["Nome", "País", "Site"],
    fields: [
      { key: "nome", label: "Nome", required: true },
      { key: "pais", label: "País", required: true },
      { key: "site", label: "Site", type: "text", required: true },
    ],
    cells: (r) => [r.nome, r.pais, r.site],
  },

  livros: {
    label: "livro",
    columns: ["Título", "Autor(es)", "Editora", "Páginas"],
    fields: [
      { key: "nome", label: "Nome", required: true },
      { key: "titulo", label: "Título", required: true },
      { key: "tituloOriginal", label: "Título original", required: true },
      { key: "isbn", label: "ISBN", required: true },
      {
        key: "dataPublicacao",
        label: "Data de publicação",
        type: "date",
        required: true,
      },
      {
        key: "numeroPaginas",
        label: "Número de páginas",
        type: "number",
        min: 1,
        required: true,
      },
      {
        key: "autores",
        label: "Autores",
        type: "multi",
        options: "autores",
        required: true,
        wide: true,
      },
      {
        key: "editora",
        label: "Editora",
        type: "select",
        options: "editoras",
        required: true,
      },
      {
        key: "generos",
        label: "Gêneros (separados por vírgula)",
        type: "text",
        required: false,
        wide: true,
      },
      {
        key: "sinopse",
        label: "Sinopse",
        type: "textarea",
        required: true,
        wide: true,
      },
    ],
    cells: (r) => [
      r.titulo,
      (r.autores || []).map((id) => getName("autores", id)).join(", "),
      getName("editoras", r.editora),
      String(r.numeroPaginas),
    ],
  },

  listas: {
    label: "lista",
    columns: ["Nome", "Livro", "Usuário ID", "Visibilidade"],
    fields: [
      { key: "nome", label: "Nome", required: true },
      {
        key: "descricao",
        label: "Descrição",
        type: "textarea",
        wide: true,
        required: true,
      },
      {
        key: "dataCriacao",
        label: "Data de criação",
        type: "date",
        required: true,
      },
      {
        key: "usuarioId",
        label: "ID do usuário",
        type: "number",
        min: 1,
        required: true,
      },
      {
        key: "livroId",
        label: "Livro",
        type: "select",
        options: "livros",
        required: true,
      },
      { key: "publica", label: "Lista pública", type: "checkbox" },
      {
        key: "visualicacaoPerfil",
        label: "Mostrar no perfil",
        type: "checkbox",
      },
    ],
    cells: (r) => [
      r.nome,
      loadRecords("livros").find((l) => l.id === Number(r.livroId))?.titulo ||
        "Não encontrado",
      String(r.usuarioId),
      r.publica ? "Pública" : "Privada",
    ],
  },
};

const cfg = definitions[type],
  search = document.querySelector("#search"),
  head = document.querySelector("#tableHead"),
  body = document.querySelector("#tableBody"),
  count = document.querySelector("#count");
const modal = new bootstrap.Modal("#editModal"),
  deleteModal = new bootstrap.Modal("#deleteModal");
let editing = null,
  removing = null;
function render() {
  const q = search.value.trim().toLocaleLowerCase("pt-BR");
  const records = loadRecords(type).filter((r) =>
    JSON.stringify(r).toLocaleLowerCase("pt-BR").includes(q),
  );
  head.innerHTML =
    "<tr>" +
    cfg.columns.map((c) => `<th>${safe(c)}</th>`).join("") +
    '<th class="text-end">Ações</th></tr>';
  body.innerHTML = records
    .map(
      (r) =>
        `<tr>${cfg
          .cells(r)
          .map(
            (v, i) =>
              `<td class="${i === 0 ? "crud-primary" : ""}">${safe(v)}</td>`,
          )
          .join(
            "",
          )}<td><div class="crud-actions"><button class="crud-action" data-action="edit" data-id="${r.id}">Editar</button><button class="crud-action delete" data-action="delete" data-id="${r.id}">Excluir</button></div></td></tr>`,
    )
    .join("");
  count.textContent = `${records.length} registro(s)`;
  document
    .querySelector("#empty")
    .classList.toggle("d-none", records.length > 0);
}
function fieldHtml(f, r) {
  const value = r?.[f.key],
    id = "field-" + f.key,
    required = f.required ? "required" : "",
    wide = f.wide ? "col-12" : "col-md-6";
  if (f.type === "checkbox")
    return `<div class="${wide}"><div class="form-check mt-4"><input class="form-check-input" type="checkbox" name="${f.key}" id="${id}" ${value ? "checked" : ""}><label class="form-check-label" for="${id}">${f.label}</label></div></div>`;
  let control = "";
  if (f.type === "textarea")
    control = `<textarea class="form-control" id="${id}" name="${f.key}" rows="3" ${required}>${safe(value)}</textarea>`;
  else if (f.type === "select" || f.type === "multi") {
    const records = loadRecords(f.options),
      multiple = f.type === "multi" ? 'multiple size="4"' : "";
    const selected = Array.isArray(value) ? value : [value];
    control = `<select class="form-select" id="${id}" name="${f.key}" ${multiple} ${required}>${f.type === "select" ? '<option value="">Selecione...</option>' : ""}${records.map((item) => `<option value="${item.id}" ${selected.map(Number).includes(item.id) ? "selected" : ""}>${safe(item.titulo || item.nome)}</option>`).join("")}</select>`;
  } else
    control = `<input class="form-control" id="${id}" name="${f.key}" type="${f.type || "text"}" ${f.min ? 'min="' + f.min + '"' : ""} ${required} value="${safe(Array.isArray(value) ? value.join(", ") : value)}">`;
  return `<div class="${wide}"><label class="form-label" for="${id}">${f.label}</label>${control}${f.type === "multi" ? '<small class="text-muted">Use Ctrl (ou Cmd) para selecionar mais de um autor.</small>' : ""}</div>`;
}
function openEdit(id) {
  editing = id;
  const r = id === null ? null : loadRecords(type).find((x) => x.id === id);
  document.querySelector("#modalTitle").textContent =
    (r ? "Editar " : "Cadastrar ") + cfg.label;
  document.querySelector("#formFields").innerHTML = cfg.fields
    .map((f) => fieldHtml(f, r))
    .join("");
  document.querySelector("#formError").classList.add("d-none");
  modal.show();
}
document
  .querySelector("#newBtn")
  .addEventListener("click", () => openEdit(null));
search.addEventListener("input", render);
body.addEventListener("click", (e) => {
  const btn = e.target.closest("[data-action]");
  if (!btn) return;
  const id = Number(btn.dataset.id);
  if (btn.dataset.action === "edit") openEdit(id);
  else {
    removing = id;
    document.querySelector("#deleteDescription").textContent =
      "O registro será removido da demonstração atual.";
    deleteModal.show();
  }
});
document.querySelector("#confirmDelete").addEventListener("click", () => {
  if (removing !== null) {
    saveRecords(
      type,
      loadRecords(type).filter((r) => r.id !== removing),
    );
    removing = null;
    render();
    deleteModal.hide();
  }
});
document.querySelector("#editForm").addEventListener("submit", async(e) => {
  e.preventDefault();
  const form = e.currentTarget;
  if (!form.reportValidity()) return;
  const data = {};
  for (const f of cfg.fields) {
    const el = form.elements[f.key];
    if (f.type === "checkbox") data[f.key] = el.checked;
    else if (f.type === "multi")
      data[f.key] = Array.from(el.selectedOptions).map((o) => Number(o.value));
    else if (f.type === "number" || f.type === "select")
      data[f.key] = Number(el.value);
    else if (f.key === "generos")
      data[f.key] = el.value
        .split(",")
        .map((x) => x.trim())
        .filter(Boolean);
    else data[f.key] = el.value.trim();
  }
  const err = document.querySelector("#formError");
  err.classList.add("d-none");
  if (
    type === "livros" &&
    loadRecords(type).some((r) => r.isbn === data.isbn && r.id !== editing)
  ) {
    err.textContent = "Já existe um livro com esse ISBN.";
    err.classList.remove("d-none");
    return;
  }
  const records = loadRecords(type);
  if (editing === null) {
    data.id = Math.max(0, ...records.map((r) => r.id)) + 1;
    records.push(data);
  } else {
    const i = records.findIndex((r) => r.id === editing);
    if (i >= 0) records[i] = { ...records[i], ...data };
  }
  saveRecords(type, records);
  render();
  modal.hide();
});

render();
