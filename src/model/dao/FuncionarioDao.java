package model.dao;

import model.entities.Funcionario;

import java.util.List;

public interface FuncionarioDao {

    void inserir(Funcionario obj);

    void atualizar(Funcionario obj);

    void deletarPorId(Integer id);

    Funcionario buscarPorId(Integer id);

    List<Funcionario> buscarTodos();
}
