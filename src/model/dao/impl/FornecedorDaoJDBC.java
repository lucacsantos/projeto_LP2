package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.FornecedorDao;
import model.entities.Fornecedor;
public class FornecedorDaoJDBC implements FornecedorDao {
    private Connection conn;

    public FornecedorDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Fornecedor buscarPorId(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM fornecedor  WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Fornecedor obj = new Fornecedor();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                obj.setEmpresa(rs.getString("Empresa"));
                obj.setEndereco(rs.getString("Endereco"));

                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Fornecedor> buscarTodos() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM fornecedor ORDER BY Nome");
            rs = st.executeQuery();

            List<Fornecedor> list = new ArrayList<>();

            while (rs.next()) {
                Fornecedor obj = new Fornecedor();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                obj.setEndereco(rs.getString("Endereco"));
                obj.setEmpresa(rs.getString("Empresa"));
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void inserir(Fornecedor obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("insert into fornecedor(nome, empresa,endereco) values(?, ? ,?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmpresa());
            st.setString(3, obj.getEndereco());
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha afetada!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void atualizar(Fornecedor obj) {
        PreparedStatement st = null;
        try {
            st=conn.prepareStatement("update fornecedor set nome = ?, empresa = ?, endereco = ? where id = ?");
            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmpresa());
            st.setString(3, obj.getEndereco());
            st.setInt(4, obj.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deletarPorId(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "DELETE FROM fornecedor WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
}
