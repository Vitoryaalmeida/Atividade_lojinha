package br.com.lojinha.service;

import br.com.lojinha.exception.EstoqueInsuficienteException;
import br.com.lojinha.model.Cliente;
import br.com.lojinha.model.ItemPedido;
import br.com.lojinha.model.Pedido;
import br.com.lojinha.model.Produto;
import br.com.lojinha.repository.PedidoRepository;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PedidoService {
    private final CatalogoService catalogoService;
    private final PedidoRepository pedidoRepository;
    private final AtomicInteger pedidoSequence = new AtomicInteger(1);
    private final AtomicInteger itemSequence = new AtomicInteger(1);

    public PedidoService(CatalogoService catalogoService, PedidoRepository pedidoRepository) {
        this.catalogoService = catalogoService;
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido criarPedido(Cliente cliente, Map<Integer, Integer> itensSolicitados) {
        Pedido pedido = new Pedido(pedidoSequence.getAndIncrement(), cliente);

        for (Map.Entry<Integer, Integer> entrada : itensSolicitados.entrySet()) {
            Produto produto = catalogoService.buscarProduto(entrada.getKey());
            int quantidade = entrada.getValue();

            if (!produto.possuiEstoque(quantidade)) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o produto %s.".formatted(produto.getNome()));
            }

            produto.baixarEstoque(quantidade);
            ItemPedido item = new ItemPedido(itemSequence.getAndIncrement(), produto, quantidade, produto.getPreco());
            pedido.adicionarItem(item);
        }

        pedidoRepository.salvar(pedido);
        return pedido;
    }
}
