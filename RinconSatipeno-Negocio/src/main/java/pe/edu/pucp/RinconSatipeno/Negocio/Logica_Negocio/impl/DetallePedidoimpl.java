package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.DetallePedidoLN;
import pe.edu.pucp.rinconSatipeno.dao.CuentaConsumoDao;
import pe.edu.pucp.rinconSatipeno.dao.DetallePedidoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.CuentaConsumoDaoImplement;
import pe.edu.pucp.rinconSatipeno.dao.impl.DetallePedidoDaoImplement;

import java.sql.SQLException;
import java.util.List;

public class DetallePedidoimpl implements DetallePedidoLN {
    private final DetallePedidoDao detallePedidoAux = new DetallePedidoDaoImplement();

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
            detallePedidoAux.findBy(id);
        } catch (SQLException variableSQL){
            throw new BLException("No se encontró la existencia de la cuenta de consumo");
        }
    }
}
