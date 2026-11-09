package pe.edu.pucp.rinconsatipeno.dao.reservas.impl;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.DatosContacto;
import pe.edu.pucp.rinconsatipeno.dao.reservas.DatosContactoDAO;

public class DatosContactoDAOImpl
        implements DatosContactoDAO {

    @Override
    public void insert(DatosContacto contacto)
            throws SQLException {

        String sql =
                "INSERT INTO datos_contacto (nombre, telefono, correo) VALUES (?, ?, ?)";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                PreparedStatement cs =
                        con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            cs.setString(1, contacto.getNombre());
            cs.setString(2, contacto.getTelefono());
            cs.setString(3, contacto.getCorreo());

            cs.executeUpdate();
            try (ResultSet claves = cs.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el ID generado");
                contacto.setIdContacto(claves.getInt(1));
            }
        }
    }

    @Override
    public void update(DatosContacto contacto)
            throws SQLException {

        String sql =
                "UPDATE datos_contacto SET nombre = ?, telefono = ?, correo = ? WHERE id_contacto = ?";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                PreparedStatement cs =
                        con.prepareStatement(sql)
        ) {

            cs.setInt(4, contacto.getIdContacto());

            cs.setString(1, contacto.getNombre());

            cs.setString(2, contacto.getTelefono());

            cs.setString(3, contacto.getCorreo());

            cs.executeUpdate();
        }
    }

    @Override
    public void delete(Integer idContacto)
            throws SQLException {

        String sql =
                "DELETE FROM datos_contacto WHERE id_contacto = ?";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                PreparedStatement cs =
                        con.prepareStatement(sql)
        ) {

            cs.setInt(1, idContacto);

            cs.executeUpdate();
        }
    }

    @Override
    public DatosContacto findBy(Integer idContacto)
            throws SQLException {

        String sql =
                "SELECT * FROM datos_contacto WHERE id_contacto = ?";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                PreparedStatement cs =
                        con.prepareStatement(sql)
        ) {

            cs.setInt(1, idContacto);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }

        return null;
    }

    @Override
    public ArrayList<DatosContacto> findAll()
            throws SQLException {

        String sql =
                "SELECT * FROM datos_contacto";

        ArrayList<DatosContacto> contactos =
                new ArrayList<>();

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                PreparedStatement cs =
                        con.prepareStatement(sql);

                ResultSet rs =
                        cs.executeQuery()
        ) {

            while (rs.next()) {
                contactos.add(
                        mapear(rs)
                );
            }
        }

        return contactos;
    }

    private DatosContacto mapear(ResultSet rs)
            throws SQLException {

        DatosContacto contacto =
                new DatosContacto();

        contacto.setIdContacto(
                rs.getInt("id_contacto")
        );

        contacto.setNombre(
                rs.getString("nombre")
        );

        contacto.setTelefono(
                rs.getString("telefono")
        );

        contacto.setCorreo(
                rs.getString("correo")
        );

        return contacto;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM datos_contacto WHERE id_contacto = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}