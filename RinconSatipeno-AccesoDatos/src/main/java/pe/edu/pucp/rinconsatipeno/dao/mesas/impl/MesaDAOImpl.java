package pe.edu.pucp.rinconsatipeno.dao.mesas.impl;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;

import pe.edu.pucp.rinconsatipeno.modelo.mesas.EstadoMesa;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Zona;
import pe.edu.pucp.rinconsatipeno.dao.mesas.MesaDAO;

public class MesaDAOImpl implements MesaDAO {

    @Override
    public void insert(Mesa mesa) throws SQLException {
        String sql = "INSERT INTO mesa (numero, capacidad, zona, estado) VALUES (?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cs.setInt(1, mesa.getNumero());
            cs.setInt(2, mesa.getCapacidad());
            cs.setString(3, mesa.getZona().name());
            cs.setString(4, mesa.getEstado().name());

            cs.executeUpdate();
            int idGeneradoKey;
            try (ResultSet claves = cs.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                idGeneradoKey = claves.getInt(1);
            }
            mesa.setId(idGeneradoKey);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar mesa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(Mesa mesa) throws SQLException {
        String sql = "UPDATE mesa SET numero = ?, capacidad = ?, zona = ?, estado = ? WHERE id_mesa = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, mesa.getNumero());
            cs.setInt(2, mesa.getCapacidad());
            cs.setString(3, mesa.getZona().name());
            cs.setString(4, mesa.getEstado().name());
            cs.setInt(5, mesa.getId());

            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al modificar mesa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Integer idMesa) throws SQLException {
        String sql = "DELETE FROM mesa WHERE id_mesa = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idMesa);
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar mesa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Mesa findBy(Integer idMesa) throws SQLException {
        String sql = "SELECT * FROM mesa WHERE id_mesa = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idMesa);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar mesa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ArrayList<Mesa> findAll() throws SQLException {
        String sql = "SELECT * FROM mesa";
        ArrayList<Mesa> mesas = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                mesas.add(mapear(rs));
            }
            return mesas;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar mesas: " + ex.getMessage(), ex);
        }
    }

    private Mesa mapear(ResultSet rs) throws SQLException {
        Mesa aux = new Mesa();
        aux.setId(rs.getInt("id_mesa"));
        aux.setNumero(rs.getInt("numero"));
        aux.setCapacidad(rs.getInt("capacidad"));
        aux.setZona(Zona.valueOf(rs.getString("zona")));
        aux.setEstado(EstadoMesa.valueOf(rs.getString("estado")));
        return aux;
    }

    @Override
    public boolean existeNumero(int numero, int excluirId) throws SQLException {
        String sql = "SELECT 1 FROM mesa WHERE numero = ? AND id_mesa <> ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            ps.setInt(2, excluirId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM mesa WHERE id_mesa = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
