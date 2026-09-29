package pe.edu.pucp.rinconSatipeno.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;

import pe.edu.pucp.RinconSatipeno.DBManager.DBManager;

import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.DatosContacto;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.EstadoReserva;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.Reserva;

import pe.edu.pucp.rinconSatipeno.dao.DatosContactoDao;
import pe.edu.pucp.rinconSatipeno.dao.MesaDao;
import pe.edu.pucp.rinconSatipeno.dao.ReservaDao;

public class ReservaDaoImplement implements ReservaDao {

    private final MesaDao mesaDao = new MesaDaoImplement();
    private final DatosContactoDao datosContactoDao =
            new DatosContactoDaoImplement();

    @Override
    public void insert(Reserva reserva) throws SQLException {

        String sql =
                "{CALL insertar_reserva(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            setParametros(cs, reserva);

            cs.registerOutParameter(9, Types.INTEGER);

            cs.execute();

            reserva.setIdReserva(cs.getInt(9));
        }
    }

    @Override
    public void update(Reserva reserva) throws SQLException {

        String sql =
                "{CALL modificar_reserva(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            setParametros(cs, reserva);

            cs.setInt(9, reserva.getIdReserva());

            cs.executeUpdate();
        }
    }

    @Override
    public void delete(Integer idReserva) throws SQLException {

        String sql = "{CALL eliminar_reserva(?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            cs.setInt(1, idReserva);

            cs.executeUpdate();
        }
    }

    @Override
    public Reserva findBy(Integer idReserva) throws SQLException {

        String sql =
                "{CALL buscar_reserva_por_id(?)}";

        try (
                Connection con =
                        DBManager.getInstance().getConnection();

                CallableStatement cs =
                        con.prepareCall(sql)
        ) {

            cs.setInt(1, idReserva);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }

        return null;
    }

    @Override
    public ArrayList<Reserva> findAll() throws SQLException {

        String sql =
                "{CALL listar_reservas()}";

        ArrayList<Reserva> reservas =
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
                reservas.add(mapear(rs));
            }
        }

        return reservas;
    }

    private void setParametros(
            CallableStatement cs,
            Reserva reserva
    ) throws SQLException {

        cs.setDate(
                1,
                Date.valueOf(reserva.getFecha())
        );

        cs.setTime(
                2,
                Time.valueOf(reserva.getHoraInicio())
        );

        cs.setTime(
                3,
                Time.valueOf(reserva.getHoraFin())
        );

        cs.setInt(
                4,
                reserva.getCantidadPersonas()
        );

        cs.setString(
                5,
                reserva.getEstado().name()
        );

        cs.setString(
                6,
                reserva.getCodigoAcceso()
        );

        Mesa mesa = reserva.getMesa();

        cs.setInt(
                7,
                mesa.getId()
        );

        DatosContacto contacto =
                reserva.getDatosContacto();

        cs.setInt(
                8,
                contacto.getIdContacto()
        );
    }

    private Reserva mapear(ResultSet rs)
            throws SQLException {

        Reserva reserva = new Reserva();

        reserva.setIdReserva(
                rs.getInt("id_reserva")
        );

        reserva.setFecha(
                rs.getDate("fecha").toLocalDate()
        );

        reserva.setHoraInicio(
                rs.getTime("hora_inicio").toLocalTime()
        );

        reserva.setHoraFin(
                rs.getTime("hora_fin").toLocalTime()
        );

        reserva.setCantidadPersonas(
                rs.getInt("cantidad_personas")
        );

        reserva.setEstado(
                EstadoReserva.valueOf(
                        rs.getString("estado")
                )
        );

        reserva.setCodigoAcceso(
                rs.getString("codigo_acceso")
        );

        Mesa mesa =
                mesaDao.findBy(
                        rs.getInt("id_mesa")
                );

        reserva.setMesa(mesa);

        DatosContacto contacto =
                datosContactoDao.findBy(
                        rs.getInt("id_contacto")
                );

        reserva.setDatosContacto(contacto);

        return reserva;
    }
}