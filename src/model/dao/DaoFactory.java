package model.dao;

import db.DB;
import model.dao.impl.*;

public class DaoFactory {

    public static ClienteDao createClienteDao() {
        return new ClienteDaoJDBC(DB.getConnection());
    }
    public static FornecedorDao createFornecedorDao(){ return new FornecedorDaoJDBC(DB.getConnection());}
    public static FuncionarioDao createFuncionarioDao(){ return new FuncionarioDaoJDBC(DB.getConnection());}
    public static ProdutoDao createProdutoDao(){ return new ProdutoDaoJDBC(DB.getConnection());}
    public static VendaDao createVendaDao(){ return new VendaDaoJDBC(DB.getConnection());}
}
