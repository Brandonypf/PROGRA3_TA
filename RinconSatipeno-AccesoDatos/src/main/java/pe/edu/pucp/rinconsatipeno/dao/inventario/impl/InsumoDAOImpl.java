package pe.edu.pucp.rinconsatipeno.dao.inventario.impl;
//public class AreaDAOImpl extends RegistroDAOImpl<Area> implements AreaDAO

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.Insumo;
import pe.edu.pucp.rinconsatipeno.dao.inventario.InsumoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsumoDAOImpl implements InsumoDAO {

    @Override
    public List<Insumo> findAll() throws SQLException {
        String sql = "SELECT * FROM insumo";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                PreparedStatement cmd = conn.prepareStatement(sql);
                ResultSet rs=cmd.executeQuery()){
               List<Insumo> insumos = new ArrayList<>();
               while (rs.next()){
                   insumos.add(mapear(rs));
               }
               return insumos;
        }catch (SQLException ex) {
            throw new RuntimeException("Error al listar insumos: " + ex.getMessage(), ex);
        }
    }


    @Override
    public Insumo findBy(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        String sql = "SELECT * FROM insumo WHERE id_insumo = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cmd = con.prepareStatement(sql)) {

            cmd.setInt(1, id);
            try (ResultSet rs = cmd.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public void insert(Insumo insumo) throws SQLException {
        if (insumo == null) {
            throw new IllegalArgumentException("El insumo no puede ser nulo");
        }

        String sql = "INSERT INTO insumo (nombre, unidad_medida, stock_actual, stock_minimo) VALUES (?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cmd = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cmd.setString(1, insumo.getNombre());
            cmd.setString(2, insumo.getUnidadMedida());
            cmd.setDouble(3, insumo.getStockActual());
            cmd.setDouble(4, insumo.getStockMinimo());

            cmd.executeUpdate();
            int idGeneradoKey;
            try (ResultSet claves = cmd.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                idGeneradoKey = claves.getInt(1);
            }

            int idGenerado = idGeneradoKey;
            if (idGenerado <= 0) {
                throw new SQLException("No se pudo obtener un ID válido para el insumo insertado.");
            }
            insumo.setId(idGenerado);
        }
    }

    @Override
    public void update(Insumo insumo) throws SQLException {
        if (insumo == null) {
            throw new IllegalArgumentException("El insumo no puede ser nulo");
        }

        String sql = "UPDATE insumo SET nombre = ?, unidad_medida = ?, stock_actual = ?, stock_minimo = ? WHERE id_insumo = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cmd = con.prepareStatement(sql)) {

            cmd.setInt(5, insumo.getId());
            cmd.setString(1, insumo.getNombre());
            cmd.setString(2, insumo.getUnidadMedida());
            cmd.setDouble(3, insumo.getStockActual());
            cmd.setDouble(4, insumo.getStockMinimo());

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo modificar el insumo");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        String sql = "DELETE FROM insumo WHERE id_insumo = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cmd = con.prepareStatement(sql)) {

            cmd.setInt(1, id);

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo eliminar el insumo");
            }
        }
    }

    private Insumo mapear(ResultSet rs) throws SQLException {
        Insumo insumo = new Insumo();
        insumo.setId(rs.getInt("id_insumo"));
        insumo.setNombre(rs.getString("nombre"));
        insumo.setUnidadMedida(rs.getString("unidad_medida"));
        insumo.setStockActual(rs.getDouble("stock_actual"));
        insumo.setStockMinimo(rs.getDouble("stock_minimo"));
        return insumo;
    }


    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM insumo WHERE id_insumo = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
