package pe.edu.pucp.rinconsatipeno.dao.platos;

import java.sql.SQLException;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.modelo.platos.RecetaInsumo;

public interface RecetaInsumoDAO {
    boolean existe(Integer idReceta, Integer idInsumo) throws SQLException;

    List<RecetaInsumo> findAll()
            throws SQLException;

    RecetaInsumo findBy(
            Integer idReceta,
            Integer idInsumo
    ) throws SQLException;

    List<RecetaInsumo> findByReceta(
            Integer idReceta
    ) throws SQLException;

    void insert(
            RecetaInsumo recetaInsumo
    ) throws SQLException;

    void update(
            RecetaInsumo recetaInsumo
    ) throws SQLException;

    void delete(
            Integer idReceta,
            Integer idInsumo
    ) throws SQLException;


    void insertLineas(
            int idReceta,
            List<RecetaInsumo> insumos
    ) throws SQLException;

    void deleteLineas(
            int idReceta
    ) throws SQLException;
}