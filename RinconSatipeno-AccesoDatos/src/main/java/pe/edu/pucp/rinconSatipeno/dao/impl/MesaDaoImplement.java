package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

import pe.edu.pucp.RinconSatipeno.Modelo.mesas.EstadoMesa;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Zona;
import pe.edu.pucp.rinconSatipeno.dao.MesaDao;

public class MesaDaoImplement implements MesaDao {

    @Override
    public void insert(Mesa mesa) throws SQLException {
        String sql = "{CALL insertar_mesa(?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, mesa.getNumero());
            cs.setInt(2, mesa.getCapacidad());
            cs.setString(3, mesa.getZona().name());
            cs.setString(4, mesa.getEstado().name());
            cs.registerOutParameter(5, Types.INTEGER);

            cs.execute();
            mesa.setId(cs.getInt(5));
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar mesa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(Mesa mesa) throws SQLException {
        String sql = "{CALL modificar_mesa(?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, mesa.getNumero());
            cs.setInt(2, mesa.getCapacidad());
            cs.setString(3, mesa.getZona().name());
            cs.setString(4, mesa.getEstado().name());
            cs.setInt(5, mesa.getId());

            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al modificar mesa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Integer idMesa) throws SQLException {
        String sql = "{CALL eliminar_mesa(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idMesa);
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar mesa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Mesa findBy(Integer idMesa) throws SQLException {
        String sql = "{CALL buscar_mesa_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idMesa);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar mesa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ArrayList<Mesa> findAll() throws SQLException {
        String sql = "{CALL listar_mesas()}";
        ArrayList<Mesa> mesas = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                mesas.add(mapear(rs));
            }
            return mesas;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar mesas: " + ex.getMessage(), ex);
        }
    }

    private Mesa mapear(ResultSet rs) throws SQLException {
        Mesa aux = new Mesa();
        aux.setId(rs.getInt("id_mesa"));
        aux.setNumero(rs.getInt("numero"));
        aux.setCapacidad(rs.getInt("capacidad"));
        aux.setZona(Zona.valueOf(rs.getString("zona")));
        aux.setEstado(EstadoMesa.valueOf(rs.getString("estado")));
        return aux;
    }
}
