/*
 * listas.js
 * Desenha as listas personalizadas na página listas.html.
 *
 * Ordem dos scripts no listas.html:
 *   dados.js  ->  comum.js  ->  listas.js
 */

const areaListas = document.querySelector("#listasGrid");

// Listas fictícias. Cada uma usa três capas, a partir da posição "indice".
const listas = [
  { nome: "Livros favoritos",                 quantidade: 8,  privada: false },
  { nome: "Para ler em dias de chuva",        quantidade: 12, privada: false },
  { nome: "Presentes para pessoas queridas",  quantidade: 6,  privada: true  },
];


/* Cartão de uma lista, com uma pilha de três capas. */
function cartaoLista(lista, indice) {
  const rotuloVisibilidade = lista.privada ? "🔒 PRIVADA" : "◎ PÚBLICA";

  return `<div class="col-md-6">
    <article class="list-card">
      <div class="cover-stack">
        ${cover(books[indice])}
        ${cover(books[indice + 1])}
        ${cover(books[indice + 2])}
      </div>
      <div class="list-copy">
        <div>
          <span>${rotuloVisibilidade}</span>
          <h2>${esc(lista.nome)}</h2>
          <p>Histórias que ficaram comigo muito depois da última página.</p>
        </div>
        <strong>${lista.quantidade} livros →</strong>
      </div>
    </article>
  </div>`;
}


/* Início ---------------------------------------------------------------- */
areaListas.innerHTML = listas.map(cartaoLista).join("");