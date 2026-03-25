package br.com.lojinha.model;

import java.math.BigDecimal;

public class ItemPedido {
    private final int id;
    private final Produto produto;
    private final int quantidade;
    private final BigDecimal precoUnitario;

    public ItemPedido(int id, Produto produto, int quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public int getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    @Override
    public String toString() {
        return "%s x%d = %s".formatted(produto.getNome(), quantidade, getSubtotal());
    }
}
