/*
 * noticias.js
 * Desenha as notícias na página noticias.html, duas por linha.
 *
 * Ordem dos scripts no noticias.html:
 *   dados.js  ->  comum.js  ->  noticias.js
 */

const areaNoticias = document.querySelector("#noticiasGrid");

areaNoticias.innerHTML = news
  .map((noticia) => newsCard(noticia, "col-md-6"))
  .join("");