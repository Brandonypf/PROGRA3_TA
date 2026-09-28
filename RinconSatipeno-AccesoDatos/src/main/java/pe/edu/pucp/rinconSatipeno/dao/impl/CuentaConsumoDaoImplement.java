package pe.edu.pucp.rinconSatipeno.dao.impl;

import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.EstadoCuenta;
import pe.edu.pucp.rinconSatipeno.dao.CuentaConsumoDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

public class CuentaConsumoDaoImplement implements CuentaConsumoDao{
    @Override
    public List<CuentaConsumo> findAll() throws SQLException{
        //Invoca un procedimiento almacenado
        String sql ="{call nombreFuncionSQL}";
        //Solicita y se activa con la base de datos.
        try(Connection conec = DBManager.getInstance().getConnection();
            //Prepara la sentencia SQL del tipo CallableStatement a travez de la conexión obtenida.
            CallableStatement cmd = conec.prepareCall(sql);
            //executeQuery ejecuta la sentenciaSQL y se guarda en rs.
            ResultSet rs = cmd.executeQuery()){

            List<CuentaConsumo> cuentaConsumos = new ArrayList<>();
            while(rs.next()){
                cuentaConsumos.add(mapear(rs, new CuentaConsumo()));
            }

            return cuentaConsumos;
        }
    }

    protected CuentaConsumo mapear(ResultSet rs,CuentaConsumo cuentaConsumo) throws SQLException{

        cuentaConsumo.setIdCuenta(rs.getInt("id_cuenta_consumo"));
        cuentaConsumo.setEstado(EstadoCuenta.valueOf(rs.getString("estado")));
        cuentaConsumo.setFechaApertura(rs.getDate("fecha_apertura").toLocalDate());
        cuentaConsumo.setFechaCierre(rs.getDate("fecha_cierre").toLocalDate());
        cuentaConsumo.setMontoTotalPagar(rs.getDouble("monto_total_pagar"));
        return cuentaConsumo;
    }

    public CuentaConsumo findBy(Integer id) throws SQLException{
        if(id==null){
            throw new IllegalArgumentException("El id no puede ser nula");
        }
        String sql = "{call nombreFuncion}";
        try(Connection conn = DBManager.getInstance().getConnection();
            CallableStatement cmd = conn.prepareCall(sql)){

            cmd.setInt("NombreDeLaID", id);

            try(ResultSet rs = cmd.executeQuery()){
                if(rs.next()){
                    return mapear(rs, new CuentaConsumo());
                }else{
                    return null;
                }

            }

        }
    }

    public void insert(CuentaConsumo modelo) throws SQLException{
        String sql = {"call nombreFuncionConParametros"};

        try(Connection connec = DBManager.getInstance().getConnection();
            CallableStatement cmd = connec.prepareCall(sql)){

            cmd.setInt("NombreParametroINT", modelo.getIdCuenta());
            //El .name() nos será útil para manejar la data enum como Strings
            cmd.setString("NombreParametroString", modelo.getEstado().name());
            cmd.setDate("NombreParametroBool",Date.valueOf(modelo.getFechaApertura()));
            cmd.setDate("NombreParametroENUM", Date.valueOf(modelo.getFechaCierre()));
            cmd.setDouble("NombreParametroINT", modelo.getMontoTotalPagar());

            //Significa que hubo algún fallo al momento de intentar realizar el update. Debemos cancelar la operación.
            if(cmd.executeUpdate()==0){
                throw new SQLException("No se pudo insertar la cuenta de consumo");
            }
            modelo.setIdCuenta(cmd.getInt("NombreParametroID"));
        }
    }
    public void update(Integer id) throws SQLException{
        String sql = {"call NombreSQL"};

        try(Connection conn = DBManager.getInstance().getConnection();
            CallableStatement cmd = conn.prepareCall(sql)){

            cmd.setInt("NombreTablaSQL", id);

            if(cmd.executeUpdate()==0){
                throw new SQLException("No se pudo modificar la data de la cuenta de consumo");
            }
        }
    }

    public void delete(Integer id) throws SQLException{

        if(id==null){
            throw new IllegalArgumentException("La ID no puede ser nula");
        }

        String sql = {"call NombreFuncionSQL"};

        try(Connection conn = DBManager.getInstance().getConnection();
            CallableStatement cmd = conn.prepareCall(sql)){

            cmd.setInt("NombreParametroID", id);

            if(cmd.executeUpdate()==0){
                throw new SQLException("No se pudo eliminar la data de la cuenta de consumo");
            }

        }

    }
}

















