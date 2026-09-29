package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import java.sql.SQLException;
import java.util.List;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.CuentaConsumoLN;
import pe.edu.pucp.rinconSatipeno.dao.CuentaConsumoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.CuentaConsumoDaoImplement;





public class CuentaConsumoimpl implements CuentaConsumoLN{

    //Nos aseguramos que la clase solo sea asignada una vez (durante su creación)
    //Utilizamos DAO porque el modelo de negocio solo conoce los contratos, más no la lógica del negocio.
    private final CuentaConsumoDao cuentaDAO = new CuentaConsumoDaoImplement();

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
    public void update(Integer id) throws BLException{
        validarExiste(id);
        try{
            cuentaDAO.update(id);
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
            cuentaDAO.findBy(id);
        } catch (SQLException variableSQL){
            throw new BLException("No se encontró la existencia de la cuenta de consumo");
        }

    }

}












