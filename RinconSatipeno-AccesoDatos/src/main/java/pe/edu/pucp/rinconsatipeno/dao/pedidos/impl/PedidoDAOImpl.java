package pe.edu.pucp.rinconsatipeno.dao.pedidos.impl;

import pe.edu.pucp.rinconsatipeno.dao.cuentas.impl.CuentaConsumoDAOImpl;

import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;

import pe.edu.pucp.rinconsatipeno.modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.rinconsatipeno.modelo.pedidos.Pedido;
import pe.edu.pucp.rinconsatipeno.modelo.pedidos.DetallePedido;
import pe.edu.pucp.rinconsatipeno.dao.cuentas.CuentaConsumoDAO;
import pe.edu.pucp.rinconsatipeno.dao.pedidos.DetallePedidoDAO;
import pe.edu.pucp.rinconsatipeno.dao.pedidos.PedidoDAO;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;

public class PedidoDAOImpl implements PedidoDAO {

    private final CuentaConsumoDAO cuentaDao = new CuentaConsumoDAOImpl();
    private final DetallePedidoDAO detallesDao = new DetallePedidoDAOImpl();

    @Override
    public void insert(Pedido pedido) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL insertar_pedido(?, ?, ?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setTimestamp(1, Timestamp.valueOf(pedido.getFechaHora()));
            cs.setInt(2, pedido.getCuentaConsumo().getIdCuenta());
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();
            pedido.setIdPedido(cs.getInt(3));

            detallesDao.insertLineas(pedido.getIdPedido(), pedido.getDetalles());
        }
    }

    @Override
    public void agregarDetalle(int idPedido, DetallePedido detalle) throws SQLException {
        detallesDao.insertarLinea(idPedido, detalle);
    }

    @Override
    public void update(Pedido pedido) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL modificar_pedido(?, ?, ?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setTimestamp(1, Timestamp.valueOf(pedido.getFechaHora()));
            cs.setInt(2, pedido.getCuentaConsumo().getIdCuenta());
            cs.setInt(3, pedido.getIdPedido());

            cs.executeUpdate();

            // Se reemplaza la lista completa de detalles
            detallesDao.deleteLineas(pedido.getIdPedido());
            detallesDao.insertLineas(pedido.getIdPedido(), pedido.getDetalles());
        }
    }

    @Override
    public void delete(Integer idPedido) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL eliminar_pedido(?)}";

        detallesDao.deleteLineas(idPedido);   // primero los hijos (FK detalle_pedido -> pedido)

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, idPedido);
            cs.executeUpdate();
        }
    }

    @Override
    public Pedido findBy(Integer idPedido) throws SQLException {
        String sql = "{CALL buscar_pedido_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idPedido);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public ArrayList<Pedido> findAll() throws SQLException {
        String sql = "{CALL listar_pedidos()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            ArrayList<Pedido> pedidos = new ArrayList<>();
            while (rs.next()) {
                pedidos.add(mapear(rs));
            }
            return pedidos;
        }
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido aux = new Pedido();
        aux.setIdPedido(rs.getInt("id_pedido"));
        aux.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        CuentaConsumo cuenta = cuentaDao.findBy(rs.getInt("id_cuenta_consumo"));
        aux.setCuentaConsumo(cuenta);
        aux.setDetalles(detallesDao.findByPedidoId(aux.getIdPedido()));
        return aux;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM pedido WHERE id_pedido = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}