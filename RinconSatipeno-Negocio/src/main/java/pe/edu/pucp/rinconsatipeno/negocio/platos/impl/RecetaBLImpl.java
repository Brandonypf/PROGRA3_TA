package pe.edu.pucp.rinconsatipeno.negocio.platos.impl;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pe.edu.pucp.rinconsatipeno.modelo.inventario.Insumo;
import pe.edu.pucp.rinconsatipeno.modelo.platos.Receta;
import pe.edu.pucp.rinconsatipeno.modelo.platos.RecetaInsumo;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.platos.RecetaBL;
import pe.edu.pucp.rinconsatipeno.dao.inventario.InsumoDAO;               // ajustar si el nombre difiere
import pe.edu.pucp.rinconsatipeno.dao.platos.RecetaDAO;
import pe.edu.pucp.rinconsatipeno.dao.inventario.impl.InsumoDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.platos.impl.RecetaDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;

public class RecetaBLImpl implements RecetaBL {

    //Nos aseguramos que la clase solo sea asignada una vez (durante su creación)
    private final RecetaDAO recetaDAO = new RecetaDAOImpl();
    private final InsumoDAO insumoDAO = new InsumoDAOImpl();

    @Override
    public List<Receta> findAll() throws BLException {
        try {
            return recetaDAO.findAll();
        } catch (SQLException variableException) {
            throw new BLException("No se pudo listar todas las recetas", variableException);
        }
    }

    @Override
    public Receta findBy(Integer id) throws BLException {
        try {
            return recetaDAO.findBy(id);
        } catch (SQLException variableException) {
            throw new BLException("No se pudo encontrar la receta", variableException);
        }
    }

    @Override
    public void insert(Receta receta) throws BLException {
        validar(receta);

        TransactionsManager.iniciar();
        try {
            recetaDAO.insert(receta);
            TransactionsManager.commit();
        } catch (SQLException variableException) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo insertar la receta", variableException);
        }
    }

    @Override
    public void update(Receta aux) throws BLException {
        validarExiste(aux.getId());
        validar(aux);

        TransactionsManager.iniciar();
        try {
            recetaDAO.update(aux);
            TransactionsManager.commit();
        } catch (SQLException variableException) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo actualizar la receta", variableException);
        }
    }

    @Override
    public void delete(Integer id) throws BLException {
        validarExiste(id);

        TransactionsManager.iniciar();
        try {
            recetaDAO.delete(id);
            TransactionsManager.commit();
        } catch (SQLException variableException) {
            TransactionsManager.rollback();
            throw new BLException("No se pudo eliminar la receta", variableException);
        }
    }

    // Devuelve la receta si existe; si no, lanza BLException
    public Receta validarExiste(int id) throws BLException {
        Receta receta = findBy(id);
        if (receta == null) {
            throw new BLException("No se encontró la existencia de la receta");
        }
        return receta;
    }

    private void validar(Receta receta) throws BLException {
        if (receta.getRecetasInsumos().isEmpty()) {
            throw new BLException("La receta debe tener al menos un insumo");
        }

        // La clave de receta_insumo es (id_receta, id_insumo): un insumo no puede repetirse
        Set<Integer> insumosVistos = new HashSet<>();
        for (RecetaInsumo ri : receta.getRecetasInsumos()) {
            if (ri.getInsumo() == null) {
                throw new BLException("Cada línea de la receta debe tener un insumo");
            }
            if (ri.getCantidadRequerida() <= 0) {
                throw new BLException("La cantidad requerida de cada insumo debe ser mayor a cero");
            }

            int idInsumo = ri.getInsumo().getId();
            if (!insumosVistos.add(idInsumo)) {
                throw new BLException("El insumo con id " + idInsumo + " está repetido en la receta");
            }
            buscarInsumo(idInsumo);
        }
    }

    private Insumo buscarInsumo(int idInsumo) throws BLException {
        try {
            Insumo insumo = insumoDAO.findBy(idInsumo);
            if (insumo == null) {
                throw new BLException("No existe un insumo con id " + idInsumo);
            }
            return insumo;
        } catch (SQLException variableException) {
            throw new BLException("No se pudo recuperar el insumo de la receta", variableException);
        }
    }
}