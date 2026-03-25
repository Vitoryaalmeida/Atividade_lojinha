package br.com.lojinha.service;

import br.com.lojinha.exception.EntidadeNaoEncontradaException;
import br.com.lojinha.model.Cliente;
import br.com.lojinha.repository.ClienteRepository;
import java.util.Collection;

public class AutenticacaoService {
    private final ClienteRepository clienteRepository;

    public AutenticacaoService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente identificarCliente(int clienteId) {
        return clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente %d não encontrado.".formatted(clienteId)));
    }

    public Collection<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }
}
