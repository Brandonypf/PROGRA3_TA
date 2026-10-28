package pe.edu.pucp.rinconsatipeno.negocio.platos;

import pe.edu.pucp.rinconsatipeno.negocio.BLException;

import java.util.List;

import pe.edu.pucp.rinconsatipeno.modelo.platos.RecetaInsumo;

public interface RecetaInsumoBL {

    List<RecetaInsumo> findAll()
            throws BLException;

    RecetaInsumo findBy(
            Integer idReceta,
            Integer idInsumo
    ) throws BLException;

    List<RecetaInsumo> findByReceta(
            Integer idReceta
    ) throws BLException;

    void insert(
            RecetaInsumo recetaInsumo
    ) throws BLException;

    void update(
            RecetaInsumo recetaInsumo
    ) throws BLException;

    void delete(
            Integer idReceta,
            Integer idInsumo
    ) throws BLException;
}