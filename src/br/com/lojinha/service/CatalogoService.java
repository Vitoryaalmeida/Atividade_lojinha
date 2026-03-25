package br.com.lojinha.service;

import br.com.lojinha.exception.EntidadeNaoEncontradaException;
import br.com.lojinha.model.Produto;
import br.com.lojinha.repository.ProdutoRepository;
import java.util.Collection;

public class CatalogoService {
    private final ProdutoRepository produtoRepository;

    public CatalogoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Collection<Produto> listarProdutos() {
        return produtoRepository.listarTodos();
    }

    public Produto buscarProduto(int produtoId) {
        return produtoRepository.buscarPorId(produtoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto %d não encontrado.".formatted(produtoId)));
    }
}
