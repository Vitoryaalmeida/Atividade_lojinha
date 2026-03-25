package br.com.lojinha.repository;

import br.com.lojinha.model.Produto;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ProdutoRepository {
    private final Map<Integer, Produto> produtos = new LinkedHashMap<>();

    public ProdutoRepository() {
        salvar(new Produto(1, "Notebook", "Notebook 16GB RAM", new BigDecimal("3500.00"), 5));
        salvar(new Produto(2, "Mouse Gamer", "Mouse RGB com 7 botões", new BigDecimal("150.00"), 10));
        salvar(new Produto(3, "Teclado Mecânico", "Switch azul ABNT2", new BigDecimal("280.00"), 8));
        salvar(new Produto(4, "Monitor 24", "Monitor Full HD 24 polegadas", new BigDecimal("899.90"), 4));
    }

    public void salvar(Produto produto) {
        produtos.put(produto.getId(), produto);
    }

    public Optional<Produto> buscarPorId(int id) {
        return Optional.ofNullable(produtos.get(id));
    }

    public Collection<Produto> listarTodos() {
        return produtos.values();
    }
}
