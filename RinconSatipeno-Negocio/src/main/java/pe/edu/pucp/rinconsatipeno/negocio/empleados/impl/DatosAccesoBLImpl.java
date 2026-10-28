package pe.edu.pucp.rinconsatipeno.negocio.empleados.impl;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.modelo.empleados.DatosAcceso;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.empleados.DatosAccesoBL;
import pe.edu.pucp.rinconsatipeno.dao.empleados.DatosAccesoDAO;
import pe.edu.pucp.rinconsatipeno.dao.empleados.impl.DatosAccesoDAOImpl;

public class DatosAccesoBLImpl implements DatosAccesoBL {

    private final DatosAccesoDAO datosCuentaDao = new DatosAccesoDAOImpl();

    @Override
    public List<DatosAcceso> findAll() throws BLException {
        try {
            return datosCuentaDao.findAll();
        } catch (SQLException ex) {
            throw new BLException("No se pudieron listar las cuentas de acceso", ex);
        }
    }

    @Override
    public DatosAcceso findBy(Integer id) throws BLException {
        try {
            return datosCuentaDao.findBy(id);
        } catch (SQLException ex) {
            throw new BLException("No se pudo obtener la cuenta de acceso con ID " + id, ex);
        }
    }

    @Override
    public DatosAcceso findByEmail(String email) throws BLException {
        try {
            return datosCuentaDao.findByEmail(email);
        } catch (SQLException ex) {
            throw new BLException("No se pudo obtener la cuenta de acceso asociada al email " + email, ex);
        }
    }

    @Override
    public void insert(DatosAcceso cuenta) throws BLException {
        try {
            datosCuentaDao.insert(cuenta);
        } catch (SQLException ex) {
            throw new BLException("No se pudo insertar la cuenta de acceso", ex);
        }
    }

    @Override
    public void update(DatosAcceso cuenta) throws BLException {
        validarExiste(cuenta.getIdCuentaAcceso());
        try {
            datosCuentaDao.update(cuenta);
        } catch (SQLException ex) {
            throw new BLException("No se pudo actualizar la cuenta de acceso", ex);
        }
    }

    @Override
    public void delete(Integer id) throws BLException {
        validarExiste(id);
        try {
            datosCuentaDao.delete(id);
        } catch (SQLException ex) {
            throw new BLException("No se pudo eliminar la cuenta de acceso", ex);
        }
    }

    private void validarExiste(Integer id) throws BLException {
        try {
            if (!datosCuentaDao.existsById(id)) {
                throw new BLException("No existe la cuenta de acceso con ID " + id);
            }
        } catch (SQLException ex) {
            throw new BLException("Error al validar la existencia de la cuenta de acceso", ex);
        }
    }
}