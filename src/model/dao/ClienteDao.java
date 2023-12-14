package model.dao;

import java.util.List;

import model.entities.Cliente;

public interface ClienteDao {

    void inserir(Cliente obj);

    void atualizar(Cliente obj);

    void deletarPorId(Integer id);

    Cliente buscarPorId(Integer id);

    List<Cliente> buscarTodos();
}
