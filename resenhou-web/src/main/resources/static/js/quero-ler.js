/*
 * quero-ler.js
 * Comportamento da página quero-ler.html: desenha os livros da estante e
 * remove um livro quando o botão "Remover" é clicado.
 *
 * Ordem dos scripts no quero-ler.html:
 *   dados.js  ->  comum.js  ->  quero-ler.js
 */

const grade = document.querySelector("#wishlistGrid");
const contador = document.querySelector("#wishlistCount");

// Livros fictícios da estante (a partir do terceiro da lista).
const estante = books.slice(2);


/* Cartão de um livro da estante, com o botão de remover. */
function cartaoQueroLer(livro) {
  return `<div class="col wishlist-col">
    <article class="book-card wishlist-item" data-book-id="${livro.id}">
      <div class="cover-wrap">${cover(livro)}</div>
      <h3>${esc(livro.title)}</h3>
      <p>${esc(livro.author)}</p>
      <small>Adicionado em 18 mar. 2025</small>
      <button class="remove-wishlist">Remover</button>
    </article>
  </div>`;
}


/* Atualiza o texto "N livros na sua estante" contando os cartões na tela. */
function atualizarContador() {
  const total = document.querySelectorAll(".wishlist-col").length;
  contador.textContent = `${total} livros na sua estante`;
}


/* Um único "ouvinte" na grade cuida de todos os botões Remover. */
grade.addEventListener("click", (evento) => {
  const botao = evento.target.closest(".remove-wishlist");
  if (!botao) return;

  botao.closest(".wishlist-col").remove();
  atualizarContador();
});


/* Início ---------------------------------------------------------------- */
grade.innerHTML = estante.map(cartaoQueroLer).join("");
atualizarContador();
