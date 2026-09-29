package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.DatosContacto;
import pe.edu.pucp.rinconSatipeno.dao.DatosContactoDao;

public class DatosContactoDaoImplement
        implements DatosContactoDao {

    @Override
    public void insert(DatosContacto contacto)
            throws SQLException {

        String sql =
                "{CALL insertar_datos_contacto(?, ?, ?, ?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            cs.setString(1, contacto.getNombre());
            cs.setString(2, contacto.getTelefono());
            cs.setString(3, contacto.getCorreo());

            cs.registerOutParameter(4, Types.INTEGER);

            cs.execute();

            contacto.setIdContacto(
                    cs.getInt(4)
            );
        }
    }

    @Override
    public void update(DatosContacto contacto)
            throws SQLException {

        String sql =
                "{CALL modificar_datos_contacto(?, ?, ?, ?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            cs.setInt(
                    1,
                    contacto.getIdContacto()
            );

            cs.setString(
                    2,
                    contacto.getNombre()
            );

            cs.setString(
                    3,
                    contacto.getTelefono()
            );

            cs.setString(
                    4,
                    contacto.getCorreo()
            );

            cs.executeUpdate();
        }
    }

    @Override
    public void delete(Integer idContacto)
            throws SQLException {

        String sql =
                "{CALL eliminar_datos_contacto(?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            cs.setInt(1, idContacto);

            cs.executeUpdate();
        }
    }

    @Override
    public DatosContacto findBy(Integer idContacto)
            throws SQLException {

        String sql =
                "{CALL buscar_datos_contacto_por_id(?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
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
                "{CALL listar_datos_contacto()}";

        ArrayList<DatosContacto> contactos =
                new ArrayList<>();

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql);

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
}