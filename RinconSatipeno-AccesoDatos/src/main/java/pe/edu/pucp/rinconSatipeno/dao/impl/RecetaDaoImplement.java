package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

import pe.edu.pucp.RinconSatipeno.Modelo.platos.Receta;
import pe.edu.pucp.rinconSatipeno.dao.RecetaDao;
import pe.edu.pucp.rinconSatipeno.dao.RecetaInsumoDao;
import pe.edu.pucp.rinconSatipeno.dao.transacciones.TransactionsManager;

public class RecetaDaoImplement implements RecetaDao {

    private final RecetaInsumoDao insumosDao = new RecetaInsumoDaoImplement();

    @Override
    public void insert(Receta receta) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL insertar_receta(?, ?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, receta.getDescripcion());
            cs.registerOutParameter(2, Types.INTEGER);

            cs.execute();
            receta.setId(cs.getInt(2));

            insumosDao.insertLineas(receta.getId(), receta.getRecetasInsumos());
        }
    }

    @Override
    public void update(Receta receta) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL modificar_receta(?, ?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, receta.getDescripcion());
            cs.setInt(2, receta.getId());

            cs.executeUpdate();

            insumosDao.deleteLineas(receta.getId());
            insumosDao.insertLineas(receta.getId(), receta.getRecetasInsumos());
        }
    }

    @Override
    public void delete(Integer idReceta) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "{CALL eliminar_receta(?)}";

        insumosDao.deleteLineas(idReceta);

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, idReceta);
            cs.executeUpdate();
        }
    }

    @Override
    public Receta findBy(Integer idReceta) throws SQLException {
        String sql = "{CALL buscar_receta_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idReceta);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public ArrayList<Receta> findAll() throws SQLException {
        String sql = "{CALL listar_recetas()}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            ArrayList<Receta> recetas = new ArrayList<>();
            while (rs.next()) {
                recetas.add(mapear(rs));
            }
            return recetas;
        }
    }

    private Receta mapear(ResultSet rs) throws SQLException {
        Receta aux = new Receta();
        aux.setId(rs.getInt("id_receta"));
        aux.setDescripcion(rs.getString("descripcion"));
        aux.setRecetasInsumos(insumosDao.findByReceta(aux.getId()));
        return aux;
    }
}