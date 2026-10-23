const books = [
  {id:1,title:"Torto Arado",author:"Itamar Vieira Junior",genre:"Literatura brasileira",year:2019,publisher:"Todavia",pages:264,cover:"terra",accent:"O chão guarda histórias",score:4.7},
  {id:2,title:"A Vegetariana",author:"Han Kang",genre:"Ficção contemporânea",year:2018,publisher:"Todavia",pages:176,cover:"olive",accent:"Han Kang",score:4.2},
  {id:3,title:"Tudo é Rio",author:"Carla Madeira",genre:"Romance",year:2021,publisher:"Record",pages:210,cover:"blue",accent:"Um romance",score:4.5},
  {id:4,title:"Cem Anos de Solidão",author:"Gabriel García Márquez",genre:"Realismo mágico",year:1967,publisher:"Record",pages:448,cover:"gold",accent:"Gabriel García Márquez",score:4.8},
  {id:5,title:"O Avesso da Pele",author:"Jeferson Tenório",genre:"Literatura brasileira",year:2020,publisher:"Companhia das Letras",pages:192,cover:"wine",accent:"Jeferson Tenório",score:4.6},
  {id:6,title:"Pachinko",author:"Min Jin Lee",genre:"Ficção histórica",year:2020,publisher:"Intrínseca",pages:528,cover:"navy",accent:"Uma saga familiar",score:4.4},
  {id:7,title:"A Ridícula Ideia de Nunca Mais te Ver",author:"Rosa Montero",genre:"Biografia",year:2019,publisher:"Todavia",pages:200,cover:"coral",accent:"Rosa Montero",score:4.3},
  {id:8,title:"O Peso do Pássaro Morto",author:"Aline Bei",genre:"Poesia",year:2017,publisher:"Nós",pages:168,cover:"cream",accent:"Aline Bei",score:4.1}
];
const news = [
 {title:"Os lançamentos literários mais aguardados deste outono",source:"Quatro Cinco Um",tag:"LANÇAMENTOS",image:"https://images.unsplash.com/photo-1697029749544-ffa7f15f9dd0?auto=format&fit=crop&w=900&q=82",summary:"Novas vozes, traduções inéditas e reencontros que já chegaram às livrarias."},
 {title:"Clubes de leitura transformam a experiência de ler em comunidade",source:"Revista Bula",tag:"COMUNIDADE",image:"https://images.unsplash.com/photo-1630343710506-89f8b9f21d31?auto=format&fit=crop&w=900&q=82",summary:"Encontros aproximam leitores de diferentes gerações."},
 {title:"Por que estamos voltando aos grandes clássicos?",source:"Nexo",tag:"ENSAIO",image:"https://images.unsplash.com/photo-1639527924446-3edc522ae4b0?auto=format&fit=crop&w=900&q=82",summary:"Novas traduções e projetos gráficos ajudam a formar leitores."}
];
const app = document.querySelector("#app");
const esc = (value) =>
  String(value).replace(
    /[&<>"']/g,
    (c) =>
      ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[
        c
      ],
  );
const cover = (b, size = "") =>
  `<div class="cover ${size ? "cover-" + size : ""} cover-${b.cover}"><span class="cover-kicker">${esc(b.accent || "")}</span><strong>${esc(b.title)}</strong><small>${esc(b.author)}</small></div>`;
const bookCard = (b) =>
  `<div class="col"><article class="book-card" data-book-id="${b.id}" tabindex="0"><div class="cover-wrap">${cover(b)}<span class="score">★ ${b.score.toFixed(1)}</span></div><h3>${esc(b.title)}</h3><p>${esc(b.author)}</p></article></div>`;
const sectionTitle = (eyebrow, title, link, label) =>
  `<div class="section-title"><div><span class="eyebrow">${eyebrow}</span><h2>${title}</h2></div><a href="#${link}">${label} →</a></div>`;
const bookGrid = (list) =>
  `<div class="row row-cols-2 row-cols-md-3 row-cols-lg-5 g-4">${list.map(bookCard).join("")}</div>`;

function home() {
  return `
<section class="hero">
    <div class="container-fluid hero-inner">
        <div class="hero-copy">
            <span class="eyebrow">SUA VIDA ENTRE LIVROS</span>
            <h1>Histórias que ficam.<br><em>Leituras que transformam.</em></h1>
            <p>Descubra novos livros, acompanhe suas leituras e compartilhe o que cada história despertou em você.</p>
            <div class="d-flex flex-wrap gap-2 mt-4"><a class="btn btn-moss" href="#catalogo">Explorar catálogo</a>
            <a class="btn btn-outline-moss" href="#diario">Meu diário de leitura</a>
        </div>
    </div>
    <div class="hero-art d-none d-md-block">
        <div class="hero-sun"></div>
            <div class="hero-cover one">${cover(books[1])}</div>
            <div class="hero-cover two">${cover(books[0])}</div>
            <div class="hero-cover three">${cover(books[3])}</div>
            <div class="quote-card"><span>“</span>
                <p>Um leitor vive mil vidas antes de morrer.</p>
                <small>George R. R. Martin</small>
            </div>
        </div>
    </div>
</section>
<section class="content-section container">${sectionTitle("PARA DESCOBRIR", "Livros em destaque", "catalogo", "Ver catálogo")}${bookGrid(books.slice(0, 5))}</section>
<section class="reading-section"><div class="container">${sectionTitle("NA SUA ESTANTE", "Lendo agora", "diario", "Abrir diário")}<div class="reading-card row align-items-center g-4"><div class="col-auto">${cover(books[5], "small")}</div><div class="col reading-info"><span>EM ANDAMENTO</span><h3>Pachinko</h3><p>Min Jin Lee</p><div class="progress-label"><span>Página 316 de 528</span><strong>60%</strong></div><div class="progress"><div class="progress-bar" style="width:60%"></div></div><small>Você começou em 12 de março</small></div><div class="col-12 col-lg-auto"><a class="btn btn-outline-moss" href="#diario">Atualizar progresso</a></div></div></div></section>
<section class="content-section container">${sectionTitle("PARA FICAR POR DENTRO", "Últimas notícias literárias", "noticias", "Ver todas")}<div class="row g-4">${news.map(newsCard).join("")}</div></section>`;
}
function newsCard(n) {
  return `<div class="col-md-4"><article class="news-card"><img src="${n.image}" alt="Livros e leitura"><div><span>${n.tag}</span><h3>${n.title}</h3><p>${n.summary}</p><small>${n.source} · 5 min de leitura</small></div></article></div>`;
}
function diary() {
  return `<section class="page container"><div class="page-heading heading-row"><div><span class="eyebrow">MINHA JORNADA</span><h1>Diário de leitura</h1><p>Acompanhe cada página e guarde as memórias das suas leituras.</p></div><button class="btn btn-moss" data-bs-toggle="modal" data-bs-target="#loginModal">＋ Adicionar leitura</button></div><div class="stat-row row g-0"><div class="col-6 col-md-3"><strong>2</strong><span>Lendo agora</span></div><div class="col-6 col-md-3"><strong>14</strong><span>Livros concluídos</span></div><div class="col-6 col-md-3"><strong>4.120</strong><span>Páginas este ano</span></div><div class="col-6 col-md-3"><strong>8</strong><span>Dias em sequência</span></div></div><div class="tabs"><button class="active">Lendo agora <span>2</span></button><button>Concluídos <span>14</span></button><button>Abandonados <span>1</span></button></div><div class="diary-list">${diaryCard(books[5], 316, 528, 60)}${diaryCard(books[2], 71, 210, 34)}</div></section>`;
}
function diaryCard(b, pages, total, pct) {
  return `<article class="diary-card"><div class="d-flex gap-4 w-100">${cover(b, "small")}<div class="diary-copy flex-grow-1"><span class="eyebrow">EM ANDAMENTO</span><h2>${b.title}</h2><p>${b.author}</p><div class="progress-label"><span class="page-count">${pages} de ${total} páginas</span><strong class="progress-number">${pct}%</strong></div><div class="progress"><div class="progress-bar" style="width:${pct}%"></div></div><div class="diary-meta"><span>Início: 12 de março de 2025</span><label><input type="checkbox"> É uma releitura</label></div><button class="btn btn-outline-moss update-progress">Atualizar progresso</button> <button class="btn btn-ghost" data-bs-toggle="modal" data-bs-target="#ratingModal">Marcar como concluído</button></div></div></article>`;
}
function lists() {
  const names = [
    "Livros favoritos",
    "Para ler em dias de chuva",
    "Presentes para pessoas queridas",
  ];

}
function profile() {
  }
function render() {
  let route = location.hash.replace("#", "") || "inicio";
  const pages = {
    inicio: home,
    catalogo: catalog,
    diario: diary,
    listas: lists,
    "quero-ler": wishlist,
    noticias: () =>
      `<section class="page container"><div class="page-heading"><span class="eyebrow">PARA FICAR POR DENTRO</span><h1>Notícias literárias</h1><p>Conversas, lançamentos e ideias que movimentam o mundo dos livros.</p></div><div class="row g-4 mt-2">${news.map((n) => `<div class="col-md-6">${newsCard(n)}</div>`).join("")}</div></section>`,
    perfil: profile,
  };
  app.innerHTML = (pages[route] || home)();
  document
    .querySelectorAll(".main-nav a,.quick-nav a")
    .forEach((a) =>
      a.classList.toggle("active", a.getAttribute("href") === "#" + route),
    );
  wirePage();
  window.scrollTo(0, 0);
}
function wirePage() {
  const filter = document.querySelector("#catalogFilters");
  if (filter)
    filter.addEventListener("submit", (e) => {
      e.preventDefault();
      const q = document.querySelector("#catalogSearch").value.toLowerCase();
      const g = document.querySelector("#catalogGenre").value;
      const found = books.filter(
        (b) =>
          (b.title + " " + b.author).toLowerCase().includes(q) &&
          (!g || b.genre === g),
      );
      document.querySelector("#catalogResults").innerHTML =
        found.map(bookCard).join("") ||
        '<p class="text-muted">Nenhum livro encontrado.</p>';
      document.querySelector("#resultCount").textContent =
        `${found.length} livros encontrados`;
    });
  document.querySelectorAll(".update-progress").forEach((btn) =>
    btn.addEventListener("click", () => {
      const card = btn.closest(".diary-copy"),
        label = card.querySelector(".progress-number"),
        bar = card.querySelector(".progress-bar"),
        count = card.querySelector(".page-count");
      const next = Math.min((parseInt(label.textContent) || 0) + 10, 100);
      label.textContent = next + "%";
      bar.style.width = next + "%";
      count.textContent =
        next === 100 ? "Leitura concluída" : next + "% concluído";
      if (next === 100) btn.textContent = "Leitura concluída";
    }),
  );
  document.querySelectorAll(".remove-wishlist").forEach((btn) =>
    btn.addEventListener("click", () => {
      btn.closest(".wishlist-col").remove();
      const count = document.querySelectorAll(".wishlist-col").length;
      document.querySelector("#wishlistCount").textContent =
        `${count} livros na sua estante`;
    }),
  );
  document
    .querySelectorAll(".rating-stars button")
    .forEach((btn, i, all) =>
      btn.addEventListener("click", () =>
        all.forEach((s, j) => s.classList.toggle("selected", j <= i)),
      ),
    );
}
document.querySelector("#globalSearch").addEventListener("submit", (e) => {
  e.preventDefault();
  location.hash = "catalogo";
  setTimeout(() => {
    const input = document.querySelector("#catalogSearch");
    if (input) {
      input.value = document.querySelector("#globalSearchInput").value;
      document
        .querySelector("#catalogFilters")
        .dispatchEvent(
          new Event("submit", { cancelable: true, bubbles: true }),
        );
    }
  }, 0);
});
document.querySelector("#loginForm").addEventListener("submit", (e) => {
  e.preventDefault();
  alert(
    "Esta é apenas uma demonstração visual. A autenticação será conectada depois.",
  );
});
document.querySelector(".login-switch").addEventListener("click", () => {
  const btn = document.querySelector(".login-switch"),
    body = document.querySelector("#loginModal .modal-body"),
    title = body.querySelector("h2"),
    p = body.querySelector("p");
  const register = btn.dataset.register !== "true";
  if (register) {
    title.textContent = "Crie sua conta";
    p.textContent = "Comece hoje o seu diário de leituras.";
    if (!body.querySelector('[name="usuario"]')) {
      const label = document.createElement("label");
      label.className = "form-label w-100 text-start";
      label.innerHTML =
        'Nome de usuário<input class="form-control mt-2" name="usuario" placeholder="Como quer ser chamado?">';
      body.querySelector("form").prepend(label);
    }
    btn.textContent = "Já tem uma conta? Entrar";
    btn.dataset.register = "true";
  } else {
    title.textContent = "Entre na sua estante";
    p.textContent = "Suas próximas histórias estão esperando.";
    body.querySelector('[name="usuario"]')?.closest("label")?.remove();
    btn.textContent = "Ainda não tem uma conta? Cadastre-se";
    btn.dataset.register = "false";
  }
});
window.addEventListener("hashchange", render);
render();