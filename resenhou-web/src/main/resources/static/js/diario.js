/*
 * diario.js
 * Comportamento da página diario.html: desenha as leituras em andamento e
 * faz o botão "Atualizar progresso" avançar a leitura.
 *
 * Ordem dos scripts no diario.html:
 *   dados.js  ->  comum.js  ->  diario.js
 */

const areaDiario = document.querySelector("#diarioLista");

// Leituras fictícias em andamento. "idLivro" aponta para um livro de dados.js.
const leituras = [
  { idLivro: 6, paginasLidas: 316, totalPaginas: 528 },
  { idLivro: 3, paginasLidas: 71,  totalPaginas: 210 },
];


/* Porcentagem lida, arredondada (316 de 528 páginas = 60%). */
function calcularPorcentagem(leitura) {
  return Math.round((leitura.paginasLidas / leitura.totalPaginas) * 100);
}


/* Cartão de uma leitura. "indice" liga o cartão ao item da lista "leituras". */
function cartaoLeitura(leitura, indice) {
  const livro = books.find((item) => item.id === leitura.idLivro);
  const porcentagem = calcularPorcentagem(leitura);

  return `<article class="diary-card" data-indice="${indice}">
    <div class="d-flex gap-4 w-100">
      ${cover(livro, "small")}
      <div class="diary-copy flex-grow-1">
        <span class="eyebrow">EM ANDAMENTO</span>
        <h2>${esc(livro.title)}</h2>
        <p>${esc(livro.author)}</p>

        <div class="progress-label">
          <span class="page-count">${leitura.paginasLidas} de ${leitura.totalPaginas} páginas</span>
          <strong class="progress-number">${porcentagem}%</strong>
        </div>
        <div class="progress"><div class="progress-bar" style="width:${porcentagem}%"></div></div>

        <div class="diary-meta">
          <span>Início: 12 de março de 2025</span>
          <label><input type="checkbox"> É uma releitura</label>
        </div>

        <button class="btn btn-outline-moss update-progress">Atualizar progresso</button>
        <button class="btn btn-ghost" data-bs-toggle="modal" data-bs-target="#ratingModal">Marcar como concluído</button>
      </div>
    </div>
  </article>`;
}


/*
 * Cada clique em "Atualizar progresso" avança 10% do total de páginas.
 * Um único "ouvinte" na lista cuida de todos os cartões.
 */
areaDiario.addEventListener("click", (evento) => {
  const botao = evento.target.closest(".update-progress");
  if (!botao) return;

  const cartao = botao.closest(".diary-card");
  const leitura = leituras[Number(cartao.dataset.indice)];

  const passo = Math.ceil(leitura.totalPaginas * 0.1);
  leitura.paginasLidas = Math.min(leitura.paginasLidas + passo, leitura.totalPaginas);

  const porcentagem = calcularPorcentagem(leitura);
  cartao.querySelector(".page-count").textContent =
    `${leitura.paginasLidas} de ${leitura.totalPaginas} páginas`;
  cartao.querySelector(".progress-number").textContent = `${porcentagem}%`;
  cartao.querySelector(".progress-bar").style.width = `${porcentagem}%`;

  if (porcentagem === 100) {
    botao.textContent = "Leitura concluída";
    botao.disabled = true;
  }
});


/* Início ---------------------------------------------------------------- */
areaDiario.innerHTML = leituras.map(cartaoLeitura).join("");
