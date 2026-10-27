/*
 * dados.js
 * Dados fictícios e funções auxiliares usadas para desenhar os cartões.
 *
 * Carregue ANTES do script da página:
 *   <script src="js/dados.js"></script>
 *
 * Quando o front for ligado aos serviços, as listas "books" e "news" serão
 * substituídas por chamadas à API (fetch). As funções de desenho (cover,
 * bookCard, newsCard) continuam servindo.
 */


/* Dados fictícios -------------------------------------------------------- */

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


/* Funções auxiliares ------------------------------------------------------ */

/*
 * esc: protege contra XSS.
 * Use SEMPRE que for colocar na página um texto que não foi escrito por você
 * (por exemplo, vindo do banco ou da API). Ela troca os caracteres especiais
 * do HTML por versões inofensivas.
 */
function esc(valor) {
  const trocas = { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" };
  return String(valor).replace(/[&<>"']/g, (caractere) => trocas[caractere]);
}

/* Capa colorida de um livro. "tamanho" pode ser "small" ou "large". */
function cover(livro, tamanho = "") {
  const classeTamanho = tamanho ? "cover-" + tamanho : "";
  return `<div class="cover ${classeTamanho} cover-${livro.cover}">
    <span class="cover-kicker">${esc(livro.accent || "")}</span>
    <strong>${esc(livro.title)}</strong>
    <small>${esc(livro.author)}</small>
  </div>`;
}

/* Cartão de livro usado nas grades (catálogo, destaques). */
function bookCard(livro) {
  return `<div class="col">
    <article class="book-card" data-book-id="${livro.id}" tabindex="0">
      <div class="cover-wrap">${cover(livro)}<span class="score">★ ${livro.score.toFixed(1)}</span></div>
      <h3>${esc(livro.title)}</h3>
      <p>${esc(livro.author)}</p>
    </article>
  </div>`;
}

/*
 * Cartão de notícia.
 * "classeColuna" define a largura: "col-md-4" (3 por linha, padrão) ou
 * "col-md-6" (2 por linha).
 * CUIDADO: não use "news.map(newsCard)" direto, porque o map entrega o índice
 * como segundo argumento. Use: news.map((n) => newsCard(n)).
 */
function newsCard(noticia, classeColuna = "col-md-4") {
  return `<div class="${classeColuna}">
    <article class="news-card">
      <img src="${esc(noticia.image)}" alt="Livros e leitura">
      <div>
        <span>${esc(noticia.tag)}</span>
        <h3>${esc(noticia.title)}</h3>
        <p>${esc(noticia.summary)}</p>
        <small>${esc(noticia.source)} · 5 min de leitura</small>
      </div>
    </article>
  </div>`;
}
