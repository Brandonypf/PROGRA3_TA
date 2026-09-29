package pe.edu.pucp.RinconSatipeno.Negocio.Logica_Negocio;

import java.util.List;

import pe.edu.pucp.RinconSatipeno.Modelo.platos.RecetaInsumo;

public interface RecetaInsumoLN {

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