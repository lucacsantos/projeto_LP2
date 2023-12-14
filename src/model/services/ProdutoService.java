package model.services;

import model.dao.DaoFactory;
import model.dao.ProdutoDao;
import model.entities.Produto;

import java.util.List;

public class ProdutoService {
    private ProdutoDao dao = DaoFactory.createProdutoDao();
    public List<Produto> buscarTodos(){
        return dao.buscarTodos();
    }

    public void salvarOuAtualizar(Produto obj) {
        if (obj.getId() == null) {
            dao.inserir(obj);
        }
        else {
            dao.atualizar(obj);
        }
    }

    public void remover(Produto obj) {
        dao.deletarPorId(obj.getId());
    }
}