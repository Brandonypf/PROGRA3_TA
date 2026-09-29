package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

import pe.edu.pucp.RinconSatipeno.Modelo.platos.Plato;
import pe.edu.pucp.RinconSatipeno.Modelo.platos.Receta;
import pe.edu.pucp.rinconSatipeno.dao.PlatoDao;

public class PlatoDaoImplement implements PlatoDao {

    // Aún no existen: saldrán en rojo hasta que se cree el DAO de Receta
    //private final RecetaDao recetaDAO = new RecetaDaoImplements();

    @Override
    public void insert(Plato plato)throws SQLException {
        String sql = "{CALL insertar_plato(?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, plato.getNombre());
            cs.setDouble(2, plato.getPrecio());
            cs.setString(3, plato.getCategoria());
            cs.setString(4, plato.getDescripcion());
            Receta aux=plato.getReceta();
            cs.setInt(5, aux.getId());
            cs.registerOutParameter(6, Types.INTEGER);

            cs.execute();
            int id = cs.getInt(6);
            plato.setIdPlato(id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar plato: " + ex.getMessage(), ex);
        }
    }
    @Override
    public void update(Plato plato)throws SQLException {
        String sql = "{CALL modificar_plato(?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, plato.getNombre());
            cs.setDouble(2, plato.getPrecio());
            cs.setString(3, plato.getCategoria());
            cs.setString(4, plato.getDescripcion());
            Receta aux=plato.getReceta();
            cs.setInt(5, aux.getId());
            cs.setInt(6, plato.getIdPlato());
        } catch (SQLException ex) {
            throw new RuntimeException("Error al modificar plato: " + ex.getMessage(), ex);
        }
    }
    @Override
    public void delete(Integer idPlato) throws SQLException {
        String sql = "{CALL eliminar_plato(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idPlato);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar plato: " + ex.getMessage(), ex);
        }
    }
    @Override
    public Plato findBy(Integer idPlato)throws SQLException {
        String sql = "{CALL buscar_plato_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

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
    public ArrayList<Plato> findAll()throws SQLException {
        String sql = "{CALL listar_platos()}";
        ArrayList<Plato> platos = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                platos.add(mapear(rs));
            }
            return platos;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar platos: " + ex.getMessage(), ex);
        }
    }

    private Plato mapear(ResultSet rs) throws SQLException {
        Plato aux = new Plato();
        aux.setIdPlato(rs.getInt("id_plato"));
        aux.setNombre(rs.getString("nombre"));
        aux.setPrecio(rs.getDouble("precio"));
        aux.setCategoria((rs.getString("categoria")));
        aux.setDescripcion(rs.getString("descripcion"));
        Receta receta = recetaDao.findBy(rs.getInt("id_receta"));
        aux.setReceta(receta);
        return aux;
    }
}
