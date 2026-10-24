/*
 * catalogo.js
 * Comportamento da página catalogo.html: lista de livros, filtro por texto e
 * gênero, e leitura do parâmetro ?busca= enviado pela busca do cabeçalho.
 *
 * Ordem dos scripts no catalogo.html:
 *   dados.js  ->  comum.js  ->  catalogo.js
 */

const formularioFiltros = document.querySelector("#catalogFilters");
const campoBusca = document.querySelector("#catalogSearch");
const campoGenero = document.querySelector("#catalogGenre");
const areaResultados = document.querySelector("#catalogResults");
const contador = document.querySelector("#resultCount");


/* Cria uma opção no <select> para cada gênero que existe nos livros. */
function preencherGeneros() {
  const generos = [...new Set(books.map((livro) => livro.genre))];

  generos.forEach((genero) => {
    const opcao = document.createElement("option");
    opcao.textContent = genero; // textContent já é seguro contra XSS
    campoGenero.appendChild(opcao);
  });
}


/* Desenha a lista de livros recebida e atualiza o contador. */
function mostrarLivros(lista) {
  if (lista.length > 0) {
    areaResultados.innerHTML = lista.map(bookCard).join("");
  } else {
    areaResultados.innerHTML = '<p class="text-muted">Nenhum livro encontrado.</p>';
  }

  contador.textContent = `${lista.length} livros encontrados`;
}


/* Lê o que foi digitado, aplica os filtros e mostra o resultado. */
function filtrar() {
  const texto = campoBusca.value.toLowerCase();
  const genero = campoGenero.value;

  const encontrados = books.filter((livro) => {
    const combinaComTexto = (livro.title + " " + livro.author).toLowerCase().includes(texto);
    const combinaComGenero = !genero || livro.genre === genero;
    return combinaComTexto && combinaComGenero;
  });

  mostrarLivros(encontrados);
}


formularioFiltros.addEventListener("submit", (evento) => {
  evento.preventDefault();
  filtrar();
});


/* Início ---------------------------------------------------------------- */
preencherGeneros();

// Se a pessoa chegou pela busca do cabeçalho (catalogo.html?busca=texto),
// preenche o campo e já filtra.
const buscaDaUrl = new URLSearchParams(location.search).get("busca");
if (buscaDaUrl) {
  campoBusca.value = buscaDaUrl;
}

filtrar();
