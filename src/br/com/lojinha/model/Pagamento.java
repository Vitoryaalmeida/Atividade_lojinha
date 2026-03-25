package br.com.lojinha.model;

public class Pagamento {
    private final int id;
    private final TipoPagamento tipoPagamento;
    private PagamentoStatus statusPagamento;
    private final Pedido pedido;

    public Pagamento(int id, TipoPagamento tipoPagamento, PagamentoStatus statusPagamento, Pedido pedido) {
        this.id = id;
        this.tipoPagamento = tipoPagamento;
        this.statusPagamento = statusPagamento;
        this.pedido = pedido;
    }

    public int getId() {
        return id;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public PagamentoStatus getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(PagamentoStatus statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public Pedido getPedido() {
        return pedido;
    }

    @Override
    public String toString() {
        return "Pagamento{id=%d, tipo=%s, status=%s}".formatted(id, tipoPagamento, statusPagamento);
    }
}
