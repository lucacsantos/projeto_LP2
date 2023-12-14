package model.dao;

import model.entities.Produto;

import java.util.List;

public interface ProdutoDao {

    void inserir(Produto obj);

    void atualizar(Produto obj);

    void deletarPorId(Integer id);

    Produto buscarPorId(Integer id);

    List<Produto> buscarTodos();
}
