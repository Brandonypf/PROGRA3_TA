package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;
import pe.edu.pucp.rinconSatipeno.dao.DetallePedidoDao;
import pe.edu.pucp.rinconSatipeno.dao.PlatoDao;
import pe.edu.pucp.rinconSatipeno.dao.transacciones.TransactionsManager;

/**
 * Un DetallePedido no existe sin su Pedido (id_pedido es FK), y el modelo no guarda
 * a qué pedido pertenece. Por eso las altas y cambios se hacen desde el pedido,
 * con insertLineas / deleteLineas (mismo criterio que LineaOrdenVentaDAO del ejemplo).
 */
public class DetallePedidoDaoImplement implements DetallePedidoDao {

    private final PlatoDao platoDao = new PlatoDaoImplement();

    // ---------- Operaciones por pedido (declaradas en DetallePedidoDao) ----------
    @Override
    public void insertLineas(int idPedido, List<DetallePedido> detalles) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL insertar_detalle_pedido(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            for (DetallePedido detalle : detalles) {
                cs.setInt(1, detalle.getCantidadPlatos());
                cs.setDouble(2, detalle.getPrecioUnitario());
                cs.setDouble(3, detalle.getSubtotal());
                cs.setInt(4, detalle.getPlato().getIdPlato());
                cs.setInt(5, idPedido);
                cs.registerOutParameter(6, Types.INTEGER);

                cs.execute();
                detalle.setIdDetalle(cs.getInt(6));
            }
        }
    }

    @Override
    public void deleteLineas(int idPedido) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL eliminar_detalles_por_pedido(?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, idPedido);
            cs.executeUpdate();
        }
    }

    @Override
    public List<DetallePedido> findByPedidoId(int idPedido) throws SQLException {
        String sql = "{CALL listar_detalles_por_pedido(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idPedido);
            try (ResultSet rs = cs.executeQuery()) {
                List<DetallePedido> detalles = new ArrayList<>();
                while (rs.next()) {
                    detalles.add(mapear(rs));
                }
                return detalles;
            }
        }
    }

    // ---------- DaoGeneral ----------
    @Override
    public void insert(DetallePedido detalle) throws SQLException {
        throw new UnsupportedOperationException(
                "Un detalle solo se inserta junto a su pedido: use insertLineas(idPedido, detalles)");
    }

    @Override
    public void update(DetallePedido detalle) throws SQLException {
        throw new UnsupportedOperationException(
                "Un detalle se modifica desde su pedido: PedidoDao.update reemplaza sus líneas");
    }

    @Override
    public void delete(Integer idDetalle) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL eliminar_detalle_pedido(?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, idDetalle);
            cs.executeUpdate();
        }
    }

    @Override
    public DetallePedido findBy(Integer idDetalle) throws SQLException {
        String sql = "{CALL buscar_detalle_pedido_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idDetalle);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public ArrayList<DetallePedido> findAll() throws SQLException {
        String sql = "{CALL listar_detalles_pedido()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            ArrayList<DetallePedido> detalles = new ArrayList<>();
            while (rs.next()) {
                detalles.add(mapear(rs));
            }
            return detalles;
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