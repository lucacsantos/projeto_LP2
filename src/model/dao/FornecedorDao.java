package model.dao;

import java.util.List;

import model.entities.Fornecedor;

public interface FornecedorDao {

    void inserir(Fornecedor obj);

    void atualizar(Fornecedor obj);

    void deletarPorId(Integer id);

    Fornecedor buscarPorId(Integer id);

    List<Fornecedor> buscarTodos();
}
