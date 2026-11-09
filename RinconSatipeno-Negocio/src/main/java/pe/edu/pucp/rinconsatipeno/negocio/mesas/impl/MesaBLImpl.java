package pe.edu.pucp.rinconsatipeno.negocio.mesas.impl;

import java.sql.SQLException;
import java.util.List;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.EstadoMesa;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.mesas.MesaBL;
import pe.edu.pucp.rinconsatipeno.dao.mesas.MesaDAO;
import pe.edu.pucp.rinconsatipeno.dao.mesas.impl.MesaDAOImpl;

public class MesaBLImpl implements MesaBL {

    private final MesaDAO mesaDAO = new MesaDAOImpl();

    @Override
    public List<Mesa> findAll() throws BLException {
        try {
            return mesaDAO.findAll();
        } catch (SQLException variableException) {
            throw new BLException("No se pudo listar todas las mesas", variableException);
        }
    }

    @Override
    public Mesa findBy(Integer id) throws BLException {
        try {
            return mesaDAO.findBy(id);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo encontrar la mesa", variableException);
        }
    }

    @Override
    public void insert(Mesa mesa) throws BLException {
        validarNumeroUnico(mesa);
        try {
            mesaDAO.insert(mesa);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo insertar la mesa", variableException);
        }
    }

    @Override
    public void update(Mesa aux) throws BLException {
        validarExiste(aux.getId());
        validarNumeroUnico(aux);
        try {
            mesaDAO.update(aux);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo actualizar la mesa", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException {
        Mesa mesa = validarExiste(id);
        if (mesa.getEstado() != EstadoMesa.LIBRE) {
            throw new BLException("Solo se puede eliminar una mesa que esté libre");
        }
        try {
            mesaDAO.delete(id);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo eliminar la mesa", variableException);
        }
    }

    public Mesa validarExiste(int id) throws BLException {
        Mesa mesa = findBy(id);
        if (mesa == null) {
            throw new BLException("No se encontró la existencia de la mesa");
        }
        return mesa;
    }

    private void validarNumeroUnico(Mesa mesa) throws BLException {
        try {
            if (mesaDAO.existeNumero(mesa.getNumero(), mesa.getId())) {
                throw new BLException("Ya existe una mesa con el número " + mesa.getNumero());
            }
        } catch (SQLException ex) {
            throw new BLException("No se pudo validar el número de mesa", ex);
        }
    }
}