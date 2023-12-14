package model.services;

import model.dao.DaoFactory;
import model.dao.FornecedorDao;
import model.entities.Fornecedor;

import java.util.List;

public class FornecedorService {
    private FornecedorDao dao = DaoFactory.createFornecedorDao();
    public List<Fornecedor> buscarTodos(){
        return dao.buscarTodos();
    }

    public void salvarOuAtualizar(Fornecedor obj) {
        if (obj.getId() == null) {
            dao.inserir(obj);
        }
        else {
            dao.atualizar(obj);
        }
    }

    public void remover(Fornecedor obj) {
        dao.deletarPorId(obj.getId());
    }
}