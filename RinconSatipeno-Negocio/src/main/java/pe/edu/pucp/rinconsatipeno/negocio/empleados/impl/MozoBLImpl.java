package pe.edu.pucp.rinconsatipeno.negocio.empleados.impl;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.modelo.empleados.DatosAcceso;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.Mozo;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.empleados.MozoBL;
import pe.edu.pucp.rinconsatipeno.dao.empleados.DatosAccesoDAO;
import pe.edu.pucp.rinconsatipeno.dao.empleados.MozoDAO;
import pe.edu.pucp.rinconsatipeno.dao.empleados.impl.DatosAccesoDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.empleados.impl.MozoDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;

public class MozoBLImpl implements MozoBL {

    private final MozoDAO mozoDao = new MozoDAOImpl();
    private final DatosAccesoDAO cuentaDao = new DatosAccesoDAOImpl();

    @Override
    public List<Mozo> findAll() throws BLException {
        try {
            return mozoDao.findAll();
        } catch (SQLException ex) {
            throw new BLException("No se pudieron listar los mozos", ex);
        }
    }

    @Override
    public Mozo findBy(Integer id) throws BLException {
        try {
            return mozoDao.findBy(id);
        } catch (SQLException ex) {
            throw new BLException("No se pudo obtener el mozo con ID " + id, ex);
        }
    }

    @Override
    public void insert(Mozo mozo) throws BLException {
        try {
            TransactionsManager.iniciar();

            DatosAcceso cuenta = mozo.getDatosAcceso();
            cuentaDao.insert(cuenta);
            mozoDao.insert(mozo);

            TransactionsManager.commit();
        } catch (Exception ex) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo insertar el mozo", ex);
        }
    }

    @Override
    public void update(Mozo mozo) throws BLException {
        validarExiste(mozo.getIdEmpleado());
        try {
            TransactionsManager.iniciar();

            cuentaDao.update(mozo.getDatosAcceso());
            mozoDao.update(mozo);

            TransactionsManager.commit();
        } catch (Exception ex) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo actualizar el mozo", ex);
        }
    }

    @Override
    public void delete(Integer id) throws BLException {
        Mozo mozo = findBy(id);
        if (mozo == null) {
            throw new BLException("No existe el mozo que desea eliminar");
        }

        try {
            TransactionsManager.iniciar();

            mozoDao.delete(id);
            cuentaDao.delete(mozo.getDatosAcceso().getIdCuentaAcceso());

            TransactionsManager.commit();
        } catch (Exception ex) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo eliminar el mozo", ex);
        }
    }

    private void validarExiste(int id) throws BLException {
        try {
            if (!mozoDao.existsById(id)) {
                throw new BLException("No se encontró la existencia del mozo");
            }
        } catch (SQLException ex) {
            throw new BLException("Error al consultar la existencia del mozo", ex);
        }
    }
}