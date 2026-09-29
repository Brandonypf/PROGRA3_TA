package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Administrador;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.DatosCuentaEmpleado;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.AdministradorLN;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.rinconSatipeno.dao.AdministradorDao;
import pe.edu.pucp.rinconSatipeno.dao.DatosCuentaEmpleadoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.AdministradorDaoImplement;
import pe.edu.pucp.rinconSatipeno.dao.impl.DatosCuentaEmpleadoDaoImplement;
import pe.edu.pucp.rinconSatipeno.dao.transacciones.TransactionsManager;

public class Administradorimpl implements AdministradorLN {

    private final AdministradorDao adminDao = new AdministradorDaoImplement();
    private final DatosCuentaEmpleadoDao cuentaDao = new DatosCuentaEmpleadoDaoImplement();

    @Override
    public List<Administrador> findAll() throws BLException {
        try {
            return adminDao.findAll();
        } catch (SQLException ex) {
            throw new BLException("No se pudieron listar los administradores", ex);
        }
    }

    @Override
    public Administrador findBy(Integer id) throws BLException {
        try {
            return adminDao.findBy(id);
        } catch (SQLException ex) {
            throw new BLException("No se pudo encontrar el administrador con ID " + id, ex);
        }
    }

    @Override
    public void insert(Administrador admin) throws BLException {
        try {
            TransactionsManager.iniciar();

            DatosCuentaEmpleado cuenta = admin.getDatosCuentaEmpleado();
            cuentaDao.insert(cuenta);
            adminDao.insert(admin);

            TransactionsManager.commit();
        } catch (Exception ex) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo insertar el administrador", ex);
        }
    }

    @Override
    public void update(Administrador admin) throws BLException {
        validarExiste(admin.getIdEmpleado());
        try {
            TransactionsManager.iniciar();

            cuentaDao.update(admin.getDatosCuentaEmpleado());
            adminDao.update(admin);

            TransactionsManager.commit();
        } catch (Exception ex) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo actualizar el administrador", ex);
        }
    }

    @Override
    public void delete(Integer id) throws BLException {
        Administrador admin = findBy(id);
        if (admin == null) {
            throw new BLException("No existe el administrador que desea eliminar");
        }

        try {
            TransactionsManager.iniciar();

            adminDao.delete(id);
            cuentaDao.delete(admin.getDatosCuentaEmpleado().getIdCuentaAcceso());

            TransactionsManager.commit();
        } catch (Exception ex) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo eliminar el administrador", ex);
        }
    }

    private void validarExiste(int id) throws BLException {
        try {
            if (adminDao.findBy(id) == null) {
                throw new BLException("No se encontró al administrador especificado");
            }
        } catch (SQLException ex) {
            throw new BLException("Error al validar la existencia del administrador", ex);
        }
    }
}