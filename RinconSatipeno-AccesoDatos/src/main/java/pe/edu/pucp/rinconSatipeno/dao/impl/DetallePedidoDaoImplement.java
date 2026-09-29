package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;
import pe.edu.pucp.rinconSatipeno.dao.DetallePedidoDao;
import pe.edu.pucp.rinconSatipeno.dao.PlatoDao;

public class DetallePedidoDaoImplement implements DetallePedidoDao {

    private final PlatoDao platoDao = new PlatoDaoImplement();
    @Override
    public void insert(DetallePedido detalle) throws SQLException {
        String sql = "{CALL insertar_detalle_pedido(?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, detalle.getCantidadPlatos());
            cs.setDouble(2, detalle.getPrecioUnitario());
            cs.setDouble(3, detalle.getSubtotal());
            Plato aux = detalle.getPlato();
            cs.setInt(4, aux.getIdPlato());
            cs.setInt(5, detalle.getIdDetalle());
            cs.registerOutParameter(6, Types.INTEGER);

            cs.execute();
            int id = cs.getInt(6);
            detalle.setIdDetalle(id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar detalle de pedido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(DetallePedido detalle) throws SQLException {
        String sql = "{CALL modificar_detalle_pedido(?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, detalle.getCantidadPlatos());
            cs.setDouble(2, detalle.getPrecioUnitario());
            cs.setDouble(3, detalle.getSubtotal());
            Plato aux = detalle.getPlato();
            cs.setInt(4, aux.getIdPlato());
            cs.setInt(5, detalle.getIdDetalle());
            cs.setInt(6, detalle.getIdDetalle());

            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al modificar detalle de pedido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Integer idDetalle) throws SQLException {
        String sql = "{CALL eliminar_detalle_pedido(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idDetalle);
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar detalle de pedido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public DetallePedido findBy(Integer idDetalle) throws SQLException {
        String sql = "{CALL buscar_detalle_pedido_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idDetalle);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar detalle de pedido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ArrayList<DetallePedido> findAll() throws SQLException {
        String sql = "{CALL listar_detalles_pedido()}";
        ArrayList<DetallePedido> detalles = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapear(rs));
            }
            return detalles;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar detalles de pedido: " + ex.getMessage(), ex);
        }
    }
    private DetallePedido mapear(ResultSet rs) throws SQLException {
        DetallePedido aux = new DetallePedido();
        aux.setIdDetalle(rs.getInt("id_detalle"));
        aux.setCantidadPlatos(rs.getInt("cantidad_platos"));
        aux.setPrecioUnitario(rs.getDouble("precio_unitario"));
        Plato plato = platoDao.findBy(rs.getInt("id_plato"));
        aux.setPlato(plato);
        return aux;
    }
}
