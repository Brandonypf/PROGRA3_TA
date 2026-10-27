package pe.edu.pucp.RinconSatipeno.Modelo.cuentas;

import pe.edu.pucp.RinconSatipeno.Modelo.mesas.Mesa;
import pe.edu.pucp.RinconSatipeno.Modelo.empleados.Mozo;
import pe.edu.pucp.RinconSatipeno.Modelo.reservas.Reserva;
import java.time.LocalDateTime;

public class CuentaConsumo {
    private int idCuenta;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private LocalDateTime fechaPago;
    private EstadoCuenta estado;
    private double montoTotalPagar;
    private String enlacePago;
    private Mesa mesa;
    private Mozo mozo;
    private Reserva reserva;

    public CuentaConsumo() {
    }

    public CuentaConsumo(final CuentaConsumo cuentaConsumo) {
        if (cuentaConsumo == null) {
            throw new IllegalArgumentException("cuentaConsumo no puede ser nula");
        }
        setIdCuenta(cuentaConsumo.getIdCuenta());
        setFechaApertura(cuentaConsumo.getFechaApertura());
        setFechaCierre(cuentaConsumo.getFechaCierre());
        setFechaPago(cuentaConsumo.getFechaPago());
        setEstado(cuentaConsumo.getEstado());
        setMontoTotalPagar(cuentaConsumo.getMontoTotalPagar());
        setEnlacePago(cuentaConsumo.getEnlacePago());
        setMesa(cuentaConsumo.getMesa());
        setMozo(cuentaConsumo.getMozo());
        setReserva(cuentaConsumo.getReserva());
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        if (idCuenta < 0) {
            throw new IllegalArgumentException("idCuenta no puede ser negativo");
        }
        this.idCuenta = idCuenta;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        if (fechaApertura == null) {
            throw new IllegalArgumentException("fechaApertura no puede ser nula");
        }
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        if (estado == null) {
            throw new IllegalArgumentException("estado no puede ser nulo");
        }
        this.estado = estado;
    }

    public double getMontoTotalPagar() {
        return montoTotalPagar;
    }

    public void setMontoTotalPagar(double montoTotalPagar) {
        if (montoTotalPagar < 0) {
            throw new IllegalArgumentException("montoTotalPagar no puede ser negativo");
        }
        this.montoTotalPagar = montoTotalPagar;
    }

    public String getEnlacePago() {
        return enlacePago;
    }

    public void setEnlacePago(String enlacePago) {
        this.enlacePago = enlacePago;
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

    public Mozo getMozo() {
        if (mozo != null) {
            return new Mozo(mozo);
        }
        return null;
    }

    public void setMozo(Mozo mozo) {
        if (mozo == null) {
            throw new IllegalArgumentException("mozo no puede ser nulo");
        }
        this.mozo = new Mozo(mozo);
    }

    public Reserva getReserva() {
        if (reserva != null) {
            return new Reserva(reserva);
        }
        return null;
    }

    public void setReserva(Reserva reserva) {
        if (reserva == null) {
            this.reserva = null;
        } else {
            this.reserva = new Reserva(reserva);
        }
    }
}