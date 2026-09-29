package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.RinconSatipeno.Modelo.empleados.DatosCuentaEmpleado;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.DatosCuentaEmpleadoLN;
import pe.edu.pucp.rinconSatipeno.dao.DatosCuentaEmpleadoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.DatosCuentaEmpleadoDaoImplement;

public class DatosCuentaEmpleadoimpl implements DatosCuentaEmpleadoLN {

    private final DatosCuentaEmpleadoDao datosCuentaDao = new DatosCuentaEmpleadoDaoImplement();

    @Override
    public List<DatosCuentaEmpleado> findAll() throws BLException {
        try {
            return datosCuentaDao.findAll();
        } catch (SQLException ex) {
            throw new BLException("No se pudieron listar las cuentas de acceso", ex);
        }
    }

    @Override
    public DatosCuentaEmpleado findBy(Integer id) throws BLException {
        try {
            return datosCuentaDao.findBy(id);
        } catch (SQLException ex) {
            throw new BLException("No se pudo obtener la cuenta de acceso con ID " + id, ex);
        }
    }

    @Override
    public DatosCuentaEmpleado findByEmail(String email) throws BLException {
        try {
            return datosCuentaDao.findByEmail(email);
        } catch (SQLException ex) {
            throw new BLException("No se pudo obtener la cuenta de acceso asociada al email " + email, ex);
        }
    }

    @Override
    public void insert(DatosCuentaEmpleado cuenta) throws BLException {
        try {
            datosCuentaDao.insert(cuenta);
        } catch (SQLException ex) {
            throw new BLException("No se pudo insertar la cuenta de acceso", ex);
        }
    }

    @Override
    public void update(DatosCuentaEmpleado cuenta) throws BLException {
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
            if (datosCuentaDao.findBy(id) == null) {
                throw new BLException("No existe la cuenta de acceso con ID " + id);
            }
        } catch (SQLException ex) {
            throw new BLException("Error al validar la existencia de la cuenta de acceso", ex);
        }
    }
}