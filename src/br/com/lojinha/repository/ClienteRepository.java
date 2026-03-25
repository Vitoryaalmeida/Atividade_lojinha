package br.com.lojinha.repository;

import br.com.lojinha.model.Cliente;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ClienteRepository {
    private final Map<Integer, Cliente> clientes = new LinkedHashMap<>();

    public ClienteRepository() {
        salvar(new Cliente(1, "Ana Souza", "ana@lojinha.com", "hash-ana"));
        salvar(new Cliente(2, "Bruno Lima", "bruno@lojinha.com", "hash-bruno"));
        salvar(new Cliente(3, "Carla Rocha", "carla@lojinha.com", "hash-carla"));
    }

    public void salvar(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
    }

    public Optional<Cliente> buscarPorId(int id) {
        return Optional.ofNullable(clientes.get(id));
    }

    public Collection<Cliente> listarTodos() {
        return clientes.values();
    }
}
