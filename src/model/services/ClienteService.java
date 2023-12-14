package model.services;

import model.dao.DaoFactory;
import model.dao.ClienteDao;
import model.entities.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteService {
    private ClienteDao dao = DaoFactory.createClienteDao();
    public List<Cliente> buscarTodos(){
        return dao.buscarTodos();
    }

    public void salvarOuAtualizar(Cliente obj) {
        if (obj.getId() == null) {
            dao.inserir(obj);
        }
        else {
            dao.atualizar(obj);
        }
    }

    public void remover(Cliente obj) {
        dao.deletarPorId(obj.getId());
    }
}
