package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import java.sql.SQLException;
import java.util.List;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.EstadoMesa;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.MesaLN;
import pe.edu.pucp.rinconSatipeno.dao.MesaDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.MesaDaoImplement;

public class Mesaimpl implements MesaLN {

    private final MesaDao mesaDAO = new MesaDaoImplement();

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
        for (Mesa otra : findAll()) {
            if (otra.getNumero() == mesa.getNumero() && otra.getId() != mesa.getId()) {
                throw new BLException("Ya existe una mesa con el número " + mesa.getNumero());
            }
        }
    }
}