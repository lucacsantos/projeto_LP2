package model.dao;


import model.entities.Vendas;

import java.util.List;

public interface VendaDao {

    void inserir(Vendas obj);

    void atualizar(Vendas obj);

    void deletarPorId(Integer id);

    Vendas buscarPorId(Integer id);

    List<Vendas> buscarTodos();
}
