package pe.edu.pucp.rinconsatipeno.dao.inventario.impl;

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.MovimientoInventario;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.TipoMovimiento;
import pe.edu.pucp.rinconsatipeno.dao.inventario.MovimientoInventarioDAO;
import pe.edu.pucp.rinconsatipeno.dao.inventario.InsumoDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDAOImpl implements MovimientoInventarioDAO {
    private final InsumoDAO insumoDao = new InsumoDAOImpl();
    @Override
    public List<MovimientoInventario> findAll() throws SQLException {
        String sql = "SELECT * FROM movimiento_inventario";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                PreparedStatement cmd = conn.prepareStatement(sql);
                ResultSet rs = cmd.executeQuery()) {
            List<MovimientoInventario> movimientos = new ArrayList<>();
            while (rs.next()) {
                movimientos.add(mapear(rs));
            }
            return movimientos;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar movimientos de inventario: " + ex.getMessage(), ex);
        }
    }

    @Override
    public MovimientoInventario findBy(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        String sql = "SELECT * FROM movimiento_inventario WHERE id_movimiento = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cmd = con.prepareStatement(sql)) {

            cmd.setInt(1, id);
            try (ResultSet rs = cmd.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public void insert(MovimientoInventario movimiento) throws SQLException {
        if (movimiento == null) {
            throw new IllegalArgumentException("El movimiento de inventario no puede ser nulo");
        }

        String sql = "INSERT INTO movimiento_inventario (id_insumo, tipo_movimiento, cantidad) VALUES (?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cmd = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cmd.setInt(1, movimiento.getInsumo().getId());
            cmd.setString(2, movimiento.getTipo().name());
            cmd.setDouble(3, movimiento.getCantidad());

            cmd.executeUpdate();
            int idGeneradoKey;
            try (ResultSet claves = cmd.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                idGeneradoKey = claves.getInt(1);
            }

            int idGenerado = idGeneradoKey;
            if (idGenerado <= 0) {
                throw new SQLException("No se pudo obtener un ID válido para el movimiento registrado.");
            }
            movimiento.setId(idGenerado);
        }
    }

    @Override
    public void update(MovimientoInventario movimiento) throws SQLException {
        if (movimiento == null) {
            throw new IllegalArgumentException("El movimiento de inventario no puede ser nulo");
        }

        String sql = "UPDATE movimiento_inventario SET id_insumo = ?, tipo_movimiento = ?, cantidad = ? WHERE id_movimiento = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cmd = con.prepareStatement(sql)) {

            cmd.setInt(4, movimiento.getId());
            cmd.setInt(1, movimiento.getInsumo().getId());
            cmd.setString(2, movimiento.getTipo().name());
            cmd.setDouble(3, movimiento.getCantidad());

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo modificar el movimiento de inventario");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        String sql = "DELETE FROM movimiento_inventario WHERE id_movimiento = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cmd = con.prepareStatement(sql)) {

            cmd.setInt(1, id);

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo eliminar el movimiento de inventario");
            }
        }
    }

    @Override
    public List<MovimientoInventario> findByInsumoId(int idInsumo) throws SQLException {
        List<MovimientoInventario> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimiento_inventario WHERE id_insumo = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idInsumo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(mapear(rs));
                }
            }
        }
        return movimientos;
    }

    private MovimientoInventario mapear(ResultSet rs) throws SQLException {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setId(rs.getInt("id_movimiento"));

        // Mapeo del insumo referenciado (solo se asigna la ID)
        if (movimiento.getInsumo() == null) {
            movimiento.setInsumo(insumoDao.findBy(rs.getInt("id_insumo")));
        }
        movimiento.getInsumo().setId(rs.getInt("id_insumo"));

        movimiento.setTipo(TipoMovimiento.valueOf((rs.getString("tipo_movimiento"))));
        movimiento.setCantidad(rs.getDouble("cantidad"));
        movimiento.setFecha(LocalDate.from(rs.getTimestamp("fecha_registro").toLocalDateTime()));
        return movimiento;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM movimiento_inventario WHERE id_movimiento = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
