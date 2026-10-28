package pe.edu.pucp.rinconsatipeno.negocio.cuentas.impl;

import java.sql.SQLException;
import java.util.List;
import pe.edu.pucp.rinconsatipeno.modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.cuentas.CuentaConsumoBL;
import pe.edu.pucp.rinconsatipeno.dao.cuentas.CuentaConsumoDAO;
import pe.edu.pucp.rinconsatipeno.dao.cuentas.impl.CuentaConsumoDAOImpl;





public class CuentaConsumoBLImpl implements CuentaConsumoBL{

    //Nos aseguramos que la clase solo sea asignada una vez (durante su creación)
    //Utilizamos DAO porque el modelo de negocio solo conoce los contratos, más no la lógica del negocio.
    private final CuentaConsumoDAO cuentaDAO = new CuentaConsumoDAOImpl();

    @Override
    public List<CuentaConsumo> findAll() throws BLException{
        try{
            return cuentaDAO.findAll();
        } catch (SQLException variableException){
            throw  new BLException("No se pudo listar todas las cuentas de consumo", variableException);
        }
    }


    @Override
    public CuentaConsumo findBy(Integer id) throws BLException{
        try{
            return cuentaDAO.findBy(id);
        } catch (SQLException variableException){
            throw new BLException("No se pudo encontrar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void insert(CuentaConsumo cuentaconsumo) throws BLException{
        try{
            cuentaDAO.insert(cuentaconsumo);
        } catch (SQLException variableException){
            throw new BLException("No se pudo insertar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void update(CuentaConsumo aux) throws BLException{
        validarExiste(aux.getIdCuenta());
        try{
            cuentaDAO.update(aux);
        } catch (SQLException variableException){
            throw new BLException("No se pudo actualizar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException{
        try{
            cuentaDAO.delete(id);
        } catch (SQLException variableException){
            throw new BLException("No se pudo eliminar la cuenta de consumo");
        }
    }

    public void validarExiste(int id) throws BLException{
        try{
            if (!cuentaDAO.existsById(id)) {
                throw new BLException("No existe el cuenta de consumo");
            }
        } catch (SQLException variableSQL){
            throw new BLException("No se encontró la existencia de la cuenta de consumo");
        }
    }

}












