package br.com.lojinha.repository;

import br.com.lojinha.model.Pedido;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class PedidoRepository {
    private final Map<Integer, Pedido> pedidos = new LinkedHashMap<>();

    public void salvar(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
    }

    public Optional<Pedido> buscarPorId(int id) {
        return Optional.ofNullable(pedidos.get(id));
    }

    public Collection<Pedido> listarTodos() {
        return pedidos.values();
    }
}
