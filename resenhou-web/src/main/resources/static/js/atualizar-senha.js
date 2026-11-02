const formAtualizarSenha = document.getElementById("formAtualizarSenha");
const mensagemAtualizacao = document.getElementById("mensagemAtualizacao");

const API_USUARIOS = "http://localhost:8083/api/usuarios/atualizar-senha";

function exibirMensagem(mensagem, tipo) {
    mensagemAtualizacao.textContent = mensagem;
    mensagemAtualizacao.className = `atualizar-senha-alert atualizar-senha-${tipo}`;
    mensagemAtualizacao.style.display = "block";
}

formAtualizarSenha.addEventListener("submit", async (event) => {
    event.preventDefault();

    const email = document.getElementById("email").value.trim();
    const senhaAtual = document.getElementById("senhaAtual").value;
    const novaSenha = document.getElementById("novaSenha").value;
    const confirmarNovaSenha = document.getElementById("confirmarNovaSenha").value;

    if (!email) {
        exibirMensagem("Informe seu e-mail cadastrado.", "error");
        return;
    }

    if (novaSenha !== confirmarNovaSenha) {
        exibirMensagem("As novas senhas não coincidem.", "error");
        return;
    }

    if (novaSenha.length < 8) {
        exibirMensagem("A nova senha deve possuir pelo menos 8 caracteres.", "error");
        return;
    }

    if (senhaAtual === novaSenha) {
        exibirMensagem("A nova senha deve ser diferente da senha atual.", "error");
        return;
    }

    const dados = {
        email,
        senhaAtual,
        novaSenha
    };

    const botao = formAtualizarSenha.querySelector("button[type='submit']");
    const textoOriginal = botao.textContent;

    botao.disabled = true;
    botao.textContent = "Atualizando...";

    try {
        const resposta = await fetch(API_USUARIOS, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dados)
        });

        const corpoResposta = await resposta.text();

        console.log("Status HTTP:", resposta.status);
        console.log("Resposta do backend:", corpoResposta);

        let resultado = {};

        try {
            resultado = JSON.parse(corpoResposta);
        } catch (erro) {
            console.error("A resposta não está em formato JSON.");
        }

        if (!resposta.ok) {
            throw new Error(
                resultado.mensagem ||
                resultado.detail ||
                resultado.message ||
                `Erro HTTP ${resposta.status}`
            );
        }
        exibirMensagem(
            resultado.mensagem || "Senha atualizada com sucesso! Confira seu e-mail.",
            "success"
        );

        formAtualizarSenha.reset();

    } catch (erro) {
        console.error("Erro ao atualizar senha:", erro);

        exibirMensagem(
            erro.message || "Não foi possível conectar ao servidor.",
            "error"
        );

    } finally {
        botao.disabled = false;
        botao.textContent = textoOriginal;
    }
});
