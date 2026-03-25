package br.com.lojinha.client;

import br.com.lojinha.model.Cliente;
import br.com.lojinha.model.Pagamento;
import br.com.lojinha.model.Pedido;
import br.com.lojinha.model.Produto;
import br.com.lojinha.model.TipoPagamento;
import br.com.lojinha.server.LojinhaServer;
import java.util.LinkedHashMap;
import java.util.Map;

public class LojinhaClientApp {
    private final LojinhaServer server;

    public LojinhaClientApp(LojinhaServer server) {
        this.server = server;
    }

    public void executarSimulacao(boolean forcarFalha) {
        Cliente cliente = server.identificarCliente(1);
        System.out.println("Cliente autenticado: " + cliente);

        System.out.println("\nProdutos disponíveis:");
        for (Produto produto : server.listarProdutos()) {
            System.out.println(" - " + produto);
        }

        Map<Integer, Integer> carrinho = new LinkedHashMap<>();
        carrinho.put(1, 1);
        carrinho.put(2, 2);

        Pedido pedido = server.criarPedido(cliente, carrinho);
        System.out.println("\nPedido criado: " + pedido);
        pedido.getItens().forEach(item -> System.out.println(" - Item: " + item));

        String tokenPagamento = forcarFalha ? "recusar-pagamento" : "token-aprovado";
        Pagamento pagamento = server.processarPagamento(pedido, TipoPagamento.PIX, tokenPagamento);

        System.out.println("\nResultado do pagamento: " + pagamento);
        System.out.println("Status final do pedido: " + pedido.getStatusPedido());
    }
}
