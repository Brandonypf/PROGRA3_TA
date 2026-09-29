package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Administrador;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.DatosCuentaEmpleado;
import pe.edu.pucp.rinconSatipeno.dao.AdministradorDao;
import pe.edu.pucp.rinconSatipeno.dao.DatosCuentaEmpleadoDao;
import pe.edu.pucp.rinconSatipeno.dao.transacciones.TransactionsManager;

public class AdministradorDaoImplement implements AdministradorDao {

    private final DatosCuentaEmpleadoDao datosCuentaEmpleadoDao = new DatosCuentaEmpleadoDaoImplement();

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
    public void insert(Administrador admin) throws SQLException {
        String sql = "INSERT INTO administrador (nombre, telefono, fecha_contratacion, id_cuenta_acceso) VALUES (?, ?, ?, ?)";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, admin.getNombre());
                ps.setString(2, admin.getTelefono());
                ps.setDate(3, Date.valueOf(admin.getFechaDeContratacion()));
                ps.setInt(4, admin.getDatosCuentaEmpleado().getIdCuentaAcceso());

                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        admin.setIdEmpleado(rs.getInt(1));
                    }
                }
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public void update(Administrador admin) throws SQLException {
        String sql = "UPDATE administrador SET nombre = ?, telefono = ?, fecha_contratacion = ? WHERE id_administrador = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, admin.getNombre());
                ps.setString(2, admin.getTelefono());
                ps.setDate(3, Date.valueOf(admin.getFechaDeContratacion()));
                ps.setInt(4, admin.getIdEmpleado());
                ps.executeUpdate();
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public void delete(Integer idAdministrador) throws SQLException {
        String sql = "DELETE FROM administrador WHERE id_administrador = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAdministrador);
                ps.executeUpdate();
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public Administrador findBy(Integer idAdministrador) throws SQLException {
        String sql = "SELECT id_administrador, nombre, telefono, fecha_contratacion, id_cuenta_acceso FROM administrador WHERE id_administrador = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idAdministrador);
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
    public List<Administrador> findAll() throws SQLException {
        String sql = "SELECT id_administrador, nombre, telefono, fecha_contratacion, id_cuenta_acceso FROM administrador";
        List<Administrador> lista = new ArrayList<>();
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

    private Administrador mapear(ResultSet rs) throws SQLException {
        Administrador admin = new Administrador();
        admin.setIdEmpleado(rs.getInt("id_administrador"));
        admin.setNombre(rs.getString("nombre"));
        admin.setTelefono(rs.getString("telefono"));
        Date fecha = rs.getDate("fecha_contratacion");
        if (fecha != null) {
            admin.setFechaDeContratacion(fecha.toLocalDate());
        }
        int idCuenta = rs.getInt("id_cuenta_acceso");
        DatosCuentaEmpleado cuenta = datosCuentaEmpleadoDao.findBy(idCuenta);
        admin.setDatosCuentaEmpleado(cuenta);
        return admin;
    }
}