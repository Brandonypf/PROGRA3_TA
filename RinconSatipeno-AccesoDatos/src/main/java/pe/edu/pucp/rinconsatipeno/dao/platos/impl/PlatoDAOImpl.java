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

import pe.edu.pucp.rinconsatipeno.modelo.platos.Producto;
import pe.edu.pucp.rinconsatipeno.modelo.platos.Receta;
import pe.edu.pucp.rinconsatipeno.dao.platos.PlatoDAO;
import pe.edu.pucp.rinconsatipeno.dao.platos.RecetaDAO;

public class PlatoDAOImpl implements PlatoDAO {

    // Aún no existen: saldrán en rojo hasta que se cree el DAO de Receta
    private final RecetaDAO recetaDao = new RecetaDAOImpl();

    @Override
    public void insert(Producto producto)throws SQLException {
        String sql = "INSERT INTO plato (nombre, precio, categoria, descripcion, id_receta) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            cs.setString(1, producto.getNombre());
            cs.setDouble(2, producto.getPrecio());
            cs.setString(3, producto.getCategoria());
            cs.setString(4, producto.getDescripcion());
            Receta aux= producto.getReceta();
            if (aux == null) cs.setNull(5, Types.INTEGER);
            else cs.setInt(5, aux.getId());
            cs.executeUpdate();
            int id;
            try (ResultSet claves = cs.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                id = claves.getInt(1);
            }
            producto.setIdPlato(id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar plato: " + ex.getMessage(), ex);
        }
    }
    @Override
    public void update(Producto producto)throws SQLException {
        String sql = "UPDATE plato SET nombre = ?, precio = ?, categoria = ?, descripcion = ?, id_receta = ? WHERE id_plato = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setString(1, producto.getNombre());
            cs.setDouble(2, producto.getPrecio());
            cs.setString(3, producto.getCategoria());
            cs.setString(4, producto.getDescripcion());
            Receta aux= producto.getReceta();
            if (aux == null) cs.setNull(5, Types.INTEGER);
            else cs.setInt(5, aux.getId());
            cs.setInt(6, producto.getIdPlato());
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al modificar plato: " + ex.getMessage(), ex);
        }
    }
    @Override
    public void delete(Integer idPlato) throws SQLException {
        String sql = "DELETE FROM plato WHERE id_plato = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idPlato);
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar plato: " + ex.getMessage(), ex);
        }
    }
    @Override
    public Producto findBy(Integer idPlato)throws SQLException {
        String sql = "SELECT * FROM plato WHERE id_plato = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idPlato);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar plato: " + ex.getMessage(), ex);
        }
    }
    @Override
    public ArrayList<Producto> findAll()throws SQLException {
        String sql = "SELECT * FROM plato";
        ArrayList<Producto> productos = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                productos.add(mapear(rs));
            }
            return productos;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar platos: " + ex.getMessage(), ex);
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto aux = new Producto();
        aux.setIdPlato(rs.getInt("id_plato"));
        aux.setNombre(rs.getString("nombre"));
        aux.setPrecio(rs.getDouble("precio"));
        aux.setCategoria((rs.getString("categoria")));
        aux.setDescripcion(rs.getString("descripcion"));
        Receta receta = recetaDao.findBy(rs.getInt("id_receta"));
        aux.setReceta(receta);
        return aux;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM plato WHERE id_plato = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
