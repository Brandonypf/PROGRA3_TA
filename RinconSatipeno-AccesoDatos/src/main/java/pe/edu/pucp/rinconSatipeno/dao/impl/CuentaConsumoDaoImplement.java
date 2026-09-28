package pe.edu.pucp.rinconSatipeno.dao.impl;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.RinconSatipeno.Modelo.cuentas.EstadoCuenta;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Mozo;
import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.Reserva;
import pe.edu.pucp.rinconSatipeno.dao.CuentaConsumoDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaConsumoDaoImplement implements CuentaConsumoDao {

    @Override
    public List<CuentaConsumo> findAll() throws SQLException {
        String sql = "{call listar_cuentas_consumo()}";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                CallableStatement cmd = conn.prepareCall(sql);
                ResultSet rs = cmd.executeQuery()) {

            List<CuentaConsumo> cuentas = new ArrayList<>();
            while (rs.next()) {
                cuentas.add(mapear(rs, new CuentaConsumo()));
            }
            return cuentas;
        }
    }

    @Override
    public CuentaConsumo findBy(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }

        String sql = "{call buscar_cuenta_consumo_por_id(?)}";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                CallableStatement cmd = conn.prepareCall(sql)) {

            cmd.setInt("p_id_cuenta", id);

            try (ResultSet rs = cmd.executeQuery()) {
                return rs.next() ? mapear(rs, new CuentaConsumo()) : null;
            }
        }
    }

    @Override
    public void insert(CuentaConsumo cuentaConsumo) throws SQLException {
        if (cuentaConsumo == null) {
            throw new IllegalArgumentException("La cuenta de consumo no puede ser nula");
        }

        String sql = "{call insertar_cuenta_consumo(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                CallableStatement cmd = conn.prepareCall(sql)) {

            cmd.setTimestamp("p_fecha_apertura", cuentaConsumo.getFechaApertura() != null ? Timestamp.valueOf(cuentaConsumo.getFechaApertura()) : null);
            cmd.setTimestamp("p_fecha_cierre", cuentaConsumo.getFechaCierre() != null ? Timestamp.valueOf(cuentaConsumo.getFechaCierre()) : null);
            cmd.setTimestamp("p_fecha_pago", cuentaConsumo.getFechaPago() != null ? Timestamp.valueOf(cuentaConsumo.getFechaPago()) : null);
            cmd.setString("p_estado", cuentaConsumo.getEstado() != null ? cuentaConsumo.getEstado().name() : null);
            cmd.setDouble("p_monto_total_pagar", cuentaConsumo.getMontoTotalPagar());
            cmd.setString("p_enlace_pago", cuentaConsumo.getEnlacePago());
            cmd.setInt("p_id_mesa", cuentaConsumo.getMesa().getId());
            cmd.setInt("p_id_mozo", cuentaConsumo.getMozo().getIdEmpleado());

            if (cuentaConsumo.getReserva() != null) {
                cmd.setInt("p_id_reserva", cuentaConsumo.getReserva().getIdReserva());
            } else {
                cmd.setNull("p_id_reserva", Types.INTEGER);
            }

            cmd.registerOutParameter("p_id_cuenta", Types.INTEGER);

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo insertar la cuenta de consumo");
            }

            cuentaConsumo.setIdCuenta(cmd.getInt("p_id_cuenta"));
        }
    }

    @Override
    public void update(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }

        String sql = "{call modificar_cuenta_consumo(?)}";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                CallableStatement cmd = conn.prepareCall(sql)) {

            cmd.setInt("p_id_cuenta", id);

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo actualizar la cuenta de consumo");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }

        String sql = "{call eliminar_cuenta_consumo(?)}";
        try (
                Connection conn = DBManager.getInstance().getConnection();
                CallableStatement cmd = conn.prepareCall(sql)) {

            cmd.setInt("p_id_cuenta", id);

            if (cmd.executeUpdate() == 0) {
                throw new SQLException("No se pudo eliminar la cuenta de consumo");
            }
        }
    }

    protected CuentaConsumo mapear(ResultSet rs, CuentaConsumo cuentaConsumo) throws SQLException {
        cuentaConsumo.setIdCuenta(rs.getInt("id_cuenta"));

        Timestamp tsApertura = rs.getTimestamp("fecha_apertura");
        if (tsApertura != null) {
            cuentaConsumo.setFechaApertura(tsApertura.toLocalDateTime());
        }

        Timestamp tsCierre = rs.getTimestamp("fecha_cierre");
        if (tsCierre != null) {
            cuentaConsumo.setFechaCierre(tsCierre.toLocalDateTime());
        }

        Timestamp tsPago = rs.getTimestamp("fecha_pago");
        if (tsPago != null) {
            cuentaConsumo.setFechaPago(tsPago.toLocalDateTime());
        }

        cuentaConsumo.setEstado(Enum.valueOf(EstadoCuenta.class, rs.getString("estado")));
        cuentaConsumo.setMontoTotalPagar(rs.getDouble("monto_total_pagar"));
        cuentaConsumo.setEnlacePago(rs.getString("enlace_pago"));

        Mesa mesa = new Mesa();
        mesa.setId(rs.getInt("id_mesa"));
        cuentaConsumo.setMesa(mesa);

        Mozo mozo = new Mozo();
        mozo.setIdEmpleado(rs.getInt("id_mozo"));
        cuentaConsumo.setMozo(mozo);

        int idReserva = rs.getInt("id_reserva");
        if (!rs.wasNull() && idReserva > 0) {
            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);
            cuentaConsumo.setReserva(reserva);
        }

        return cuentaConsumo;
    }
}