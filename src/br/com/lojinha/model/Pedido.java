package br.com.lojinha.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final int id;
    private final LocalDateTime dataPedido;
    private PedidoStatus statusPedido;
    private BigDecimal valorTotal;
    private final Cliente cliente;
    private final List<ItemPedido> itens;
    private Pagamento pagamento;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.dataPedido = LocalDateTime.now();
        this.statusPedido = PedidoStatus.CRIADO;
        this.valorTotal = BigDecimal.ZERO;
        this.itens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public PedidoStatus getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(PedidoStatus statusPedido) {
        this.statusPedido = statusPedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        valorTotal = valorTotal.add(item.getSubtotal());
    }

    @Override
    public String toString() {
        return "Pedido{id=%d, cliente='%s', status=%s, total=%s}"
                .formatted(id, cliente.getNome(), statusPedido, valorTotal);
    }
}
