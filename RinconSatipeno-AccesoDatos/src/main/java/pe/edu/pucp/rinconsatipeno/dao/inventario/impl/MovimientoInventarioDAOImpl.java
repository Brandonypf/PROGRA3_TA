package pe.edu.pucp.rinconsatipeno.dao.inventario.impl;

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.MovimientoInventario;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.TipoMovimiento;
import pe.edu.pucp.rinconsatipeno.dao.inventario.MovimientoInventarioDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDAOImpl implements MovimientoInventarioDAO {
    @Override
    public List<MovimientoInventario> findAll() throws SQLException {
        String sql = "{call listar_movimientos_inventario()}";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                CallableStatement cmd = conn.prepareCall(sql);
                ResultSet rs = cmd.executeQuery()) {
            List<MovimientoInventario> movimientos = new ArrayList<>();
            while (rs.next()) {
                movimientos.add(mapear(rs, new MovimientoInventario()));
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
        String sql = "{call buscar_movimiento_inventario_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setInt(1, id);
            try (ResultSet rs = cmd.executeQuery()) {
                return rs.next() ? mapear(rs, new MovimientoInventario()) : null;
            }
        }
    }

    @Override
    public void insert(MovimientoInventario movimiento) throws SQLException {
        if (movimiento == null) {
            throw new IllegalArgumentException("El movimiento de inventario no puede ser nulo");
        }
        // procedimiento: (p_id_insumo, p_tipo_movimiento, p_cantidad, OUT p_id_movimiento)
        String sql = "{call insertar_movimiento_inventario(?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setInt(1, movimiento.getInsumo().getId());
            cmd.setString(2, movimiento.getTipo().name());
            cmd.setDouble(3, movimiento.getCantidad());

            // Parámetro de salida OUT es el 4°
            cmd.registerOutParameter(4, Types.INTEGER);

            cmd.execute();

            int idGenerado = cmd.getInt(4);
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
        // procedimiento: (p_id_movimiento, p_id_insumo, p_tipo_movimiento, p_cantidad)
        String sql = "{call modificar_movimiento_inventario(?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setInt(1, movimiento.getId());
            cmd.setInt(2, movimiento.getInsumo().getId());
            cmd.setString(3, movimiento.getTipo().name());
            cmd.setDouble(4, movimiento.getCantidad());

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
        String sql = "{call eliminar_movimiento_inventario(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

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
                    movimientos.add(mapear(rs, new MovimientoInventario()));
                }
            }
        }
        return movimientos;
    }

    private MovimientoInventario mapear(ResultSet rs, MovimientoInventario movimiento) throws SQLException {
        movimiento.setId(rs.getInt("id_movimiento"));

        // Mapeo del insumo referenciado (solo se asigna la ID)
        if (movimiento.getInsumo() == null) {
            movimiento.setInsumo(new InsumoDAOImpl().findBy(rs.getInt("id_insumo")));
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
