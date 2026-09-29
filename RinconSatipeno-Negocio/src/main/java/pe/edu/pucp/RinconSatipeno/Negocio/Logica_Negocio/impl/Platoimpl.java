package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import pe.edu.pucp.RinconSatipeno.Modelo.pedidos.DetallePedido;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.PlatoLN;
import pe.edu.pucp.rinconSatipeno.dao.PlatoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.PlatoDaoImplement;

import java.sql.SQLException;
import java.util.List;

public class Platoimpl implements PlatoLN {
    private final PlatoDao platoDao = new PlatoDaoImplement();

    @Override
    public List<Plato> findAll() throws BLException {
        try{
            return platoDao.findAll();
        } catch (SQLException variableException){
            throw  new BLException("No se pudo listar todas las cuentas de consumo", variableException);
        }
    }


    @Override
    public Plato findBy(Integer id) throws BLException{
        try{
            return platoDao.findBy(id);
        } catch (SQLException variableException){
            throw new BLException("No se pudo encontrar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void insert(Plato aux) throws BLException{
        try{
            platoDao.insert(aux);
        } catch (SQLException variableException){
            throw new BLException("No se pudo insertar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void update(Plato aux) throws BLException{
        validarExiste(aux.getIdPlato());
        try{
            platoDao.update(aux);
        } catch (SQLException variableException){
            throw new BLException("No se pudo actualizar la cuenta de consumo", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException{
        try{
            platoDao.delete(id);
        } catch (SQLException variableException){
            throw new BLException("No se pudo eliminar la cuenta de consumo");
        }
    }

    public void validarExiste(int id) throws BLException{
        try{
            platoDao.findBy(id);
        } catch (SQLException variableSQL){
            throw new BLException("No se encontró la existencia de la cuenta de consumo");
        }
    }
}
