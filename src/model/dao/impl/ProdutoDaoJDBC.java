package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ProdutoDao;
import model.entities.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDaoJDBC implements ProdutoDao {
    private Connection conn;

    public ProdutoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Produto buscarPorId(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM produto  WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Produto obj = new Produto();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                obj.setLote(rs.getString("Lote"));
                obj.setPreco(rs.getDouble("Preco"));
                obj.setQuantidade(rs.getInt("Quantidade"));
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
    public List<Produto> buscarTodos() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * FROM produto ORDER BY Nome");
            rs = st.executeQuery();

            List<Produto> list = new ArrayList<>();

            while (rs.next()) {
                Produto obj = new Produto();
                obj.setId(rs.getInt("Id"));
                obj.setNome(rs.getString("Nome"));
                obj.setLote(rs.getString("lote"));
                obj.setPreco(rs.getDouble("Preco"));
                obj.setQuantidade(rs.getInt("Quantidade"));
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
    public void inserir(Produto obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("insert into produto(nome, lote, preco, quantidade) values(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getNome());
            st.setString(2, obj.getLote());
            st.setDouble(3, obj.getPreco());
            st.setInt(4, obj.getQuantidade());
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
    public void atualizar(Produto obj) {
        PreparedStatement st = null;
        try {
            st=conn.prepareStatement("update produto set nome = ?, lote = ?, preco = ?, quantidade = ?" +
                    " where id = ?");
            st.setString(1, obj.getNome());
            st.setString(2, obj.getLote());
            st.setDouble(3, obj.getPreco());
            st.setInt(4, obj.getQuantidade());
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
                    "DELETE FROM produto WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
}
