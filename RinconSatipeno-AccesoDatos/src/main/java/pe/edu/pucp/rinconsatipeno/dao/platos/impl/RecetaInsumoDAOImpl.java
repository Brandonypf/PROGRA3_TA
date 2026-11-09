package pe.edu.pucp.rinconsatipeno.dao.platos.impl;

import pe.edu.pucp.rinconsatipeno.dao.inventario.impl.InsumoDAOImpl;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;

import pe.edu.pucp.rinconsatipeno.modelo.inventario.Insumo;
import pe.edu.pucp.rinconsatipeno.modelo.platos.Receta;
import pe.edu.pucp.rinconsatipeno.modelo.platos.RecetaInsumo;

import pe.edu.pucp.rinconsatipeno.dao.inventario.InsumoDAO;
import pe.edu.pucp.rinconsatipeno.dao.platos.RecetaInsumoDAO;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;


public class RecetaInsumoDAOImpl implements RecetaInsumoDAO {

    private final InsumoDAO insumoDao = new InsumoDAOImpl();

    @Override
    public void insert(RecetaInsumo recetaInsumo) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "INSERT INTO receta_insumo (id_receta, id_insumo, cantidad_requerida) VALUES (?, ?, ?)";

        try (PreparedStatement cs = con.prepareStatement(sql)) {
            cs.setInt(1, recetaInsumo.getReceta().getId());
            cs.setInt(2, recetaInsumo.getInsumo().getId());
            cs.setDouble(3, recetaInsumo.getCantidadRequerida());

            cs.executeUpdate();
        }
    }

    @Override
    public void update(RecetaInsumo recetaInsumo) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "UPDATE receta_insumo SET cantidad_requerida = ? WHERE id_receta = ? AND id_insumo = ?";

        try (PreparedStatement cs = con.prepareStatement(sql)) {
            cs.setInt(2, recetaInsumo.getReceta().getId());
            cs.setInt(3, recetaInsumo.getInsumo().getId());
            cs.setDouble(1, recetaInsumo.getCantidadRequerida());

            cs.executeUpdate();
        }
    }

    @Override
    public void delete(Integer idReceta, Integer idInsumo) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "DELETE FROM receta_insumo WHERE id_receta = ? AND id_insumo = ?";

        try (PreparedStatement cs = con.prepareStatement(sql)) {
            cs.setInt(1, idReceta);
            cs.setInt(2, idInsumo);

            cs.executeUpdate();
        }
    }

    @Override
    public void insertLineas(int idReceta, List<RecetaInsumo> insumos) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "INSERT INTO receta_insumo (id_receta, id_insumo, cantidad_requerida) VALUES (?, ?, ?)";

        try (PreparedStatement cs = con.prepareStatement(sql)) {
            for (RecetaInsumo recetaInsumo : insumos) {
                cs.setInt(1, idReceta);   // se usa el id recibido: la receta puede ser nueva
                cs.setInt(2, recetaInsumo.getInsumo().getId());
                cs.setDouble(3, recetaInsumo.getCantidadRequerida());

                cs.executeUpdate();
            }
        }
    }

    @Override
    public void deleteLineas(int idReceta) throws SQLException {
        Connection con = TransactionsManager.getConnection();
        String sql = "DELETE FROM receta_insumo WHERE id_receta = ?";

        try (PreparedStatement cs = con.prepareStatement(sql)) {
            cs.setInt(1, idReceta);

            cs.executeUpdate();
        }
    }

    @Override
    public RecetaInsumo findBy(Integer idReceta, Integer idInsumo) throws SQLException {
        String sql = "SELECT ri.id_receta, ri.id_insumo, ri.cantidad_requerida, r.descripcion AS descripcion_receta FROM receta_insumo ri JOIN receta r ON r.id_receta = ri.id_receta WHERE ri.id_receta = ? AND ri.id_insumo = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idReceta);
            cs.setInt(2, idInsumo);

            try (ResultSet rs = cs.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public ArrayList<RecetaInsumo> findAll() throws SQLException {
        String sql = "SELECT ri.id_receta, ri.id_insumo, ri.cantidad_requerida, r.descripcion AS descripcion_receta FROM receta_insumo ri JOIN receta r ON r.id_receta = ri.id_receta ORDER BY ri.id_receta, ri.id_insumo";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql);
             ResultSet rs = cs.executeQuery()) {

            ArrayList<RecetaInsumo> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        }
    }

    @Override
    public ArrayList<RecetaInsumo> findByReceta(Integer idReceta) throws SQLException {
        String sql = "SELECT ri.id_receta, ri.id_insumo, ri.cantidad_requerida, r.descripcion AS descripcion_receta FROM receta_insumo ri JOIN receta r ON r.id_receta = ri.id_receta WHERE ri.id_receta = ? ORDER BY ri.id_insumo";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idReceta);

            try (ResultSet rs = cs.executeQuery()) {
                ArrayList<RecetaInsumo> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        }
    }

    @Override
    public boolean existe(Integer idReceta, Integer idInsumo) throws SQLException {
        String sql = "SELECT 1 FROM receta_insumo WHERE id_receta = ? AND id_insumo = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReceta);
            ps.setInt(2, idInsumo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private RecetaInsumo mapear(ResultSet rs) throws SQLException {
        RecetaInsumo recetaInsumo = new RecetaInsumo();
        Receta receta = new Receta();
        receta.setId(rs.getInt("id_receta"));
        receta.setDescripcion(rs.getString("descripcion_receta"));

        Insumo insumo = insumoDao.findBy(rs.getInt("id_insumo"));

        recetaInsumo.setReceta(receta);
        recetaInsumo.setInsumo(insumo);
        recetaInsumo.setCantidadRequerida(rs.getDouble("cantidad_requerida"));

        return recetaInsumo;
    }
}