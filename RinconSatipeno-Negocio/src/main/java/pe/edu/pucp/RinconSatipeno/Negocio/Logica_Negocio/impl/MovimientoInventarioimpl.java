package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import pe.edu.pucp.RinconSatipeno.Modelo.inventario.MovimientoInventario;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.MovimientoInventarioLN;
import pe.edu.pucp.rinconSatipeno.dao.MovimientoInventarioDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.MovimientoInventarioDaoImplement;

import java.sql.SQLException;
import java.util.List;

public class MovimientoInventarioimpl implements MovimientoInventarioLN {
    private final MovimientoInventarioDao movimientoDao = new MovimientoInventarioDaoImplement();

    @Override
    public List<MovimientoInventario> findAll() throws BLException {
        try {
            return movimientoDao.findAll();
        } catch (SQLException variableException) {
            throw new BLException("No se pudo listar los movimientos de inventario", variableException);
        }
    }

    @Override
    public MovimientoInventario findBy(Integer id) throws BLException {
        if (id == null || id <= 0) {
            throw new BLException("El ID del movimiento de inventario ingresado no es válido.");
        }
        try {
            return movimientoDao.findBy(id);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo encontrar el movimiento de inventario especificado", variableException);
        }
    }

    @Override
    public void insert(MovimientoInventario movimiento) throws BLException {
        validarCamposMovimiento(movimiento);
        try {
            movimientoDao.insert(movimiento);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo registrar el movimiento de inventario", variableException);
        }
    }

    @Override
    public void update(MovimientoInventario movimiento) throws BLException {
        if (movimiento == null) {
            throw new BLException("El movimiento de inventario a actualizar debe tener un ID válido.");
        }
        // Regla de negocio: Verificar la existencia antes de actualizar
        validarExiste(movimiento.getId());
        validarCamposMovimiento(movimiento);
        try {
            movimientoDao.update(movimiento);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo actualizar el movimiento de inventario", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException {
        if (id == null || id <= 0) {
            throw new BLException("El ID del movimiento de inventario a eliminar no es válido.");
        }
        // Regla de negocio: Verificar la existencia antes de eliminar
        validarExiste(id);
        try {
            movimientoDao.delete(id);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo eliminar el movimiento de inventario", variableException);
        }
    }

    private void validarExiste(int id) throws BLException {
        try {
            MovimientoInventario movimiento = movimientoDao.findBy(id);
            if (movimiento == null) {
                throw new BLException("No se encontró la existencia del movimiento de inventario especificado.");
            }
        } catch (SQLException variableSQL) {
            throw new BLException("Error al verificar la existencia del movimiento de inventario", variableSQL);
        }
    }

    private void validarCamposMovimiento(MovimientoInventario movimiento) throws BLException {
        if (movimiento == null) {
            throw new BLException("El objeto movimiento de inventario no puede ser nulo.");
        }
        if (movimiento.getInsumo() == null || movimiento.getInsumo().getId() <= 0) {
            throw new BLException("El movimiento debe estar asociado a un insumo válido.");
        }
        if (movimiento.getTipo() == null) {
            throw new BLException("El tipo de movimiento (ENTRADA/SALIDA) es obligatorio.");
        }
        if (movimiento.getCantidad() <= 0) {
            throw new BLException("La cantidad del movimiento de inventario debe ser mayor a cero.");
        }
    }
}
