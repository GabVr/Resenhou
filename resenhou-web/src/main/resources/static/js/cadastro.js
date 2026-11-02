const formCadastro = document.getElementById("formCadastro");
const mensagemCadastro = document.getElementById("mensagemCadastro");

const API_USUARIOS = "http://localhost:8083/api/usuarios";

function obterDataAtual() {
    const hoje = new Date();

    const ano = hoje.getFullYear();
    const mes = String(hoje.getMonth() + 1).padStart(2, "0");
    const dia = String(hoje.getDate()).padStart(2, "0");

    return `${ano}-${mes}-${dia}`;
}

function exibirMensagem(mensagem, tipo) {
    mensagemCadastro.textContent = mensagem;
    mensagemCadastro.className = `cadastro-senha-message ${tipo}`;
    mensagemCadastro.style.display = "block";
}

formCadastro.addEventListener("submit", async (event) => {
    event.preventDefault();

    const nome = document.getElementById("nome").value.trim();
    const email = document.getElementById("email").value.trim();
    const cpf = document.getElementById("cpf").value.trim();
    const username = document.getElementById("username").value.trim();
    const biografia = document.getElementById("biografia").value.trim();
    const senha = document.getElementById("senha").value;
    const confirmarSenha = document.getElementById("confirmarSenha").value;

    if (senha !== confirmarSenha) {
        exibirMensagem("As senhas informadas não coincidem.", "error");
        return;
    }

    if (senha.length < 8) {
        exibirMensagem("A senha deve possuir pelo menos 8 caracteres.", "error");
        return;
    }

    const usuario = {
        nome: nome,
        username: username,
        email: email,
        senha: senha,
        dataCadastro: obterDataAtual(),
        biografia: biografia || null,
        cpf: cpf
    };

    const botao = formCadastro.querySelector("button[type='submit']");
    const textoOriginal = botao.textContent;

    botao.disabled = true;
    botao.textContent = "Cadastrando...";

    try {
        const resposta = await fetch(API_USUARIOS, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(usuario)
        });

        const dados = await resposta.json().catch(() => ({}));

        if (!resposta.ok) {
            throw new Error(
                dados.mensagem || "Não foi possível realizar o cadastro."
            );
        }

        exibirMensagem(
            dados.mensagem || "Conta criada com sucesso! Você já pode entrar.",
            "success"
        );

        formCadastro.reset();

    } catch (erro) {
        console.error("Erro no cadastro:", erro);

        exibirMensagem(
            erro.message || "Não foi possível conectar ao servidor. Tente novamente.",
            "error"
        );

    } finally {
        botao.disabled = false;
        botao.textContent = textoOriginal;
    }
});