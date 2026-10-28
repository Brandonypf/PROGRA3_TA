package pe.edu.pucp.rinconsatipeno.dao.pedidos;

import pe.edu.pucp.rinconsatipeno.dao.GeneralDAO;

import pe.edu.pucp.rinconsatipeno.modelo.pedidos.Pedido;
import pe.edu.pucp.rinconsatipeno.modelo.pedidos.DetallePedido;
import java.sql.SQLException;

public interface PedidoDAO extends GeneralDAO<Pedido,Integer>{
    void agregarDetalle(int idPedido, DetallePedido detalle) throws SQLException;

}
