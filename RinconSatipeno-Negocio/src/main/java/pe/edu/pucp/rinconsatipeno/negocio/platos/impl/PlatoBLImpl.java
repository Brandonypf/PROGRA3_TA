package pe.edu.pucp.rinconsatipeno.negocio.platos.impl;

import pe.edu.pucp.rinconsatipeno.modelo.platos.Producto;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.platos.PlatoBL;
import pe.edu.pucp.rinconsatipeno.dao.platos.PlatoDAO;
import pe.edu.pucp.rinconsatipeno.dao.platos.impl.PlatoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class PlatoBLImpl implements PlatoBL {
    private final PlatoDAO platoDao = new PlatoDAOImpl();

    @Override
    public List<Producto> findAll() throws BLException {
        try{
            return platoDao.findAll();
        } catch (SQLException variableException){
            throw  new BLException("No se pudo listar todos los platos", variableException);
        }
    }


    @Override
    public Producto findBy(Integer id) throws BLException{
        try{
            return platoDao.findBy(id);
        } catch (SQLException variableException){
            throw new BLException("No se pudo encontrar el plato", variableException);
        }
    }

    @Override
    public void insert(Producto aux) throws BLException{
        try{
            platoDao.insert(aux);
        } catch (SQLException variableException){
            throw new BLException("No se pudo insertar el plato", variableException);
        }
    }

    @Override
    public void update(Producto aux) throws BLException{
        validarExiste(aux.getIdPlato());
        try{
            platoDao.update(aux);
        } catch (SQLException variableException){
            throw new BLException("No se pudo actualizar el plato", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException{
        try{
            platoDao.delete(id);
        } catch (SQLException variableException){
            throw new BLException("No se pudo eliminar el plato");
        }
    }

    public void validarExiste(int id) throws BLException{
        try{
            if (!platoDao.existsById(id)) {
                throw new BLException("No existe el plato");
            }
        } catch (SQLException variableSQL){
            throw new BLException("No se encontró la existencia de el plato");
        }
    }
}
