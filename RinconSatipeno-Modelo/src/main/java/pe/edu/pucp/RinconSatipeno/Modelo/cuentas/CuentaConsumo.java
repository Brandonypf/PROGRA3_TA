package pe.edu.pucp.RinconSatipeno.Modelo.cuentas;


import java.time.LocalDate;

public class CuentaConsumo {
    private int idCuenta;
    private LocalDate fechaApertura;
    private LocalDate fechaCierre;
    private EstadoCuenta estado;
    private double montoTotalPagar;

    public CuentaConsumo() {
    }
    public CuentaConsumo(final CuentaConsumo cuentaConsumo) {
        if (cuentaConsumo == null) {
            throw new IllegalArgumentException("cuentaConsumo no puede ser nula");
        }
        setIdCuenta(cuentaConsumo.getIdCuenta());
        setFechaApertura(cuentaConsumo.getFechaApertura());
        setFechaCierre(cuentaConsumo.getFechaCierre());
        setEstado(cuentaConsumo.getEstado());
        setMontoTotalPagar(cuentaConsumo.getMontoTotalPagar());
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

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        if (fechaApertura == null) {
            throw new IllegalArgumentException("fechaApertura no puede ser nula");
        }
        this.fechaApertura = fechaApertura;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDate fechaCierre) {
        // fechaCierre puede quedar en null mientras la cuenta siga ABIERTA
        this.fechaCierre = fechaCierre;
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
}