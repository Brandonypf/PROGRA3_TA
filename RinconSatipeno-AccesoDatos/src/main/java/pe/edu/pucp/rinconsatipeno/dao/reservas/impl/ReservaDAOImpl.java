package pe.edu.pucp.rinconsatipeno.dao.reservas.impl;

import pe.edu.pucp.rinconsatipeno.dao.mesas.impl.MesaDAOImpl;

import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

import pe.edu.pucp.rinconsatipeno.dbmanager.DBManager;

import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.DatosContacto;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.EstadoReserva;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.Reserva;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.ReservaCalendarioDTO;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Zona;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.EstadoMesa;

import pe.edu.pucp.rinconsatipeno.dao.reservas.DatosContactoDAO;
import pe.edu.pucp.rinconsatipeno.dao.mesas.MesaDAO;
import pe.edu.pucp.rinconsatipeno.dao.reservas.ReservaDAO;

public class ReservaDAOImpl implements ReservaDAO {

    private final MesaDAO mesaDao = new MesaDAOImpl();
    private final DatosContactoDAO datosContactoDao =
            new DatosContactoDAOImpl();

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

    @Override
    public List<ReservaCalendarioDTO> listarCalendario(LocalDate fecha) throws SQLException {
        String sql = "SELECT r.id_reserva, c.nombre, m.numero, r.fecha, " +
                "r.hora_inicio, r.cantidad_personas, r.estado " +
                "FROM reserva r JOIN datos_contacto c ON c.id_contacto = r.id_contacto " +
                "JOIN mesa m ON m.id_mesa = r.id_mesa " +
                "WHERE r.fecha = ? ORDER BY r.hora_inicio, m.numero";
        List<ReservaCalendarioDTO> lista = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ReservaCalendarioDTO(
                            rs.getInt("id_reserva"), rs.getString("nombre"),
                            rs.getInt("numero"), rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora_inicio").toLocalTime(),
                            rs.getInt("cantidad_personas"),
                            EstadoReserva.valueOf(rs.getString("estado"))));
                }
            }
        }
        return lista;
    }

    @Override
    public List<Mesa> buscarMesasDisponibles(LocalDate fecha, LocalTime inicio,
                                           LocalTime fin, int personas) throws SQLException {
        String sql = "SELECT m.id_mesa, m.numero, m.capacidad, m.zona, m.estado " +
                "FROM mesa m WHERE m.capacidad >= ? AND NOT EXISTS (" +
                "SELECT 1 FROM reserva r WHERE r.id_mesa = m.id_mesa AND r.fecha = ? " +
                "AND r.estado IN ('PENDIENTE', 'CONFIRMADA') " +
                "AND r.hora_inicio < ? AND r.hora_fin > ?) " +
                "ORDER BY m.capacidad, m.numero";
        List<Mesa> mesas = new ArrayList<>();
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, personas);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setTime(3, Time.valueOf(fin));
            ps.setTime(4, Time.valueOf(inicio));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mesa mesa = new Mesa();
                    mesa.setId(rs.getInt("id_mesa"));
                    mesa.setNumero(rs.getInt("numero"));
                    mesa.setCapacidad(rs.getInt("capacidad"));
                    mesa.setZona(Zona.valueOf(rs.getString("zona")));
                    mesa.setEstado(EstadoMesa.valueOf(rs.getString("estado")));
                    mesas.add(mesa);
                }
            }
        }
        return mesas;
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM reserva WHERE id_reserva = ? LIMIT 1";
        try (Connection con = DBManager.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}