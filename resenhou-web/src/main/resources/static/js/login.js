const formulario = document.querySelector("#loginForm");

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    // O web envia as credenciais em JSON à API e mantém o JWT na sessão.
    const resposta = await fetch(formulario.action, {
        method: "POST",
        credentials: "same-origin",
        body: new URLSearchParams(new FormData(formulario))
    });

    if (resposta.ok) {
        const dados = await resposta.json();
        window.location.assign(dados.redirect);
    }
});
