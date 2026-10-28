package pe.edu.pucp.rinconsatipeno.negocio.pedidos.impl;

import pe.edu.pucp.rinconsatipeno.modelo.pedidos.DetallePedido;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.pedidos.DetallePedidoBL;
import pe.edu.pucp.rinconsatipeno.dao.cuentas.CuentaConsumoDAO;
import pe.edu.pucp.rinconsatipeno.dao.pedidos.DetallePedidoDAO;
import pe.edu.pucp.rinconsatipeno.dao.cuentas.impl.CuentaConsumoDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.pedidos.impl.DetallePedidoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class DetallePedidoBLImpl implements DetallePedidoBL {
    private final DetallePedidoDAO detallePedidoAux = new DetallePedidoDAOImpl();

    @Override
    public List<DetallePedido> findAll() throws BLException {
        try{
            return detallePedidoAux.findAll();
        } catch (SQLException variableException){
            throw  new BLException("No se pudo listar todas las cuentas de consumo", variableException);
        }
    }


    @Override
    public DetallePedido findBy(Integer id) throws BLException{
        try{
            return detallePedidoAux.findBy(id);
        } catch (SQLException variableException){
            throw new BLException("No se pudo encontrar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void insert(DetallePedido cuentaconsumo) throws BLException{
        try{
            detallePedidoAux.insert(cuentaconsumo);
        } catch (SQLException variableException){
            throw new BLException("No se pudo insertar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void update(DetallePedido aux) throws BLException{
        validarExiste(aux.getIdDetalle());
        try{
            detallePedidoAux.update(aux);
        } catch (SQLException variableException){
            throw new BLException("No se pudo actualizar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException{
        try{
            detallePedidoAux.delete(id);
        } catch (SQLException variableException){
            throw new BLException("No se pudo eliminar la cuenta de consumo");
        }
    }

    public void validarExiste(int id) throws BLException{
        try{
            if (!detallePedidoAux.existsById(id)) {
                throw new BLException("No existe el detalle de pedido");
            }
        } catch (SQLException variableSQL){
            throw new BLException("No se encontró la existencia de la cuenta de consumo");
        }
    }
}
