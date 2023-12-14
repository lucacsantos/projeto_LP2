package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.VendaDao;
import model.entities.Vendas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDaoJDBC implements VendaDao {
    private Connection conn;

    public VendaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Vendas buscarPorId(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM vendas  WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Vendas obj = new Vendas();
                obj.setId(rs.getInt("Id"));
                obj.setIdCliente(rs.getInt("IdCliente"));
                obj.setIdFuncionario(rs.getInt("IdFuncionario"));
                obj.setPreco(rs.getDouble("Preco"));
                obj.setNomeProduto(rs.getString("NomeProduto"));
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
    public List<Vendas> buscarTodos() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM vendas ORDER BY NomeProduto");
            rs = st.executeQuery();

            List<Vendas> list = new ArrayList<>();

            while (rs.next()) {
                Vendas obj = new Vendas();
                obj.setId(rs.getInt("Id"));
                obj.setIdCliente(rs.getInt("IdCliente"));
                obj.setIdFuncionario(rs.getInt("IdFuncionario"));
                obj.setPreco(rs.getDouble("Preco"));
                obj.setNomeProduto(rs.getString("NomeProduto"));
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
    public void inserir(Vendas obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("insert into vendas(idcliente, idfuncionario, " +
                            "preco, nomeproduto) values(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, obj.getIdCliente());
            st.setInt(2, obj.getIdFuncionario());
            st.setDouble(3, obj.getPreco());
            st.setString(4, obj.getNomeProduto());
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
    public void atualizar(Vendas obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("update vendas set idcliente = ?, idfuncionario = ?," +
                    " preco = ?, nomeproduto = ?" +
                    " where id = ?");
            st.setInt(1, obj.getIdCliente());
            st.setInt(2, obj.getIdFuncionario());
            st.setDouble(3, obj.getPreco());
            st.setString(4, obj.getNomeProduto());
            st.setInt(5, obj.getId());

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
                    "DELETE FROM vendas WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
}