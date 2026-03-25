package br.com.lojinha.integration;

import br.com.lojinha.model.Pedido;

public class GatewayPagamentoSingleton {
    private static GatewayPagamentoSingleton instance;

    private GatewayPagamentoSingleton() {
    }

    public static synchronized GatewayPagamentoSingleton getInstance() {
        // Uma única instância centraliza a comunicação com o sistema externo
        // de pagamento durante toda a execução do monólito.
        if (instance == null) {
            instance = new GatewayPagamentoSingleton();
        }
        return instance;
    }

    public boolean processarPagamento(Pedido pedido, String tokenPagamento) {
        if (tokenPagamento == null || tokenPagamento.isBlank()) {
            return false;
        }
        String tokenNormalizado = tokenPagamento.trim().toUpperCase();
        if (tokenNormalizado.contains("RECUSAR")) {
            return false;
        }
        return pedido.getValorTotal().doubleValue() <= 5000.0;
    }
}
