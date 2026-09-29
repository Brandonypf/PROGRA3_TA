package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.impl;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.RinconSatipeno.Modelo.platos.RecetaInsumo;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.BLException;
import pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio.RecetaInsumoLN;

import pe.edu.pucp.rinconSatipeno.dao.RecetaInsumoDao;
import pe.edu.pucp.rinconSatipeno.dao.impl.RecetaInsumoDaoImplement;

public class RecetaInsumoImpl
        implements RecetaInsumoLN {

    private final RecetaInsumoDao recetaInsumoDao =
            new RecetaInsumoDaoImplement();

    @Override
    public List<RecetaInsumo> findAll()
            throws BLException {

        try {
            return recetaInsumoDao.findAll();

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudieron listar los insumos de las recetas",
                    ex
            );
        }
    }

    @Override
    public RecetaInsumo findBy(
            Integer idReceta,
            Integer idInsumo)
            throws BLException {

        validarIds(idReceta, idInsumo);

        try {
            RecetaInsumo recetaInsumo =
                    recetaInsumoDao.findBy(
                            idReceta,
                            idInsumo
                    );

            if (recetaInsumo == null) {
                throw new BLException(
                        "No se encontró el insumo dentro de la receta"
                );
            }

            return recetaInsumo;

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo buscar el insumo de la receta",
                    ex
            );
        }
    }

    @Override
    public List<RecetaInsumo> findByReceta(
            Integer idReceta)
            throws BLException {

        if (idReceta == null || idReceta <= 0) {
            throw new BLException(
                    "El ID de la receta no es válido"
            );
        }

        try {
            return recetaInsumoDao.findByReceta(
                    idReceta
            );

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudieron listar los insumos de la receta",
                    ex
            );
        }
    }

    @Override
    public void insert(
            RecetaInsumo recetaInsumo)
            throws BLException {

        validarRecetaInsumo(recetaInsumo);

        int idReceta =
                recetaInsumo.getReceta().getId();

        int idInsumo =
                recetaInsumo.getInsumo().getId();

        try {

            RecetaInsumo existente =
                    recetaInsumoDao.findBy(
                            idReceta,
                            idInsumo
                    );

            if (existente != null) {
                throw new BLException(
                        "El insumo ya pertenece a esta receta"
                );
            }

            recetaInsumoDao.insert(
                    recetaInsumo
            );

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo agregar el insumo a la receta",
                    ex
            );
        }
    }

    @Override
    public void update(
            RecetaInsumo recetaInsumo)
            throws BLException {

        validarRecetaInsumo(recetaInsumo);

        int idReceta =
                recetaInsumo.getReceta().getId();

        int idInsumo =
                recetaInsumo.getInsumo().getId();

        validarExiste(
                idReceta,
                idInsumo
        );

        try {
            recetaInsumoDao.update(
                    recetaInsumo
            );

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo actualizar la cantidad requerida del insumo",
                    ex
            );
        }
    }

    @Override
    public void delete(
            Integer idReceta,
            Integer idInsumo)
            throws BLException {

        validarIds(
                idReceta,
                idInsumo
        );

        validarExiste(
                idReceta,
                idInsumo
        );

        try {
            recetaInsumoDao.delete(
                    idReceta,
                    idInsumo
            );

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo eliminar el insumo de la receta",
                    ex
            );
        }
    }

    private void validarExiste(
            Integer idReceta,
            Integer idInsumo)
            throws BLException {

        try {
            RecetaInsumo recetaInsumo =
                    recetaInsumoDao.findBy(
                            idReceta,
                            idInsumo
                    );

            if (recetaInsumo == null) {
                throw new BLException(
                        "El insumo no pertenece a la receta"
                );
            }

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo verificar la relación entre receta e insumo",
                    ex
            );
        }
    }

    private void validarIds(
            Integer idReceta,
            Integer idInsumo)
            throws BLException {

        if (idReceta == null ||
                idReceta <= 0) {

            throw new BLException(
                    "El ID de la receta no es válido"
            );
        }

        if (idInsumo == null ||
                idInsumo <= 0) {

            throw new BLException(
                    "El ID del insumo no es válido"
            );
        }
    }

    private void validarRecetaInsumo(
            RecetaInsumo recetaInsumo)
            throws BLException {

        if (recetaInsumo == null) {
            throw new BLException(
                    "RecetaInsumo no puede ser nulo"
            );
        }

        if (recetaInsumo.getReceta() == null) {
            throw new BLException(
                    "Debe indicarse una receta"
            );
        }

        if (recetaInsumo.getReceta().getId() <= 0) {
            throw new BLException(
                    "La receta debe tener un ID válido"
            );
        }

        if (recetaInsumo.getInsumo() == null) {
            throw new BLException(
                    "Debe indicarse un insumo"
            );
        }

        if (recetaInsumo.getInsumo().getId() <= 0) {
            throw new BLException(
                    "El insumo debe tener un ID válido"
            );
        }

        if (recetaInsumo.getCantidadRequerida() <= 0) {
            throw new BLException(
                    "La cantidad requerida debe ser mayor a cero"
            );
        }
    }
}