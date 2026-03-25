package br.com.lojinha.service;

import br.com.lojinha.integration.GatewayPagamentoSingleton;
import br.com.lojinha.model.Pagamento;
import br.com.lojinha.model.PagamentoStatus;
import br.com.lojinha.model.Pedido;
import br.com.lojinha.model.PedidoStatus;
import br.com.lojinha.model.TipoPagamento;
import java.util.concurrent.atomic.AtomicInteger;

public class PagamentoService {
    private final GatewayPagamentoSingleton gatewayPagamento;
    private final AtomicInteger pagamentoSequence = new AtomicInteger(1);

    public PagamentoService(GatewayPagamentoSingleton gatewayPagamento) {
        this.gatewayPagamento = gatewayPagamento;
    }

    public Pagamento processarPagamento(Pedido pedido, TipoPagamento tipoPagamento, String tokenPagamento) {
        pedido.setStatusPedido(PedidoStatus.AGUARDANDO_PAGAMENTO);

        Pagamento pagamento = new Pagamento(
                pagamentoSequence.getAndIncrement(),
                tipoPagamento,
                PagamentoStatus.PENDENTE,
                pedido
        );

        boolean aprovado = gatewayPagamento.processarPagamento(pedido, tokenPagamento);
        if (aprovado) {
            pagamento.setStatusPagamento(PagamentoStatus.APROVADO);
            pedido.setStatusPedido(PedidoStatus.PAGO);
        } else {
            pagamento.setStatusPagamento(PagamentoStatus.RECUSADO);
            pedido.setStatusPedido(PedidoStatus.PAGAMENTO_RECUSADO);
        }

        pedido.setPagamento(pagamento);
        return pagamento;
    }
}
