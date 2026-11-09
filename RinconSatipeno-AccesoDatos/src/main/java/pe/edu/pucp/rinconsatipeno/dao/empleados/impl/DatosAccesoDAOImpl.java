package pe.edu.pucp.rinconsatipeno.dao.empleados.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.DatosAcceso;
import pe.edu.pucp.rinconsatipeno.modelo.empleados.EstadoEmpleado;
import pe.edu.pucp.rinconsatipeno.dao.empleados.DatosAccesoDAO;
import pe.edu.pucp.rinconsatipeno.dao.transacciones.TransactionsManager;

public class DatosAccesoDAOImpl implements DatosAccesoDAO {

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
    public void insert(DatosAcceso cuenta) throws SQLException {
        String sql = "INSERT INTO cuenta_acceso (email, contrasenia, estado) VALUES (?, ?, ?)";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cuenta.getEmail());
                ps.setString(2, cuenta.getContrasenia());
                ps.setString(3, cuenta.getEstado().name());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        cuenta.setIdCuentaAcceso(rs.getInt(1));
                    }
                }
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public void update(DatosAcceso cuenta) throws SQLException {
        String sql = "UPDATE cuenta_acceso SET email = ?, contrasenia = ?, estado = ? WHERE id_cuenta_acceso = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, cuenta.getEmail());
                ps.setString(2, cuenta.getContrasenia());
                ps.setString(3, cuenta.getEstado().name());
                ps.setInt(4, cuenta.getIdCuentaAcceso());
                ps.executeUpdate();
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public void delete(Integer idCuentaAcceso) throws SQLException {
        String sql = "DELETE FROM cuenta_acceso WHERE id_cuenta_acceso = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCuentaAcceso);
                ps.executeUpdate();
            }
        } finally {
            cerrarConexionSiNoEsTransaccional(con);
        }
    }

    @Override
    public DatosAcceso findBy(Integer idCuentaAcceso) throws SQLException {
        String sql = "SELECT id_cuenta_acceso, email, contrasenia, estado FROM cuenta_acceso WHERE id_cuenta_acceso = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCuentaAcceso);
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
    public DatosAcceso findByEmail(String email) throws SQLException {
        String sql = "SELECT id_cuenta_acceso, email, contrasenia, estado FROM cuenta_acceso WHERE email = ?";
        Connection con = null;
        try {
            con = obtenerConexion();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);
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
    public List<DatosAcceso> findAll() throws SQLException {
        String sql = "SELECT id_cuenta_acceso, email, contrasenia, estado FROM cuenta_acceso";
        List<DatosAcceso> lista = new ArrayList<>();
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

    private DatosAcceso mapear(ResultSet rs) throws SQLException {
        DatosAcceso cuenta = new DatosAcceso();
        cuenta.setIdCuentaAcceso(rs.getInt("id_cuenta_acceso"));
        cuenta.setEmail(rs.getString("email"));
        cuenta.setContrasenia(rs.getString("contrasenia"));
        cuenta.setEstado(EstadoEmpleado.valueOf(rs.getString("estado")));
        return cuenta;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM cuenta_acceso WHERE id_cuenta_acceso = ? LIMIT 1";
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
