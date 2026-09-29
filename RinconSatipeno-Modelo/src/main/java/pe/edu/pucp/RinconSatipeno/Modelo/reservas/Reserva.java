package pe.edu.pucp.RinconSatipeno.Modelo.reservas;

import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    private int idReserva;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int cantidadPersonas;
    private EstadoReserva estado;
    private String codigoAcceso;
    private Mesa mesa;
    private DatosContacto datosContacto;

    public Reserva() {
        this.estado = EstadoReserva.PENDIENTE;
    }

    public Reserva(final Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("reserva no puede ser nula");
        }

        this.idReserva = reserva.idReserva;
        this.fecha = reserva.fecha;
        this.horaInicio = reserva.horaInicio;
        this.horaFin = reserva.horaFin;
        this.cantidadPersonas = reserva.cantidadPersonas;
        this.estado = reserva.estado;
        this.codigoAcceso = reserva.codigoAcceso;
        this.mesa = reserva.mesa;
        this.datosContacto = reserva.datosContacto;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        if (idReserva < 0) {
            throw new IllegalArgumentException(
                    "idReserva no puede ser negativo"
            );
        }

        this.idReserva = idReserva;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException(
                    "fecha no puede ser nula"
            );
        }

        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        if (horaInicio == null) {
            throw new IllegalArgumentException(
                    "horaInicio no puede ser nula"
            );
        }

        if (horaFin != null) {
            validarHorario(horaInicio, horaFin);
        }

        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        if (horaFin == null) {
            throw new IllegalArgumentException(
                    "horaFin no puede ser nula"
            );
        }

        if (horaInicio != null) {
            validarHorario(horaInicio, horaFin);
        }

        this.horaFin = horaFin;
    }

    private void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (!horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException(
                    "horaFin debe ser posterior a horaInicio"
            );
        }

        long minutos = Duration.between(horaInicio, horaFin).toMinutes();

        if (minutos > 120) {
            throw new IllegalArgumentException(
                    "la reserva no puede durar más de 2 horas"
            );
        }
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        if (cantidadPersonas <= 0) {
            throw new IllegalArgumentException(
                    "cantidadPersonas debe ser mayor a cero"
            );
        }

        this.cantidadPersonas = cantidadPersonas;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        if (estado == null) {
            throw new IllegalArgumentException(
                    "estado no puede ser nulo"
            );
        }

        this.estado = estado;
    }

    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public void setCodigoAcceso(String codigoAcceso) {
        if (codigoAcceso == null || codigoAcceso.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "codigoAcceso no puede ser nulo o vacío"
            );
        }

        if (codigoAcceso.length() > 64) {
            throw new IllegalArgumentException(
                    "codigoAcceso no puede superar 64 caracteres"
            );
        }

        this.codigoAcceso = codigoAcceso;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException(
                    "mesa no puede ser nula"
            );
        }

        this.mesa = mesa;
    }

    public DatosContacto getDatosContacto() {
        return datosContacto;
    }

    public void setDatosContacto(DatosContacto datosContacto) {
        if (datosContacto == null) {
            throw new IllegalArgumentException(
                    "datosContacto no puede ser nulo"
            );
        }

        this.datosContacto = datosContacto;
    }
}