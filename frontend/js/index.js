/*
 * index.js
 * Desenha os trechos da página inicial que dependem de dados: as capas, os
 * livros em destaque e as notícias.
 *
 * Ordem dos scripts no index.html:
 *   dados.js  ->  comum.js  ->  index.js
 */


/*
 * Qualquer elemento com data-capa="ID" recebe a capa do livro que tem esse id.
 * Opcionalmente, data-tamanho="small" escolhe o tamanho da capa.
 */
function desenharCapas() {
  document.querySelectorAll("[data-capa]").forEach((elemento) => {
    const idDoLivro = Number(elemento.dataset.capa);
    const livro = books.find((item) => item.id === idDoLivro);

    if (livro) {
      elemento.innerHTML = cover(livro, elemento.dataset.tamanho || "");
    }
  });
}


/* Os cinco primeiros livros da lista, em grade. */
function desenharDestaques() {
  const area = document.querySelector("#destaques");
  area.innerHTML = books.slice(0, 5).map(bookCard).join("");
}


/* As notícias, três por linha (o padrão do newsCard). */
function desenharNoticias() {
  const area = document.querySelector("#noticiasHome");
  area.innerHTML = news.map((noticia) => newsCard(noticia)).join("");
}


/* Início ---------------------------------------------------------------- */
desenharCapas();
desenharDestaques();
desenharNoticias();
