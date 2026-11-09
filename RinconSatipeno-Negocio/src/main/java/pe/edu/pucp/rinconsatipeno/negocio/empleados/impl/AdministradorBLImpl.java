package pe.edu.pucp.rinconsatipeno.negocio.empleados.impl;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.modelo.empleados.Administrador;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.DatosAcceso;
import pe.edu.pucp.rinconsatipeno.negocio.empleados.AdministradorBL;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.dao.empleados.AdministradorDAO;
import pe.edu.pucp.rinconsatipeno.dao.empleados.DatosAccesoDAO;
import pe.edu.pucp.rinconsatipeno.dao.empleados.impl.AdministradorDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.empleados.impl.DatosAccesoDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;

public class AdministradorBLImpl implements AdministradorBL {

    private final AdministradorDAO adminDao = new AdministradorDAOImpl();
    private final DatosAccesoDAO cuentaDao = new DatosAccesoDAOImpl();

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

            DatosAcceso cuenta = admin.getDatosAcceso();
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

            cuentaDao.update(admin.getDatosAcceso());
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
            cuentaDao.delete(admin.getDatosAcceso().getIdCuentaAcceso());

            TransactionsManager.commit();
        } catch (Exception ex) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo eliminar el administrador", ex);
        }
    }

    private void validarExiste(int id) throws BLException {
        try {
            if (!adminDao.existsById(id)) {
                throw new BLException("No se encontró al administrador especificado");
            }
        } catch (SQLException ex) {
            throw new BLException("Error al validar la existencia del administrador", ex);
        }
    }
}