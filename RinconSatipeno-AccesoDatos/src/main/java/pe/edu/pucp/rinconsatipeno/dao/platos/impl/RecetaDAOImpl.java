package pe.edu.pucp.rinconsatipeno.dao.platos.impl;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;

import pe.edu.pucp.rinconsatipeno.modelo.platos.Receta;
import pe.edu.pucp.rinconsatipeno.dao.platos.RecetaDAO;
import pe.edu.pucp.rinconsatipeno.dao.platos.RecetaInsumoDAO;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;

public class RecetaDAOImpl implements RecetaDAO {

    private final RecetaInsumoDAO insumosDao = new RecetaInsumoDAOImpl();

    @Override
    public void insert(Receta receta) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "INSERT INTO receta (descripcion) VALUES (?)";

        try (PreparedStatement cs = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            cs.setString(1, receta.getDescripcion());

            cs.executeUpdate();
            int idGeneradoKey;
            try (ResultSet claves = cs.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                idGeneradoKey = claves.getInt(1);
            }
            receta.setId(idGeneradoKey);

            insumosDao.insertLineas(receta.getId(), receta.getRecetasInsumos());
        }
    }

    @Override
    public void update(Receta receta) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "UPDATE receta SET descripcion = ? WHERE id_receta = ?";

        try (PreparedStatement cs = con.prepareStatement(sql)) {
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
        String sql = "DELETE FROM receta WHERE id_receta = ?";

        insumosDao.deleteLineas(idReceta);

        try (PreparedStatement cs = con.prepareStatement(sql)) {
            cs.setInt(1, idReceta);
            cs.executeUpdate();
        }
    }

    @Override
    public Receta findBy(Integer idReceta) throws SQLException {
        String sql = "SELECT * FROM receta WHERE id_receta = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idReceta);
            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public ArrayList<Receta> findAll() throws SQLException {
        String sql = "SELECT * FROM receta";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql);
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

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM receta WHERE id_receta = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}