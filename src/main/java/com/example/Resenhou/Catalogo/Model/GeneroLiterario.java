package com.example.Resenhou.Catalogo.Model;

public enum GeneroLiterario {
    ROMANCE("Romance"),
    FANTASIA("Fantasia"),
    FICCAO_CIENTIFICA("Ficção Científica"),
    DISTOPIA("Distopia"),
    SUSPENSE_POLICIAL("Suspense / Policial"),
    TERROR_HORROR("Terror / Horror"),
    AVENTURA("Aventura"),
    CONTO("Conto"),
    POESIA("Poesia"),
    BIOGRAFIA("Biografia");

    private final String descricao;

    GeneroLiterario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}