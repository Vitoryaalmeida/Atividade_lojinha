package br.com.lojinha.server;

import br.com.lojinha.integration.GatewayPagamentoSingleton;
import br.com.lojinha.model.Cliente;
import br.com.lojinha.model.Pagamento;
import br.com.lojinha.model.Pedido;
import br.com.lojinha.model.Produto;
import br.com.lojinha.model.TipoPagamento;
import br.com.lojinha.repository.ClienteRepository;
import br.com.lojinha.repository.PedidoRepository;
import br.com.lojinha.repository.ProdutoRepository;
import br.com.lojinha.service.AutenticacaoService;
import br.com.lojinha.service.CatalogoService;
import br.com.lojinha.service.PagamentoService;
import br.com.lojinha.service.PedidoService;
import java.util.Collection;
import java.util.Map;

public class LojinhaServer {
    private final AutenticacaoService autenticacaoService;
    private final CatalogoService catalogoService;
    private final PedidoService pedidoService;
    private final PagamentoService pagamentoService;

    public LojinhaServer() {
        ClienteRepository clienteRepository = new ClienteRepository();
        ProdutoRepository produtoRepository = new ProdutoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        this.autenticacaoService = new AutenticacaoService(clienteRepository);
        this.catalogoService = new CatalogoService(produtoRepository);
        this.pedidoService = new PedidoService(catalogoService, pedidoRepository);
        this.pagamentoService = new PagamentoService(GatewayPagamentoSingleton.getInstance());
    }

    public Cliente identificarCliente(int clienteId) {
        return autenticacaoService.identificarCliente(clienteId);
    }

    public Collection<Cliente> listarClientes() {
        return autenticacaoService.listarClientes();
    }

    public Collection<Produto> listarProdutos() {
        return catalogoService.listarProdutos();
    }

    public Pedido criarPedido(Cliente cliente, Map<Integer, Integer> itens) {
        return pedidoService.criarPedido(cliente, itens);
    }

    public Pagamento processarPagamento(Pedido pedido, TipoPagamento tipoPagamento, String tokenPagamento) {
        return pagamentoService.processarPagamento(pedido, tipoPagamento, tokenPagamento);
    }
}
