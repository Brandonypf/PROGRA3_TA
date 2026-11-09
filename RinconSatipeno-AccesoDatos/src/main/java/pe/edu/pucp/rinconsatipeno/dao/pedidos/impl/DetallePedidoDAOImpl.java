package pe.edu.pucp.rinconsatipeno.dao.pedidos.impl;

import pe.edu.pucp.rinconsatipeno.dao.platos.impl.PlatoDAOImpl;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.modelo.pedidos.DetallePedido;
import pe.edu.pucp.rinconsatipeno.modelo.platos.Producto;
import pe.edu.pucp.rinconsatipeno.dao.pedidos.DetallePedidoDAO;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;
import pe.edu.pucp.rinconsatipeno.dao.platos.PlatoDAO;

public class DetallePedidoDAOImpl implements DetallePedidoDAO {

    private final PlatoDAO platoDao = new PlatoDAOImpl();

    @Override
    public void insert(DetallePedido detalle) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cs.setInt(1, detalle.getCantidadPlatos());
            cs.setDouble(2, detalle.getPrecioUnitario());
            cs.setDouble(3, detalle.getSubtotal());
            Producto aux = detalle.getPlato();
            cs.setInt(4, aux != null ? aux.getIdPlato() : 0);
            cs.setInt(5, detalle.getIdDetalle());

            cs.executeUpdate();
            int idGeneradoKey;
            try (ResultSet claves = cs.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                idGeneradoKey = claves.getInt(1);
            }
            int id = idGeneradoKey;
            detalle.setIdDetalle(id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar detalle de pedido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(DetallePedido detalle) throws SQLException {
        String sql = "UPDATE detalle_pedido SET cantidad_platos = ?, precio_unitario = ?, subtotal = ?, id_plato = ?, id_pedido = ? WHERE id_detalle = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, detalle.getCantidadPlatos());
            cs.setDouble(2, detalle.getPrecioUnitario());
            cs.setDouble(3, detalle.getSubtotal());
            Producto aux = detalle.getPlato();
            cs.setInt(4, aux != null ? aux.getIdPlato() : 0);
            cs.setInt(5, detalle.getIdDetalle());
            cs.setInt(6, detalle.getIdDetalle());

            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al modificar detalle de pedido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Integer idDetalle) throws SQLException {
        String sql = "DELETE FROM detalle_pedido WHERE id_detalle = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idDetalle);
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar detalle de pedido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void insertarLinea(int idPedido, DetallePedido detalle) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (cantidad_platos, precio_unitario, subtotal, id_plato, id_pedido) VALUES (?, ?, ?, ?, ?)";
        Connection con = TransactionsManager.activa()
                ? TransactionsManager.getConnection()
                : DBManager.getInstance().getConnection();
        try (PreparedStatement cs = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            cs.setInt(1, detalle.getCantidadPlatos());
            cs.setDouble(2, detalle.getPrecioUnitario());
            cs.setDouble(3, detalle.getSubtotal());
            cs.setInt(4, detalle.getPlato().getIdPlato());
            cs.setInt(5, idPedido);

            cs.executeUpdate();
            int idGeneradoKey;
            try (ResultSet claves = cs.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                idGeneradoKey = claves.getInt(1);
            }
            detalle.setIdDetalle(idGeneradoKey);
        } finally {
            if (!TransactionsManager.activa()) con.close();
        }
    }

    @Override
    public void insertLineas(int idPedido, List<DetallePedido> detalles) throws SQLException {
        if (detalles == null) return;
        for (DetallePedido det : detalles) {
            insertarLinea(idPedido, det);
        }
    }

    @Override
    public void deleteLineas(int idPedido) throws SQLException {
        String sql = "DELETE FROM detalle_pedido WHERE id_pedido = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idPedido);
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar líneas de pedido: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<DetallePedido> findByPedidoId(int idPedido) throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido WHERE id_pedido = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idPedido);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapear(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar detalles por ID de pedido: " + ex.getMessage(), ex);
        }
        return detalles;
    }

    @Override
    public DetallePedido findBy(Integer idDetalle) throws SQLException {
        String sql = "SELECT * FROM detalle_pedido WHERE id_detalle = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idDetalle);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar detalle de pedido: " + ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public ArrayList<DetallePedido> findAll() throws SQLException {
        ArrayList<DetallePedido> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedido";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql);
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
        aux.setSubtotal(rs.getDouble("subtotal"));

        int idPlato = rs.getInt("id_plato");
        if (idPlato > 0) {
            Producto producto = platoDao.findBy(idPlato);
            aux.setPlato(producto);
        }
        return aux;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM detalle_pedido WHERE id_detalle = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}