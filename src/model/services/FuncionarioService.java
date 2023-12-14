package model.services;

import model.dao.DaoFactory;
import model.dao.FuncionarioDao;
import model.entities.Funcionario;

import java.util.List;

public class FuncionarioService {
    private FuncionarioDao dao = DaoFactory.createFuncionarioDao();
        public List<Funcionario> buscarTodos(){
        return dao.buscarTodos();
    }

    public void salvarOuAtualizar(Funcionario obj) {
        if (obj.getId() == null) {
            dao.inserir(obj);
        }
        else {
            dao.atualizar(obj);
        }
    }

    public void remover(Funcionario obj) {
        dao.deletarPorId(obj.getId());
    }
}
