package pe.edu.pucp.rinconsatipeno.dao.cuentas.impl;

import pe.edu.pucp.rinconsatipeno.dao.mesas.impl.MesaDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.reservas.impl.ReservaDAOImpl;
import pe.edu.pucp.rinconsatipeno.dao.empleados.impl.MozoDAOImpl;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;

import pe.edu.pucp.rinconsatipeno.modelo.cuentas.CuentaConsumo;
import pe.edu.pucp.rinconsatipeno.modelo.cuentas.EstadoCuenta;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.Mozo;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.Reserva;
import pe.edu.pucp.rinconsatipeno.dao.cuentas.CuentaConsumoDAO;
import pe.edu.pucp.rinconsatipeno.dao.mesas.MesaDAO;
import pe.edu.pucp.rinconsatipeno.dao.empleados.MozoDAO;
import pe.edu.pucp.rinconsatipeno.dao.reservas.ReservaDAO;

public class CuentaConsumoDAOImpl implements CuentaConsumoDAO {

    // Aún no existen: saldrán en rojo hasta que se creen los DAO de Mesa, Mozo y Reserva
    private final MesaDAO mesaDao = new MesaDAOImpl();
    private final MozoDAO mozoDao = new MozoDAOImpl();
    private final ReservaDAO reservaDao = new ReservaDAOImpl();

    @Override
    public void insert(CuentaConsumo cuenta) throws SQLException {
        String sql = "INSERT INTO cuenta_consumo (fecha_apertura, fecha_cierre, fecha_pago, estado, monto_total_pagar, enlace_pago, id_mesa, id_mozo, id_reserva) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParametros(cs, cuenta);

            cs.executeUpdate();
            int idGeneradoKey;
            try (ResultSet claves = cs.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                idGeneradoKey = claves.getInt(1);
            }
            int id = idGeneradoKey;
            cuenta.setIdCuenta(id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar cuenta de consumo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(CuentaConsumo cuenta) throws SQLException {
        String sql = "UPDATE cuenta_consumo SET fecha_apertura = ?, fecha_cierre = ?, fecha_pago = ?, estado = ?, monto_total_pagar = ?, enlace_pago = ?, id_mesa = ?, id_mozo = ?, id_reserva = ? WHERE id_cuenta = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            setParametros(cs, cuenta);
            cs.setInt(10, cuenta.getIdCuenta());

            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al modificar cuenta de consumo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Integer idCuenta) throws SQLException {
        String sql = "DELETE FROM cuenta_consumo WHERE id_cuenta = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

            cs.setInt(1, idCuenta);
            cs.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar cuenta de consumo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public CuentaConsumo findBy(Integer idCuenta) throws SQLException {
        String sql = "SELECT * FROM cuenta_consumo WHERE id_cuenta = ?";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql)) {

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
        String sql = "SELECT * FROM cuenta_consumo";
        ArrayList<CuentaConsumo> cuentas = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement cs = con.prepareStatement(sql);
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
    private void setParametros(PreparedStatement cs, CuentaConsumo cuenta) throws SQLException {
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

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM cuenta_consumo WHERE id_cuenta = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}