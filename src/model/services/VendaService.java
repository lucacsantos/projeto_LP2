package model.services;

import model.dao.DaoFactory;
import model.dao.ProdutoDao;
import model.dao.VendaDao;
import model.entities.Produto;
import model.entities.Vendas;

import java.util.List;

public class VendaService {
    private VendaDao dao = DaoFactory.createVendaDao();
    public List<Vendas> buscarTodos(){
        return dao.buscarTodos();
    }

    public void salvarOuAtualizar(Vendas obj) {
        if (obj.getId() == null) {
            dao.inserir(obj);
        }
        else {
            dao.atualizar(obj);
        }
    }

    public void remover(Vendas obj) {
        dao.deletarPorId(obj.getId());
    }
}
