package pe.edu.pucp.RinconSatipeno.Modelo.reservas;

import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
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
    private datosContacto datosContacto;

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
        setCantidadPersonas(reserva.getCantidadPersonas());
        setEstado(reserva.getEstado());
        setCodigoAcceso(reserva.getCodigoAcceso());
        setMesa(reserva.getMesa());
        setDatosContacto(reserva.getDatosContacto());
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

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        if (cantidadPersonas <= 0) {
            throw new IllegalArgumentException("cantidadPersonas debe ser mayor a cero");
        }
        this.cantidadPersonas = cantidadPersonas;
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

    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public void setCodigoAcceso(String codigoAcceso) {
        if (codigoAcceso == null || codigoAcceso.trim().isEmpty()) {
            throw new IllegalArgumentException("codigoAcceso no puede ser nulo o vacío");
        }
        this.codigoAcceso = codigoAcceso;
    }

    public Mesa getMesa() {
        if (mesa != null) {
            return new Mesa(mesa);
        }
        return null;
    }

    public void setMesa(Mesa mesa) {
        if (mesa == null) {
            throw new IllegalArgumentException("mesa no puede ser nula");
        }
        this.mesa = new Mesa(mesa);
    }

    public datosContacto getDatosContacto() {
        if (datosContacto != null) {
            return new datosContacto(datosContacto);
        }
        return null;
    }

    public void setDatosContacto(datosContacto datosContacto) {
        if (datosContacto == null) {
            throw new IllegalArgumentException("datosContacto no puede ser nulo");
        }
        this.datosContacto = new datosContacto(datosContacto);
    }
}