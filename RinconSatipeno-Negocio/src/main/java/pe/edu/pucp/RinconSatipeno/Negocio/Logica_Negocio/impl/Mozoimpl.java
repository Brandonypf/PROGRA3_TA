package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.RinconSatipeno.Modelo.empleados.DatosCuentaEmpleado;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Mozo;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.MozoLN;
import pe.edu.pucp.rinconSatipeno.dao.DatosCuentaEmpleadoDao;
import pe.edu.pucp.rinconSatipeno.dao.MozoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.DatosCuentaEmpleadoDaoImplement;
import pe.edu.pucp.rinconSatipeno.dao.impl.MozoDaoImplement;
import pe.edu.pucp.rinconSatipeno.dao.transacciones.TransactionsManager;

public class Mozoimpl implements MozoLN {

    private final MozoDao mozoDao = new MozoDaoImplement();
    private final DatosCuentaEmpleadoDao cuentaDao = new DatosCuentaEmpleadoDaoImplement();

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

            DatosCuentaEmpleado cuenta = mozo.getDatosCuentaEmpleado();
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

            cuentaDao.update(mozo.getDatosCuentaEmpleado());
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
            cuentaDao.delete(mozo.getDatosCuentaEmpleado().getIdCuentaAcceso());

            TransactionsManager.commit();
        } catch (Exception ex) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo eliminar el mozo", ex);
        }
    }

    private void validarExiste(int id) throws BLException {
        try {
            if (mozoDao.findBy(id) == null) {
                throw new BLException("No se encontró la existencia del mozo");
            }
        } catch (SQLException ex) {
            throw new BLException("Error al consultar la existencia del mozo", ex);
        }
    }
}