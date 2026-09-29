package pe.edu.pucp.rinconSatipeno.dao.impl;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.MovimientoInventario;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.TipoMovimiento;
import pe.edu.pucp.rinconSatipeno.dao.MovimientoInventarioDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDaoImplement implements MovimientoInventarioDao {
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

            cmd.setInt("p_id_movimiento", id);
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
        String sql = "{call insertar_movimiento_inventario(?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setInt("p_id_insumo", movimiento.getInsumo().getId());
            cmd.setString("p_tipo_movimiento", movimiento.getTipo().name());
            cmd.setDouble("p_cantidad", movimiento.getCantidad());

            cmd.registerOutParameter("p_id_movimiento", Types.INTEGER);

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo insertar el movimiento de inventario");
            }
            movimiento.setId(cmd.getInt("p_id_movimiento"));
        }
    }

    @Override
    public void update(MovimientoInventario movimiento) throws SQLException {
        if (movimiento == null) {
            throw new IllegalArgumentException("El movimiento de inventario no puede ser nulo");
        }
        String sql = "{call modificar_movimiento_inventario(?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setInt("p_id_movimiento", movimiento.getId());
            cmd.setInt("p_id_insumo", movimiento.getInsumo().getId());
            cmd.setString("p_tipo_movimiento", movimiento.getTipo().name());
            cmd.setDouble("p_cantidad", movimiento.getCantidad());

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

            cmd.setInt("p_id_movimiento", id);

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo eliminar el movimiento de inventario");
            }
        }
    }

    private MovimientoInventario mapear(ResultSet rs, MovimientoInventario movimiento) throws SQLException {
        movimiento.setId(rs.getInt("id_movimiento"));

        // Mapeo del insumo referenciado (solo se asigna la ID)
        if (movimiento.getInsumo() == null) {
            movimiento.setInsumo(new InsumoDaoImplement().findBy(rs.getInt("id_insumo")));
        }
        movimiento.getInsumo().setId(rs.getInt("id_insumo"));

        movimiento.setTipo(TipoMovimiento.valueOf((rs.getString("tipo_movimiento"))));
        movimiento.setCantidad(rs.getDouble("cantidad"));
        movimiento.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return movimiento;
    }
}
