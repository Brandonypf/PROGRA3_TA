package pe.edu.pucp.rinconsatipeno.dao.pedidos;

import pe.edu.pucp.rinconsatipeno.dao.GeneralDAO;

import java.sql.SQLException;
import java.util.List;
import pe.edu.pucp.rinconsatipeno.modelo.pedidos.DetallePedido;

public interface DetallePedidoDAO extends GeneralDAO<DetallePedido, Integer> {
    void insertarLinea(int idPedido, DetallePedido detalle) throws SQLException;
    void insertLineas(int idPedido, List<DetallePedido> detalles) throws SQLException;
    void deleteLineas(int idPedido) throws SQLException;
    List<DetallePedido> findByPedidoId(int idPedido) throws SQLException;
}