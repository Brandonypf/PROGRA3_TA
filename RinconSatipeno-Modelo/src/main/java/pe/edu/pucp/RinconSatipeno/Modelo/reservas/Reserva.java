package pe.edu.pucp.RinconSatipeno.Modelo.reservas;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {
    private int idReserva;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int numPersonas;
    private EstadoReserva estado;
    private String nombreContacto;
    private String telefonoContacto;
    private String correoContacto;

    public Reserva() {
    }

    public Reserva(final Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("reserva no puede ser nula");
        }
        setIdReserva(reserva.getIdReserva());
        setFecha(reserva.getFecha());
        setHoraInicio(reserva.getHoraInicio());
        setHoraFin(reserva.getHoraFin());
        setNumPersonas(reserva.getNumPersonas());
        setEstado(reserva.getEstado());
        setNombreContacto(reserva.getNombreContacto());
        setTelefonoContacto(reserva.getTelefonoContacto());
        setCorreoContacto(reserva.getCorreoContacto());
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        if (idReserva < 0) {
            throw new IllegalArgumentException("idReserva no puede ser negativo");
        }
        this.idReserva = idReserva;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("fecha no puede ser nula");
        }
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        if (horaInicio == null) {
            throw new IllegalArgumentException("horaInicio no puede ser nula");
        }
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        if (horaFin == null) {
            throw new IllegalArgumentException("horaFin no puede ser nula");
        }
        if (horaInicio != null && !horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("horaFin debe ser posterior a horaInicio");
        }
        this.horaFin = horaFin;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        if (numPersonas <= 0) {
            throw new IllegalArgumentException("numPersonas debe ser mayor a cero");
        }
        this.numPersonas = numPersonas;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        if (estado == null) {
            throw new IllegalArgumentException("estado no puede ser nulo");
        }
        this.estado = estado;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        if (nombreContacto == null || nombreContacto.isEmpty()) {
            throw new IllegalArgumentException("nombreContacto no puede ser nulo o vacío");
        }
        this.nombreContacto = nombreContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        if (telefonoContacto == null || telefonoContacto.isEmpty()) {
            throw new IllegalArgumentException("telefonoContacto no puede ser nulo o vacío");
        }
        this.telefonoContacto = telefonoContacto;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        if (correoContacto == null || correoContacto.isEmpty() || !correoContacto.contains("@")) {
            throw new IllegalArgumentException("correoContacto no puede ser nulo, vacío o inválido");
        }
        this.correoContacto = correoContacto;
    }
}
