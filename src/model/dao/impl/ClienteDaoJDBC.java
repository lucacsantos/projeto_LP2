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
import model.dao.ClienteDao;
import model.entities.Cliente;
public class ClienteDaoJDBC implements ClienteDao {
    private Connection conn;

    public ClienteDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM cliente  WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Cliente obj = new Cliente();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                obj.setEndereco(rs.getString("Endereco"));
                obj.setCPF(rs.getString("CPF"));
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
    public List<Cliente> buscarTodos() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM cliente ORDER BY Nome");
            rs = st.executeQuery();

            List<Cliente> list = new ArrayList<>();

            while (rs.next()) {
                Cliente obj = new Cliente();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                obj.setEndereco(rs.getString("Endereco"));
                obj.setCPF(rs.getString("CPF"));
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
    public void inserir(Cliente obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("insert into cliente(nome, endereco, cpf) values(?, ? ,?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getNome());
            st.setString(2, obj.getEndereco());
            st.setString(3, obj.getCPF());
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
    public void atualizar(Cliente obj) {
        PreparedStatement st = null;
        try {
            st=conn.prepareStatement("update cliente set nome = ?,endereco = ?, cpf = ? where id = ?");
            st.setString(1, obj.getNome());
            st.setString(2, obj.getEndereco());
            st.setString(3, obj.getCPF());
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
                    "DELETE FROM cliente WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
}
