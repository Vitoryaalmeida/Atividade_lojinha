package br.com.lojinha.model;

import java.math.BigDecimal;

public class Produto {
    private final int id;
    private final String nome;
    private final String descricao;
    private final BigDecimal preco;
    private int estoque;

    public Produto(int id, String nome, String descricao, BigDecimal preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public boolean possuiEstoque(int quantidade) {
        return estoque >= quantidade;
    }

    public void baixarEstoque(int quantidade) {
        estoque -= quantidade;
    }

    @Override
    public String toString() {
        return "Produto{id=%d, nome='%s', preco=%s, estoque=%d}"
                .formatted(id, nome, preco, estoque);
    }
}
