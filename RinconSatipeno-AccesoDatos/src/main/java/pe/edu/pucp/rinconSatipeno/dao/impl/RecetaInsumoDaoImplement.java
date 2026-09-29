package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

import pe.edu.pucp.RinconSatipeno.Modelo.inventario.Insumo;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Receta;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.RecetaInsumo;

import pe.edu.pucp.rinconSatipeno.dao.InsumoDao;
import pe.edu.pucp.rinconSatipeno.dao.RecetaDao;
import pe.edu.pucp.rinconSatipeno.dao.RecetaInsumoDao;

public class RecetaInsumoDaoImplement
        implements RecetaInsumoDao {

    private final RecetaDao recetaDao =
            new RecetaDaoImplement();

    private final InsumoDao insumoDao =
            new InsumoDaoImplement();

    @Override
    public void insert(
            RecetaInsumo recetaInsumo
    ) throws SQLException {

        String sql =
                "{CALL insertar_receta_insumo(?, ?, ?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            Receta receta =
                    recetaInsumo.getReceta();

            Insumo insumo =
                    recetaInsumo.getInsumo();

            cs.setInt(
                    1,
                    receta.getId()
            );

            cs.setInt(
                    2,
                    insumo.getId()
            );

            cs.setDouble(
                    3,
                    recetaInsumo.getCantidadRequerida()
            );

            cs.executeUpdate();
        }
    }

    @Override
    public void update(
            RecetaInsumo recetaInsumo
    ) throws SQLException {

        String sql =
                "{CALL modificar_receta_insumo(?, ?, ?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            Receta receta =
                    recetaInsumo.getReceta();

            Insumo insumo =
                    recetaInsumo.getInsumo();

            cs.setInt(
                    1,
                    receta.getId()
            );

            cs.setInt(
                    2,
                    insumo.getId()
            );

            cs.setDouble(
                    3,
                    recetaInsumo.getCantidadRequerida()
            );

            cs.executeUpdate();
        }
    }

    @Override
    public void delete(
            Integer idReceta,
            Integer idInsumo
    ) throws SQLException {

        String sql =
                "{CALL eliminar_receta_insumo(?, ?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            cs.setInt(1, idReceta);
            cs.setInt(2, idInsumo);

            cs.executeUpdate();
        }
    }

    @Override
    public RecetaInsumo findBy(
            Integer idReceta,
            Integer idInsumo
    ) throws SQLException {

        String sql =
                "{CALL buscar_receta_insumo_por_ids(?, ?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            cs.setInt(1, idReceta);
            cs.setInt(2, idInsumo);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }

        return null;
    }

    @Override
    public ArrayList<RecetaInsumo> findAll()
            throws SQLException {

        String sql =
                "{CALL listar_recetas_insumos()}";

        ArrayList<RecetaInsumo> lista =
                new ArrayList<>();

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql);

                ResultSet rs =
                        cs.executeQuery()
        ) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    @Override
    public ArrayList<RecetaInsumo> findByReceta(
            Integer idReceta
    ) throws SQLException {

        String sql =
                "{CALL listar_insumos_por_receta(?)}";

        ArrayList<RecetaInsumo> lista =
                new ArrayList<>();

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            cs.setInt(1, idReceta);

            try (ResultSet rs = cs.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }

        return lista;
    }

    private RecetaInsumo mapear(ResultSet rs)
            throws SQLException {

        RecetaInsumo recetaInsumo =
                new RecetaInsumo();

        Receta receta =
                recetaDao.findBy(
                        rs.getInt("id_receta")
                );

        Insumo insumo =
                insumoDao.findBy(
                        rs.getInt("id_insumo")
                );

        recetaInsumo.setReceta(receta);

        recetaInsumo.setInsumo(insumo);

        recetaInsumo.setCantidadRequerida(
                rs.getDouble("cantidad_requerida")
        );

        return recetaInsumo;
    }
}