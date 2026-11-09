package pe.edu.pucp.rinconsatipeno.negocio.reservas.impl;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import pe.edu.pucp.rinconsatipeno.modelo.mesas.Mesa;
import pe.edu.pucp.rinconsatipeno.modelo.reservas.ReservaCalendarioDTO;

import pe.edu.pucp.rinconsatipeno.modelo.reservas.Reserva;
import pe.edu.pucp.rinconsatipeno.negocio.BLException;
import pe.edu.pucp.rinconsatipeno.negocio.reservas.ReservaBL;

import pe.edu.pucp.rinconsatipeno.dao.reservas.ReservaDAO;
import pe.edu.pucp.rinconsatipeno.dao.reservas.impl.ReservaDAOImpl;

public class ReservaBLImpl implements ReservaBL {

    private final ReservaDAO reservaDao =
            new ReservaDAOImpl();

    @Override
    public List<ReservaCalendarioDTO> listarCalendario(LocalDate fecha) throws BLException {
        if (fecha == null) throw new BLException("Ingresa una fecha válida");
        try {
            return reservaDao.listarCalendario(fecha);
        } catch (SQLException ex) {
            throw new BLException("No se pudo consultar el calendario", ex);
        }
    }

    @Override
    public List<Mesa> buscarMesasDisponibles(LocalDate fecha, LocalTime inicio,
                                            LocalTime fin, int personas) throws BLException {
        if (fecha == null || inicio == null || fin == null || !fin.isAfter(inicio)) {
            throw new BLException("Selecciona una fecha y un horario válidos");
        }
        if (personas <= 0) throw new BLException("La cantidad de personas debe ser mayor a cero");
        if (Duration.between(inicio, fin).toMinutes() > 120) {
            throw new BLException("La reserva no puede durar más de 2 horas");
        }
        try {
            return reservaDao.buscarMesasDisponibles(fecha, inicio, fin, personas);
        } catch (SQLException ex) {
            throw new BLException("No se pudo consultar la disponibilidad", ex);
        }
    }

    @Override
    public List<Reserva> findAll() throws BLException {

        try {
            return reservaDao.findAll();
        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudieron listar las reservas",
                    ex
            );
        }
    }

    @Override
    public Reserva findBy(Integer id) throws BLException {

        validarId(id);

        try {
            Reserva reserva = reservaDao.findBy(id);

            if (reserva == null) {
                throw new BLException(
                        "No se encontró la reserva"
                );
            }

            return reserva;

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo buscar la reserva",
                    ex
            );
        }
    }

    @Override
    public void insert(Reserva reserva)
            throws BLException {

        validarReserva(reserva);

        try {
            reservaDao.insert(reserva);
        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo registrar la reserva",
                    ex
            );
        }
    }

    @Override
    public void update(Reserva reserva)
            throws BLException {

        validarReserva(reserva);

        if (reserva.getIdReserva() <= 0) {
            throw new BLException(
                    "La reserva debe tener un ID válido"
            );
        }

        validarExiste(reserva.getIdReserva());

        try {
            reservaDao.update(reserva);
        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo actualizar la reserva",
                    ex
            );
        }
    }

    @Override
    public void delete(Integer id)
            throws BLException {

        validarId(id);
        validarExiste(id);

        try {
            reservaDao.delete(id);
        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo eliminar la reserva",
                    ex
            );
        }
    }

    private void validarExiste(Integer id)
            throws BLException {

        try {
            if (!reservaDao.existsById(id)) {
                throw new BLException(
                        "La reserva no existe"
                );
            }

        } catch (SQLException ex) {
            throw new BLException(
                    "No se pudo verificar la existencia de la reserva",
                    ex
            );
        }
    }

    private void validarId(Integer id)
            throws BLException {

        if (id == null || id <= 0) {
            throw new BLException(
                    "El ID de la reserva no es válido"
            );
        }
    }

    private void validarReserva(Reserva reserva)
            throws BLException {

        if (reserva == null) {
            throw new BLException(
                    "La reserva no puede ser nula"
            );
        }

        if (reserva.getFecha() == null) {
            throw new BLException(
                    "La reserva debe tener una fecha"
            );
        }

        if (reserva.getHoraInicio() == null ||
                reserva.getHoraFin() == null) {

            throw new BLException(
                    "La reserva debe tener hora de inicio y fin"
            );
        }

        if (!reserva.getHoraFin()
                .isAfter(reserva.getHoraInicio())) {

            throw new BLException(
                    "La hora final debe ser posterior a la hora inicial"
            );
        }

        long minutos = Duration.between(
                reserva.getHoraInicio(),
                reserva.getHoraFin()
        ).toMinutes();

        if (minutos > 120) {
            throw new BLException(
                    "La reserva no puede durar más de 2 horas"
            );
        }

        if (reserva.getCantidadPersonas() <= 0) {
            throw new BLException(
                    "La cantidad de personas debe ser mayor a cero"
            );
        }

        if (reserva.getMesa() == null) {
            throw new BLException(
                    "La reserva debe tener una mesa"
            );
        }

        if (reserva.getDatosContacto() == null) {
            throw new BLException(
                    "La reserva debe tener datos de contacto"
            );
        }

        if (reserva.getEstado() == null) {
            throw new BLException(
                    "La reserva debe tener un estado"
            );
        }

        if (reserva.getCodigoAcceso() == null ||
                reserva.getCodigoAcceso().trim().isEmpty()) {

            throw new BLException(
                    "La reserva debe tener un código de acceso"
            );
        }
    }
}
