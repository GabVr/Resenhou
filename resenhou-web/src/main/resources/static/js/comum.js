/*
 * comum.js
 * Comportamentos compartilhados por TODAS as páginas do Resenhou.
 * Carregue este arquivo no final do <body>, depois do bundle do Bootstrap:
 *
 *   <script src="js/comum.js"></script>
 *
 * Cada função confere se o elemento existe antes de usá-lo, porque nem toda
 * página tem todos os elementos (o perfil, por exemplo, não tem o modal de
 * avaliação).
 */


/* 1. Menu ativo ---------------------------------------------------------
 * Antes, o render() do app.js marcava o item do menu com a classe "active".
 * Agora cada página é um arquivo, então comparamos o href de cada link com o
 * nome do arquivo atual. Assim você não precisa marcar o menu à mão.
 */
function marcarMenuAtivo() {
  const arquivoAtual = location.pathname.split("/").pop() || "index.html";

  document.querySelectorAll(".main-nav a, .quick-nav a").forEach((link) => {
    const destino = link.getAttribute("href");
    link.classList.toggle("active", destino === arquivoAtual);
  });
}


/* 2. Busca do cabeçalho -------------------------------------------------
 * Antes: mudava o "#" da URL. Agora: leva o texto para a página do catálogo
 * pela URL (catalogo.html?busca=texto).
 * A página do catálogo precisará ler esse parâmetro (veja a observação no
 * final do arquivo).
 */
function iniciarBuscaGlobal() {
  const formulario = document.querySelector("#globalSearch");
  const campo = document.querySelector("#globalSearchInput");
  if (!formulario || !campo) return;

  formulario.addEventListener("submit", (evento) => {
    evento.preventDefault();
    const texto = campo.value.trim();
    const parametro = texto ? "?busca=" + encodeURIComponent(texto) : "";
    location.href = "catalogo.html" + parametro;
  });
}


/* 3. Formulário de login ------------------------------------------------
 * Por enquanto é só demonstração visual. Quando houver integração com o
 * serviço de usuários, é aqui que a chamada à API vai entrar.
 */
function iniciarFormularioLogin() {
  const formulario = document.querySelector("#loginForm");
  if (!formulario) return;

  formulario.addEventListener("submit", (evento) => {
    evento.preventDefault();
    alert(
      "Esta é apenas uma demonstração visual. A autenticação será conectada depois.",
    );
  });
}


/* 4. Alternar entre "Entrar" e "Cadastrar" no modal de login ------------
 * Troca título, texto e botão, e adiciona (ou remove) o campo de nome de
 * usuário.
 */
function iniciarAlternarCadastro() {
  const botao = document.querySelector(".login-switch");
  const corpo = document.querySelector("#loginModal .modal-body");
  if (!botao || !corpo) return;

  botao.addEventListener("click", () => {
    const titulo = corpo.querySelector("h2");
    const texto = corpo.querySelector("p");
    const formulario = corpo.querySelector("form");
    const vaiCadastrar = botao.dataset.register !== "true";

    if (vaiCadastrar) {
      titulo.textContent = "Crie sua conta";
      texto.textContent = "Comece hoje o seu diário de leituras.";

      if (!corpo.querySelector('[name="usuario"]')) {
        const rotulo = document.createElement("label");
        rotulo.className = "form-label w-100 text-start";
        rotulo.innerHTML =
          'Nome de usuário<input class="form-control mt-2" name="usuario" placeholder="Como quer ser chamado?">';
        formulario.prepend(rotulo);
      }

      botao.textContent = "Já tem uma conta? Entrar";
      botao.dataset.register = "true";
    } else {
      titulo.textContent = "Entre na sua estante";
      texto.textContent = "Suas próximas histórias estão esperando.";

      corpo.querySelector('[name="usuario"]')?.closest("label")?.remove();

      botao.textContent = "Ainda não tem uma conta? Cadastre-se";
      botao.dataset.register = "false";
    }
  });
}


/* 5. Estrelas da avaliação ----------------------------------------------
 * Clicar na estrela N pinta da primeira até a N.
 */
function iniciarEstrelas() {
  const estrelas = document.querySelectorAll(".rating-stars button");
  if (estrelas.length === 0) return;

  estrelas.forEach((estrela, posicao) => {
    estrela.addEventListener("click", () => {
      estrelas.forEach((outra, outraPosicao) => {
        outra.classList.toggle("selected", outraPosicao <= posicao);
      });
    });
  });
}


/* Início ---------------------------------------------------------------- */
marcarMenuAtivo();
iniciarBuscaGlobal();
iniciarFormularioLogin();
iniciarAlternarCadastro();
iniciarEstrelas();


/*
 * OBSERVAÇÃO PARA A PÁGINA DO CATÁLOGO (catalogo.html):
 * Quando ela for criada, precisa ler o parâmetro "busca" da URL e preencher
 * o campo de busca, algo como:
 *
 *   const busca = new URLSearchParams(location.search).get("busca");
 *
 * (documentação: MDN, "URLSearchParams")
 */
