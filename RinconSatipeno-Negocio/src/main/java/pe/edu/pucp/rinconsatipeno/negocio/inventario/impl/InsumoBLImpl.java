package pe.edu.pucp.rinconsatipeno.negocio.inventario.impl;

import pe.edu.pucp.rinconsatipeno.modelo.inventario.Insumo;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.inventario.InsumoBL;
import pe.edu.pucp.rinconsatipeno.dao.inventario.InsumoDAO;
import pe.edu.pucp.rinconsatipeno.dao.inventario.impl.InsumoDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class InsumoBLImpl implements InsumoBL {
    private final InsumoDAO insumoDao = new InsumoDAOImpl();
    @Override
    public List<Insumo> findAll() throws BLException {
        try {
            return insumoDao.findAll();
        } catch (SQLException variableException) {
            throw new BLException("No se pudo listar los insumos del inventario", variableException);
        }
    }

    @Override
    public Insumo findBy(Integer id) throws BLException {
        if (id == null || id <= 0) {
            throw new BLException("El ID del insumo ingresado no es válido.");
        }
        try {
            return insumoDao.findBy(id);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo encontrar el insumo especificado", variableException);
        }
    }

    @Override
    public void insert(Insumo insumo) throws BLException {
        validarCamposInsumo(insumo);
        try {
            insumoDao.insert(insumo);
        } catch (SQLException variableException) {
            // Imprime el error exacto que envía MySQL
            System.err.println("ERROR SQL EXACTO: " + variableException.getMessage());
            variableException.printStackTrace();
            throw new BLException("No se pudo registrar el insumo en el inventario", variableException);
        }
    }

    @Override
    public void update(Insumo insumo) throws BLException {
        if (insumo == null) {
            throw new BLException("El insumo a actualizar debe tener un ID válido.");
        }
        // Regla de negocio: Verificar la existencia antes de actualizar
        validarExiste(insumo.getId());
        validarCamposInsumo(insumo);
        try {
            insumoDao.update(insumo);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo actualizar el insumo en el inventario", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException {
        if (id == null || id <= 0) {
            throw new BLException("El ID del insumo a eliminar no es válido.");
        }
        // Regla de negocio: Verificar la existencia antes de eliminar
        validarExiste(id);
        try {
            insumoDao.delete(id);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo eliminar el insumo del inventario", variableException);
        }
    }

    private void validarExiste(int id) throws BLException {
        try {
            if (!insumoDao.existsById(id)) {
                throw new BLException("No se encontró la existencia del insumo especificado.");
            }
        } catch (SQLException variableSQL) {
            throw new BLException("Error al verificar la existencia del insumo", variableSQL);
        }
    }

    private void validarCamposInsumo(Insumo insumo) throws BLException {
        if (insumo == null) {
            throw new BLException("El objeto insumo no puede ser nulo.");
        }
        if (insumo.getNombre() == null || insumo.getNombre().trim().isEmpty()) {
            throw new BLException("El nombre del insumo es obligatorio.");
        }
        if (insumo.getUnidadMedida() == null || insumo.getUnidadMedida().trim().isEmpty()) {
            throw new BLException("La unidad de medida es obligatoria.");
        }
        if (insumo.getStockActual() < 0) {
            throw new BLException("El stock actual no puede ser negativo.");
        }
        if (insumo.getStockMinimo() < 0) {
            throw new BLException("El stock mínimo no puede ser negativo.");
        }
    }
}
