package pe.edu.pucp.rinconSatipeno.dao.impl;
//public class AreaDAOImpl extends RegistroDAOImpl<Area> implements AreaDAO

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.inventario.Insumo;
import pe.edu.pucp.rinconSatipeno.dao.InsumoDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsumoDaoImplement implements InsumoDao {

    @Override
    public List<Insumo> findAll() throws SQLException {
        String sql = "{call listar_insumos()}";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                CallableStatement cmd = conn.prepareCall(sql);
                ResultSet rs=cmd.executeQuery()){
               List<Insumo> insumos = new ArrayList<>();
               while (rs.next()){
                   insumos.add(mapear(rs, new Insumo()));
               }
               return insumos;
        }catch (SQLException ex) {
            throw new RuntimeException("Error al listar platos: " + ex.getMessage(), ex);
        }
    }


    @Override
    public Insumo findBy(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        String sql = "{call buscar_insumo_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setInt(1, id);
            try (ResultSet rs = cmd.executeQuery()) {
                return rs.next() ? mapear(rs, new Insumo()) : null;
            }
        }
    }

    @Override
    public void insert(Insumo insumo) throws SQLException {
        if (insumo == null) {
            throw new IllegalArgumentException("El insumo no puede ser nulo");
        }
        // procedimiento: (p_nombre, p_unidad_medida, p_stock_actual, p_stock_minimo, OUT p_id_insumo)
        String sql = "{call insertar_insumo(?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setString(1, insumo.getNombre());
            cmd.setString(2, insumo.getUnidadMedida());
            cmd.setDouble(3, insumo.getStockActual());
            cmd.setDouble(4, insumo.getStockMinimo());

            // Registro del parámetro OUT (es el quinto ?)
            cmd.registerOutParameter(5, Types.INTEGER);

            cmd.execute();

            int idGenerado = cmd.getInt(5);
            if (idGenerado <= 0) {
                throw new SQLException("No se pudo obtener un ID válido para el insumo insertado.");
            }
            insumo.setId(idGenerado);
        }
    }

    @Override
    public void update(Insumo insumo) throws SQLException {
        if (insumo == null) {
            throw new IllegalArgumentException("El insumo no puede ser nulo");
        }
        // procedimiento: (p_id_insumo, p_nombre, p_unidad_medida, p_stock_actual, p_stock_minimo)
        String sql = "{call modificar_insumo(?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setInt(1, insumo.getId());
            cmd.setString(2, insumo.getNombre());
            cmd.setString(3, insumo.getUnidadMedida());
            cmd.setDouble(4, insumo.getStockActual());
            cmd.setDouble(5, insumo.getStockMinimo());

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo modificar el insumo");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        String sql = "{call eliminar_insumo(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cmd = con.prepareCall(sql)) {

            cmd.setInt(1, id);

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo eliminar el insumo");
            }
        }
    }

    private Insumo mapear(ResultSet rs, Insumo insumo) throws SQLException {
        insumo.setId(rs.getInt("id_insumo"));
        insumo.setNombre(rs.getString("nombre"));
        insumo.setUnidadMedida(rs.getString("unidad_medida"));
        insumo.setStockActual(rs.getDouble("stock_actual"));
        insumo.setStockMinimo(rs.getDouble("stock_minimo"));
        return insumo;
    }

}
