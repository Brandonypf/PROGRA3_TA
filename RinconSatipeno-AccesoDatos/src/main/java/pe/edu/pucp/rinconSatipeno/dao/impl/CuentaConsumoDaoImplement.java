package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.EstadoCuenta;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Mozo;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.Reserva;
import pe.edu.pucp.rinconSatipeno.dao.CuentaConsumoDao;
import pe.edu.pucp.rinconSatipeno.dao.MesaDao;
import pe.edu.pucp.rinconSatipeno.dao.MozoDao;
import pe.edu.pucp.rinconSatipeno.dao.ReservaDao;

public class CuentaConsumoDaoImplement implements CuentaConsumoDao {

    // Aún no existen: saldrán en rojo hasta que se creen los DAO de Mesa, Mozo y Reserva
    private final MesaDao mesaDao = new MesaDaoImplement();
    private final MozoDao mozoDao = new MozoDaoImplement();
    private final ReservaDao reservaDao = new ReservaDaoImplement();

    @Override
    public void insert(CuentaConsumo cuenta) throws SQLException {
        String sql = "{CALL insertar_cuenta_consumo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            setParametros(cs, cuenta);
            cs.registerOutParameter(10, Types.INTEGER);

            cs.execute();
            int id = cs.getInt(10);
            cuenta.setIdCuenta(id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar cuenta de consumo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(CuentaConsumo cuenta) throws SQLException {
        String sql = "{CALL modificar_cuenta_consumo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            setParametros(cs, cuenta);
            cs.setInt(10, cuenta.getIdCuenta());

            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al modificar cuenta de consumo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Integer idCuenta) throws SQLException {
        String sql = "{CALL eliminar_cuenta_consumo(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idCuenta);
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar cuenta de consumo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public CuentaConsumo findBy(Integer idCuenta) throws SQLException {
        String sql = "{CALL buscar_cuenta_consumo_por_id(?)}";
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idCuenta);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
            return null;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar cuenta de consumo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ArrayList<CuentaConsumo> findAll() throws SQLException {
        String sql = "{CALL listar_cuentas_consumo()}";
        ArrayList<CuentaConsumo> cuentas = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                cuentas.add(mapear(rs));
            }
            return cuentas;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar cuentas de consumo: " + ex.getMessage(), ex);
        }
    }

    /** Parámetros 1..9 (mismo orden en insert y update). Cierre, pago, enlace y reserva pueden ser NULL. */
    private void setParametros(CallableStatement cs, CuentaConsumo cuenta) throws SQLException {
        cs.setTimestamp(1, aTimestamp(cuenta.getFechaApertura()));
        cs.setTimestamp(2, aTimestamp(cuenta.getFechaCierre()));
        cs.setTimestamp(3, aTimestamp(cuenta.getFechaPago()));
        cs.setString(4, cuenta.getEstado().name());
        cs.setDouble(5, cuenta.getMontoTotalPagar());

        if (cuenta.getEnlacePago() == null) {
            cs.setNull(6, Types.CHAR);
        } else {
            cs.setString(6, cuenta.getEnlacePago());
        }

        Mesa mesa = cuenta.getMesa();
        cs.setInt(7, mesa.getId());
        Mozo mozo = cuenta.getMozo();
        cs.setInt(8, mozo.getIdEmpleado());

        Reserva reserva = cuenta.getReserva();
        if (reserva == null) {
            cs.setNull(9, Types.INTEGER);
        } else {
            cs.setInt(9, reserva.getIdReserva());
        }
    }

    private Timestamp aTimestamp(LocalDateTime fecha) {
        return fecha == null ? null : Timestamp.valueOf(fecha);
    }

    private LocalDateTime aLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }

    private CuentaConsumo mapear(ResultSet rs) throws SQLException {
        CuentaConsumo aux = new CuentaConsumo();
        aux.setIdCuenta(rs.getInt("id_cuenta"));
        aux.setFechaApertura(aLocalDateTime(rs.getTimestamp("fecha_apertura"))); // antes que fechaCierre
        aux.setFechaCierre(aLocalDateTime(rs.getTimestamp("fecha_cierre")));
        aux.setFechaPago(aLocalDateTime(rs.getTimestamp("fecha_pago")));
        aux.setEstado(EstadoCuenta.valueOf(rs.getString("estado")));
        aux.setMontoTotalPagar(rs.getDouble("monto_total_pagar"));
        aux.setEnlacePago(rs.getString("enlace_pago"));

        Mesa mesa = mesaDao.findBy(rs.getInt("id_mesa"));
        aux.setMesa(mesa);
        Mozo mozo = mozoDao.findBy(rs.getInt("id_mozo"));
        aux.setMozo(mozo);

        int idReserva = rs.getInt("id_reserva");
        if (!rs.wasNull()) {
            aux.setReserva(reservaDao.findBy(idReserva));
        }
        return aux;
    }
}