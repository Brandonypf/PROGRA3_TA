package pe.edu.pucp.rinconSatipeno.dao;

import java.sql.SQLException;
import java.util.List;
import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;

public interface DetallePedidoDao extends DaoGeneral<DetallePedido, Integer> {
    void insertLineas(int idPedido, List<DetallePedido> detalles) throws SQLException;
    void deleteLineas(int idPedido) throws SQLException;
    List<DetallePedido> findByPedidoId(int idPedido) throws SQLException;
}