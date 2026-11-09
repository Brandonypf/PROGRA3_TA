package pe.edu.pucp.rinconsatipeno.dao.empleados.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.DatosAcceso;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.Mozo;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.Turno;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Zona;
import pe.edu.pucp.rinconsatipeno.dao.empleados.DatosAccesoDAO;
import pe.edu.pucp.rinconsatipeno.dao.empleados.MozoDAO;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;

public class MozoDAOImpl implements MozoDAO {

    private final DatosAccesoDAO datosAccesoDao = new DatosAccesoDAOImpl();

    private Connection obtenerConexion() throws SQLException {
        if (TransactionsManager.activa()) {
            return TransactionsManager.getConnection();
        }
        return DBManager.getInstance().getConnection();
    }

    private void cerrarConexionSiNoEsTransaccional(Connection con) {
        if (!TransactionsManager.activa() && con != null) {
            try { con.close(); } catch (SQLException ignored) { }
        }
    }

    @Override
    public void insert(Mozo mozo) throws SQLException {
        String sql = "INSERT INTO mozo (nombre, telefono, fecha_contratacion, turno, zona, id_cuenta_acceso) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, mozo.getNombre());
                ps.setString(2, mozo.getTelefono());
                ps.setDate(3, Date.valueOf(mozo.getFechaDeContratacion()));
                ps.setString(4, mozo.getTurno().name());
                ps.setString(5, mozo.getZona().name());
                ps.setInt(6, mozo.getDatosAcceso().getIdCuentaAcceso());

                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        mozo.setIdEmpleado(rs.getInt(1));
                    }
                }
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public void update(Mozo mozo) throws SQLException {
        String sql = "UPDATE mozo SET nombre = ?, telefono = ?, fecha_contratacion = ?, turno = ?, zona = ? WHERE id_mozo = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, mozo.getNombre());
                ps.setString(2, mozo.getTelefono());
                ps.setDate(3, Date.valueOf(mozo.getFechaDeContratacion()));
                ps.setString(4, mozo.getTurno().name());
                ps.setString(5, mozo.getZona().name());
                ps.setInt(6, mozo.getIdEmpleado());
                ps.executeUpdate();
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public void delete(Integer idMozo) throws SQLException {
        String sql = "DELETE FROM mozo WHERE id_mozo = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMozo);
                ps.executeUpdate();
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public Mozo findBy(Integer idMozo) throws SQLException {
        String sql = "SELECT id_mozo, nombre, telefono, fecha_contratacion, turno, zona, id_cuenta_acceso FROM mozo WHERE id_mozo = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMozo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapear(rs);
                    }
                }
            }
            return null;
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public List<Mozo> findAll() throws SQLException {
        String sql = "SELECT id_mozo, nombre, telefono, fecha_contratacion, turno, zona, id_cuenta_acceso FROM mozo";
        List<Mozo> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    private Mozo mapear(ResultSet rs) throws SQLException {
        Mozo mozo = new Mozo();
        mozo.setIdEmpleado(rs.getInt("id_mozo"));
        mozo.setNombre(rs.getString("nombre"));
        mozo.setTelefono(rs.getString("telefono"));
        Date fecha = rs.getDate("fecha_contratacion");
        if (fecha != null) {
            mozo.setFechaDeContratacion(fecha.toLocalDate());
        }
        mozo.setTurno(Turno.valueOf(rs.getString("turno")));
        mozo.setZona(Zona.valueOf(rs.getString("zona")));
        int idCuenta = rs.getInt("id_cuenta_acceso");
        DatosAcceso cuenta = datosAccesoDao.findBy(idCuenta);
        mozo.setDatosAcceso(cuenta);
        return mozo;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM mozo WHERE id_mozo = ? LIMIT 1";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }
}