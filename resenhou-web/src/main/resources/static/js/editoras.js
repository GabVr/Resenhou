// Editoras: os registros ainda ficam salvos no navegador.
const STORE = "resenhou-crud-v1-";
const type = "editoras";
const seed = {
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

const cfg = {
    label: "editora",
    columns: ["Nome", "País", "Site"],
    fields: [
      { key: "nome", label: "Nome", required: true },
      { key: "pais", label: "País", required: true },
      { key: "site", label: "Site", type: "text", required: true },
    ],
    cells: (r) => [r.nome, r.pais, r.site],
  };

const search = document.querySelector("#search"),
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
  const id = "field-" + f.key;
  return `<div class="col-md-6"><label class="form-label" for="${id}">${f.label}</label><input class="form-control" id="${id}" name="${f.key}" required value="${safe(r?.[f.key])}"></div>`;
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
document.querySelector("#editForm").addEventListener("submit", (e) => {
  e.preventDefault();
  const form = e.currentTarget;
  if (!form.reportValidity()) return;
  const data = {};
  for (const f of cfg.fields) {
    data[f.key] = form.elements[f.key].value.trim();
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
