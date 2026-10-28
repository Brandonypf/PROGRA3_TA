package pe.edu.pucp.rinconsatipeno.dao.pedidos.impl;

import pe.edu.pucp.rinconsatipeno.dao.cuentas.impl.CuentaConsumoDAOImpl;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;
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
        String sql = "INSERT INTO pedido (fecha_hora, id_cuenta_consumo) VALUES (?, ?)";

        try (PreparedStatement cs = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            cs.setTimestamp(1, Timestamp.valueOf(pedido.getFechaHora()));
            cs.setInt(2, pedido.getCuentaConsumo().getIdCuenta());

            cs.executeUpdate();
            int idGeneradoKey;
            try (ResultSet claves = cs.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                idGeneradoKey = claves.getInt(1);
            }
            pedido.setIdPedido(idGeneradoKey);

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
        String sql = "UPDATE pedido SET fecha_hora = ?, id_cuenta_consumo = ? WHERE id_pedido = ?";

        try (PreparedStatement cs = con.prepareStatement(sql)) {
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
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";

        detallesDao.deleteLineas(idPedido);   // primero los hijos (FK detalle_pedido -> pedido)

        try (PreparedStatement cs = con.prepareStatement(sql)) {
            cs.setInt(1, idPedido);
            cs.executeUpdate();
        }
    }

    @Override
    public Pedido findBy(Integer idPedido) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idPedido);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public ArrayList<Pedido> findAll() throws SQLException {
        String sql = "SELECT * FROM pedido";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql);
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